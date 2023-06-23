package com.user.schedule.controller;

import com.user.schedule.database.model.Master;
import com.user.schedule.database.model.Profile;
import com.user.schedule.database.model.User;
import com.user.schedule.database.service.MasterService;
import com.user.schedule.database.service.ResponseForm;
import com.user.schedule.database.service.UsersService;
import com.user.schedule.security.AuthenticateRequest;
import com.user.schedule.security.AuthenticateResponse;
import com.user.schedule.security.service.JwtUtil;
import com.user.schedule.security.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class UsersController {

    private final UsersService usersService;

    private final MasterService masterService;

    private final JwtUtil jwtTokenUtil;


    private final AuthenticationManager authenticationManager;

    private final MyUserDetailService myUserDetailService;


    @Autowired
    public UsersController(UsersService usersService, MasterService masterService, JwtUtil jwtTokenUtil,
                           AuthenticationManager authenticationManager,
                           MyUserDetailService myUserDetailService) {
        this.usersService = usersService;
        this.masterService = masterService;
        this.jwtTokenUtil = jwtTokenUtil;

        this.authenticationManager = authenticationManager;
        this.myUserDetailService = myUserDetailService;
    }


    @PostMapping("/api/auth/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticateRequest authenticateRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticateRequest.getCode(), authenticateRequest.getPassword())
            );
        } catch (Exception e) {
            ResponseForm responseForm = new ResponseForm("validation-error", "Incorrect Username or Password",
                    null);
            return ResponseEntity.status(422).body(responseForm);
        }
        final UserDetails userDetails = myUserDetailService
                .loadUserByUsername(authenticateRequest.getCode());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
//        final String expAt = (jwtTokenUtil.extractExpiration(jwt).toInstant().atZone(ZoneId.of("Iran")).format(DateTimeFormatter.ofPattern("EEE MMM d yyyy hh:mm:ss")))+" GMT+0330 (Iran Standard Time)";
        final String expAt = (jwtTokenUtil.extractExpiration(jwt).toInstant().atZone(ZoneId.of("Iran")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        final User user = usersService.findByCode(jwtTokenUtil.extractUsername(jwt));
        ResponseForm responseForm = new ResponseForm("success", null, new AuthenticateResponse(jwt, expAt, user));
        return ResponseEntity.ok(responseForm);
    }


    // <---------------------- USERS PART --------------------------->

    @GetMapping("/api/users")
    public ResponseForm getUsers(
            @RequestParam(value = "search", required = false, defaultValue = "") String lastName,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        return new ResponseForm("success", null, usersService.getUserList(lastName, pageSize, page));
    }

    @PostMapping("/api/users")
    public ResponseForm addUser(@RequestBody User user) throws Exception {
        user.setRole(user.getRole().toUpperCase());
        return new ResponseForm("success", null, usersService.addUser(user));
    }


    @GetMapping("/api/users/{id}")
    public ResponseForm getUserById(@PathVariable int id) {
        try {
            User user = usersService.getById(id);
            return new ResponseForm("success", null, user);

        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id " + e.getMessage(), null);
        }
    }

    @GetMapping("/api/masters")
    public ResponseForm getMastersList(
            @RequestParam(value = "search", required = false, defaultValue = "") String name,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        System.out.println(name);
        List<Master> masters = masterService.getMastersList(name, pageSize, page);
        return new ResponseForm("success", null, masters);
    }

    @PutMapping("/api/users/{id}")
    public ResponseForm putUser(@PathVariable int id, @RequestBody UsersService.UpdateUser user) {
        try {
            User updatedUser = usersService.putUser(id, user);
            return new ResponseForm("success", null, updatedUser);
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id or body " + e.getMessage(), null);
        }
    }

    public void updateUser(@PathVariable int id, @RequestBody User user) {
        try {
            User updatedUser = usersService.editUser(id, user);
            new ResponseForm("success", null, updatedUser);
        } catch (Exception e) {
            new ResponseForm("failed", "invalid id or body " + e.getMessage(), null);
        }
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseForm deleteUser(@PathVariable int id) {
        try {
            usersService.deleteUser(id);
            return new ResponseForm("success", null, null);
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id " + e.getMessage(), null);
        }

    }

    @GetMapping("api/users/profile")
    public ResponseForm getUserProfile(@RequestHeader String authorization) {

        try {
            String userCode;
            if (authorization.startsWith("Bearer ")) {
                userCode = jwtTokenUtil.extractUsername(authorization.substring(7));
            } else {
                userCode = jwtTokenUtil.extractUsername(authorization);
            }
            User user = usersService.findByCode(userCode);

            return new ResponseForm("success", null, new Profile(user.getId(), user.getFirstName(), user.getLastName(), user.getCode(), user.getRole()));
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid token", null);
        }

    }

    @PostMapping("api/users/profile")
    public ResponseForm updateUserProfile(@RequestBody Profile.UserPass userPass, @RequestHeader String authorization) {
        try {
            String userCode;

            if (authorization.startsWith("Bearer ")) {
                userCode = jwtTokenUtil.extractUsername(authorization.substring(7));
            } else {
                userCode = jwtTokenUtil.extractUsername(authorization);
            }

            User user = usersService.findByCode(userCode);
            if (!userPass.getFirstName().equals("")) {
                user.setFirstName(userPass.getFirstName());
            }
            if (!userPass.getLastName().equals("")) {
                user.setLastName(userPass.getLastName());
            }
            updateUser(user.getId(), user);

            return new ResponseForm("success", null, new Profile(user.getId(), user.getFirstName(), user.getLastName(), user.getCode(), user.getRole()));
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid token", null);
        }

    }

    @PostMapping("api/users/profile/change-password")
    public ResponseForm changePassword(@RequestBody Profile.ChangePass newPassData, @RequestHeader String authorization) {
        try {
            String userCode;
            if (authorization.startsWith("Bearer ")) {
                userCode = jwtTokenUtil.extractUsername(authorization.substring(7));
            } else {
                userCode = jwtTokenUtil.extractUsername(authorization);
            }

            User user = usersService.findByCode(userCode);

            if (newPassData.getCurrentPassword().equals(user.getPassword())) {
                user.setPassword(newPassData.getNewPassword());
            } else {
                throw new Exception("Current Password is Wrong!");
            }

            updateUser(user.getId(), user);

            return new ResponseForm("success", null, null);
        } catch (Exception e) {
            return new ResponseForm("failed", e.getMessage(), null);
        }
    }

    @PostMapping("/api/users/add-list")
    public ResponseForm addUsersList(@RequestBody List<User> users) throws Exception {
//        try {
        return new ResponseForm("success", null, usersService.addUsersList(users));
//        } catch (Exception e) {
//            return new ResponseForm("failed", "invalid input", null);
//        }
    }


}
