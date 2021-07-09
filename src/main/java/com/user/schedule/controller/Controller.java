package com.user.schedule.controller;

import com.user.schedule.database.model.*;
import com.user.schedule.database.service.*;
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
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


@CrossOrigin(origins = "*")
@RestController
public class Controller {

    private final UsersService usersService;

    private final BellsService bellsService;

    private final DaysService daysService;

    private final CoursesService coursesService;

    private final AnnouncementsService announcementsService;

    private final TimeTableBellsService timeTableBellsService;

    private final TimeTableService timeTableService;


    private final AuthenticationManager authenticationManager;

    private final MyUserDetailService myUserDetailService;

    private final JwtUtil jwtTokenUtil;

    @Autowired
    public Controller(UsersService usersService, BellsService bellsService, DaysService daysService,
                      CoursesService coursesService, AnnouncementsService announcementsService,
                      TimeTableBellsService timeTableBellsService, TimeTableService timeTableService,
                      AuthenticationManager authenticationManager, MyUserDetailService myUserDetailService, JwtUtil jwtTokenUtil) {
        this.usersService = usersService;
        this.bellsService = bellsService;
        this.daysService = daysService;
        this.coursesService = coursesService;
        this.announcementsService = announcementsService;
        this.timeTableBellsService = timeTableBellsService;
        this.timeTableService = timeTableService;
        this.authenticationManager = authenticationManager;
        this.myUserDetailService = myUserDetailService;
        this.jwtTokenUtil = jwtTokenUtil;
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
            return ResponseEntity.ok(responseForm);
        }
        final UserDetails userDetails = myUserDetailService
                .loadUserByUsername(authenticateRequest.getCode());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
//        final String expAt = (jwtTokenUtil.extractExpiration(jwt).toInstant().atZone(ZoneId.of("Iran")).format(DateTimeFormatter.ofPattern("EEE MMM d yyyy hh:mm:ss")))+" GMT+0330 (Iran Standard Time)";
        final String expAt = (jwtTokenUtil.extractExpiration(jwt).toInstant().atZone(ZoneId.of("Iran")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        final User user = usersService.findByCode(jwtTokenUtil.extractUsername(jwt));
        ResponseForm responseForm = new ResponseForm("success", "null", new AuthenticateResponse(jwt, expAt, user));
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
    public ResponseForm addUser(@RequestBody User user) {
        return new ResponseForm("success", null, usersService.addUser(user));
    }

    @GetMapping("/api/users/{id}")
    public ResponseForm getUserById(@PathVariable int id) {
        try {
            User user = usersService.getById(id);
            return new ResponseForm("success", null, user);

        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id "+e.getMessage(), null);
        }
    }

    @PutMapping("/api/users/{id}")
    public ResponseForm updateUser(@PathVariable int id, @RequestBody User user) {
        try {
            User updatedUser = usersService.editUser(id, user);
            return new ResponseForm("success", null, updatedUser);
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id or body "+e.getMessage(), null);
        }
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseForm deleteUser(@PathVariable int id) {
        try {
            usersService.deleteUser(id);
            return new ResponseForm("success", null, null);
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id "+e.getMessage(), null);
        }

    }

    @GetMapping("api/users/profile")
    public ResponseForm getUserProfile(@RequestHeader String authorization){

        try {
            String userCode = jwtTokenUtil.extractUsername(authorization.substring(7));
            User user = usersService.findByCode(userCode);

            return new ResponseForm("success",null,new Profile(user.getId(),user.getFirstName(),user.getLastName(),user.getCode(),user.getRole()));
        }catch (Exception e){
            return new ResponseForm("failed","invalid token",null);
        }

    }

    @PostMapping("api/users/profile")
    public ResponseForm updateUserProfile(@RequestBody Profile.UserPass userPass, @RequestHeader String authorization){
        try {
            String userCode = jwtTokenUtil.extractUsername(authorization.substring(7));
            User user = usersService.findByCode(userCode);
            if (!userPass.getFirstName().equals("")){
                user.setFirstName(userPass.getFirstName());
            }
            if (!userPass.getLastName().equals("")){
                user.setLastName(userPass.getLastName());
            }
            updateUser(user.getId(),user);

            return new ResponseForm("success",null,new Profile(user.getId(),user.getFirstName(),user.getLastName(),user.getCode(),user.getRole()));
        }catch (Exception e){
            return new ResponseForm("failed","invalid token",null);
        }

    }

    @PostMapping("api/users/profile/changepassword")
    public ResponseForm changePassword(@RequestBody Profile.ChangePass newPassData, @RequestHeader String authorization){
        try {
            String userCode = jwtTokenUtil.extractUsername(authorization.substring(7));
            User user = usersService.findByCode(userCode);

            if (newPassData.getCurrentPassword().equals(user.getPassword())){
                user.setPassword(newPassData.getNewPassword());
            }else {
                throw new Exception("Current Password is Wrong!");
            }

            updateUser(user.getId(),user);

            return new ResponseForm("success",null,null);
        }catch (Exception e){
            return new ResponseForm("failed",e.getMessage(),null);
        }
    }

    @PostMapping("/api/users/addlist")
    public ResponseForm addUsersList(@RequestBody List<User> users){
        try {
            return new ResponseForm("success",null,usersService.addUsersList(users));
        }catch (Exception e){
            return new ResponseForm("failed","invalid input",null);
        }
    }


    // <-----------------   BELLS PART ---------------------->

    @GetMapping("/api/bells")
    public ResponseForm getBells(
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        return new ResponseForm("success", null, bellsService.getBellList(pageSize, page));
    }

    @PostMapping("/api/bells")
    public ResponseForm addBell(@RequestBody Bell bell) {
        return new ResponseForm("success", null, bellsService.addBell(bell));
    }

    @GetMapping("/api/bells/{id}")
    public ResponseForm getBellById(@PathVariable int id) {
        try {
            Bell bell = bellsService.getById(id);
            return new ResponseForm("success", null, bell);
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id "+e.getMessage(), null);
        }
    }

    @PutMapping("/api/bells/{id}")
    public ResponseForm updateBell(@PathVariable int id, @RequestBody Bell bell) {
        try {
            Bell updatedBell = bellsService.editBell(id, bell);
            return new ResponseForm("success", null, updatedBell);

        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id or body "+e.getMessage(), null);
        }
    }

    @DeleteMapping("/api/bells/{id}")
    public ResponseForm deleteBell(@PathVariable int id) {
        try {
            bellsService.deleteBell(id);
            return new ResponseForm("success", null, null);

        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id "+e.getMessage(), null);
        }

    }

    // <-----------------   DAYS PART ---------------------->

    @GetMapping("/api/days")
    public ResponseForm getDays(
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        return new ResponseForm("success", null, daysService.getDayList(pageSize, page));
    }

    @PostMapping("/api/days")
    public ResponseForm addDay(@RequestBody Day day) {
        return new ResponseForm("success", null, daysService.addDay(day));
    }

    @GetMapping("/api/days/{id}")
    public ResponseForm getDayById(@PathVariable int id) {
        try {
            Day day = daysService.getById(id);
            return new ResponseForm("success", null, day);

        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id "+e.getMessage(), null);
        }
    }

    @PutMapping("/api/days/{id}")
    public ResponseForm updateDay(@PathVariable int id, @RequestBody Day day) {
        try {
            Day updatedDay = daysService.editDay(id, day);
            return new ResponseForm("success", null, updatedDay);
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id or body "+e.getMessage(), null);
        }
    }

    @DeleteMapping("/api/days/{id}")
    public ResponseForm deleteDay(@PathVariable int id) {
        try {
            daysService.deleteDay(id);
            return new ResponseForm("success", null, null);
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id "+e.getMessage(), null);
        }

    }

    // <-----------------   COURSES PART ---------------------->
    @GetMapping("/api/courses")
    public ResponseForm getCourses(
            @RequestParam(value = "search", required = false, defaultValue = "") String courseName,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        return new ResponseForm("success", null, coursesService.getCourseList(courseName, pageSize, page));
    }

    @PostMapping("/api/courses")
    public ResponseForm addCourse(@RequestBody Course course) {
        return new ResponseForm("success", null, coursesService.addCourse(course));
    }

    @GetMapping("/api/courses/{id}")
    public ResponseForm getCourseById(@PathVariable int id) {
        try {
            Course course = coursesService.getById(id);
            return new ResponseForm("success", null, course);

        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id "+e.getMessage(), null);
        }
    }

    @PutMapping("/api/courses/{id}")
    public ResponseForm updateCourse(@PathVariable int id, @RequestBody Course course) {
        try {
            Course updatedCourse = coursesService.editCourse(id, course);
            return new ResponseForm("success", null, updatedCourse);
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id or body "+e.getMessage(), null);
        }
    }

    @DeleteMapping("/api/courses/{id}")
    public ResponseForm deleteCourse(@PathVariable int id) {
        try {
            coursesService.deleteCourse(id);
            return new ResponseForm("success", null, null);
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id "+e.getMessage(), null);
        }

    }

    @GetMapping("/api/courses/{id}/timetables")
    public ResponseForm getCourseTimeTable(
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @PathVariable int id) {
        try {
            CoursesService.CourseTimeTables timeTables = coursesService.getCourseTimeTables(id, pageSize, page);
            return new ResponseForm("success", null, timeTables);

        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id "+e.getMessage(), null);
        }
    }

    @GetMapping("/api/courses/{id}/masters")
    public ResponseForm getCourseMasters(
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @PathVariable int id) {
        try {
            CoursesService.Masters masters = coursesService.getCourseMasters(id, pageSize, page);
            return new ResponseForm("success", null, masters);

        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id "+e.getMessage(), null);
        }
    }

    @PostMapping("/api/courses/{id}/choose")
    public ResponseForm masterCourseChoose(@PathVariable int id, @RequestHeader String authorization) {
        try {
            //**************** MASTER DYNAMIC *****************
            String masterCode = jwtTokenUtil.extractUsername(authorization.substring(7));
            Master master = usersService.findByCode(masterCode).getMasterList().get(0);
            //**************** MASTER DYNAMIC *****************

            coursesService.masterCourseChoose(master, id);
            return new ResponseForm("success", null, null);
        } catch (Exception e) {
            return new ResponseForm("failed", "bad credential "+e.getMessage(), null);

        }

    }

    @GetMapping("/api/courses/todaycourses")
    public ResponseForm masterTodayClassList(@RequestHeader String authorization){
        try {
            String masterCode = jwtTokenUtil.extractUsername(authorization.substring(7));
            Master master = usersService.findByCode(masterCode).getMasterList().get(0);

            List<CoursesService.TodayClass> todayTimeTables = new LinkedList<>();
            String today =  new SimpleDateFormat("EEEE").format(new Date());

            for (TimeTable timeTable : master.getTimeTableList()){

                for (TimeTableBell timeTableBell : timeTable.getTimeTableBellList()){
                    if (timeTableBell.getDay().getLabel().equalsIgnoreCase(today)){
                        CoursesService.TodayClass todayClass = new CoursesService.TodayClass(
                                timeTable.getCourse().getTitle(),
                                timeTableBell.getDay().getLabel(),
                                timeTableBell.getBell().getLabel()
                                );
                        todayTimeTables.add(todayClass);
                    }
                }

            }
            return new ResponseForm("success", null, todayTimeTables);

        }catch (Exception e){
            return new ResponseForm("failed", "bad credential "+e.getMessage(), null);
        }

    }

    // <-----------------   ANNOUNCEMENT PART ---------------------->

    @GetMapping("/api/announcements")
    public ResponseForm getAnnouncements(
            @RequestParam(value = "masterId", required = false, defaultValue = "0") int masterId,
            @RequestParam(value = "timeTableId", required = false, defaultValue = "0") int timeTableId,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        return new ResponseForm("success", null, announcementsService.getAnnouncementList(masterId, timeTableId, pageSize, page));
    }

    @PostMapping("/api/announcements")
    public ResponseForm addAnnouncement(@RequestBody AnnouncementReference reference) {
        try {
            Announcement announcement = new Announcement(timeTableService.getById(reference.getTimeTable()), reference.getMessage());
            return new ResponseForm("success", null, announcementsService.addAnnouncement(announcement));
        } catch (Exception e) {
            return new ResponseForm("failed", "bad credential "+e.getMessage(), null);

        }

    }

    @GetMapping("/api/announcements/{id}")
    public ResponseForm getAnnouncementById(@PathVariable int id) {
        try {
            Announcement announcement = announcementsService.getById(id);
            return new ResponseForm("success", null, announcement);

        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id "+e.getMessage(), null);
        }
    }

    @PutMapping("/api/announcements/{id}")
    public ResponseForm updateAnnouncement(@PathVariable int id, @RequestBody AnnouncementReference reference) {
        try {
            Announcement announcement = new Announcement(timeTableService.getById(reference.getTimeTable()), reference.getMessage());
            return new ResponseForm("success", null, announcementsService.editAnnouncement(id, announcement));
        } catch (Exception e) {
            return new ResponseForm("failed", "bad credential "+e.getMessage(), null);

        }

    }

    @DeleteMapping("/api/announcements/{id}")
    public ResponseForm deleteAnnouncement(@PathVariable int id) {
        try {
            daysService.deleteDay(id);
            return new ResponseForm("success", null, null);
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id "+e.getMessage(), null);

        }

    }

    @GetMapping("/api/announcements/studentannouncements")
    public ResponseForm studentAnnouncements(@RequestHeader String authorization){

        try {
            String studentCode = jwtTokenUtil.extractUsername(authorization.substring(7));
            Student student = usersService.findByCode(studentCode).getStudentList().get(0);

            List<Announcement> announcements = new LinkedList<>();

            for (TimeTable timeTable : student.getTimeTableList()){
                announcements.addAll(timeTable.getAnnouncementList());
            }

            return new ResponseForm("success", null, announcements);

        }catch (Exception e){
            return new ResponseForm("failed", "bad credential "+e.getMessage(), null);
        }


    }

    // <-----------------   TIMETABLEBELL PART ---------------------->

    @GetMapping("/api/timetablebells")
    public ResponseForm getTimeTableBells(
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        return new ResponseForm("success", null, timeTableBellsService.getTimeTableBellList(pageSize, page));
    }

    @PostMapping("/api/timetablebells")
    public ResponseForm addTimeTableBell(@RequestBody TableBellsReference reference,
                                         @RequestHeader String authorization) {

        try {
            TimeTableBell timeTableBell = new TimeTableBell(bellsService.getById(reference.getBellId()), daysService.getById(reference.getDayId()));

            //**************** MASTER DYNAMIC *****************
            String masterCode = jwtTokenUtil.extractUsername(authorization.substring(7));
            Master master = usersService.findByCode(masterCode).getMasterList().get(0);
            //**************** MASTER DYNAMIC *****************

            timeTableBell.setMaster(master);

            return new ResponseForm("success", null, timeTableBellsService.addTimeTableBell(timeTableBell));
        } catch (Exception e) {
            return new ResponseForm("failed", "bad credential "+e.getMessage(), null);

        }

    }

    @GetMapping("/api/timetablebells/{id}")
    public ResponseForm getTimeTableBellById(@PathVariable int id) {
        try {
            return new ResponseForm("success", null, timeTableBellsService.getById(id));

        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id "+e.getMessage(), null);
        }
    }

    @PutMapping("/api/timetablebells/{id}")
    public ResponseForm updateTimeTableBell(@PathVariable int id, @RequestBody TableBellsReference reference) {
        try {
            TimeTableBell timeTableBell = new TimeTableBell(bellsService.getById(reference.getBellId()), daysService.getById(reference.getDayId()));
            return new ResponseForm("success", null, timeTableBellsService.editTimeTableBell(id, timeTableBell));
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id or body "+e.getMessage(), null);
        }

    }

    @DeleteMapping("/api/timetablebells/{id}")
    public ResponseForm deleteTimeTableBell(@PathVariable int id) {
        try {
            timeTableBellsService.deleteTimeTableBell(id);
            return new ResponseForm("success", null, null);

        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id "+e.getMessage(), null);
        }

    }

    // <-----------------   TIMETABLE PART ---------------------->

    @GetMapping("/api/timetables")
    public ResponseForm getTimeTables(
            @RequestParam(value = "studentId", required = false, defaultValue = "0") int studentId,
            @RequestParam(value = "courseId", required = false, defaultValue = "0") int courseId,
            @RequestParam(value = "masterId", required = false, defaultValue = "0") int masterId,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        return new ResponseForm("success", null, timeTableService.getTimeTableList(studentId, courseId, masterId, pageSize, page));
    }

    @GetMapping("/api/timetables/{id}")
    public ResponseForm getTimeTableById(@PathVariable int id) {
        try {

            return new ResponseForm("success", null, timeTableService.getById(id));
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id "+e.getMessage(), null);

        }
    }

    @PostMapping("/api/timetables/{id}/choose")
    public ResponseForm studentTimeTableChoose(@PathVariable int id, @RequestHeader String authorization) {
        try {
            //**************** STUDENT DYNAMIC *****************
            String studentCode = jwtTokenUtil.extractUsername(authorization.substring(7));
            Student student = usersService.findByCode(studentCode).getStudentList().get(0);
            //**************** STUDENT DYNAMIC *****************

            timeTableService.studentCourseChoose(student, id);
            return new ResponseForm("success", null, null);
        } catch (Exception e) {
            return new ResponseForm("failed", "bad credential "+e.getMessage(), null);
        }

    }

    @PostMapping("/api/timetables/startprocess")
    public ResponseForm startProcess(
            @RequestParam(value = "maxClassPerBell", required = false, defaultValue = "10") int maxClassPerBell) {
        try {
            timeTableService.startProcess(maxClassPerBell);
            return new ResponseForm("success", null, null);

        } catch (Exception e) {
            return new ResponseForm("failed", e.getMessage(), null);
        }

    }


}















