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

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


@CrossOrigin(origins = "*")
@RestController
public class Controller {

    private final UsersService usersService;

    private final BellsService bellsService;

    private final DaysService daysService;


    private final TimeTableBellsService timeTableBellsService;

    private final TimeTableService timeTableService;

    private final MajorService majorService;


    private final AuthenticationManager authenticationManager;

    private final MyUserDetailService myUserDetailService;

    private final JwtUtil jwtTokenUtil;

    @Autowired
    public Controller(UsersService usersService, BellsService bellsService, DaysService daysService,
                      TimeTableBellsService timeTableBellsService, TimeTableService timeTableService,
                      MajorService majorService, AuthenticationManager authenticationManager, MyUserDetailService myUserDetailService, JwtUtil jwtTokenUtil) {
        this.usersService = usersService;
        this.bellsService = bellsService;
        this.daysService = daysService;

        this.timeTableBellsService = timeTableBellsService;
        this.timeTableService = timeTableService;
        this.majorService = majorService;
        this.authenticationManager = authenticationManager;
        this.myUserDetailService = myUserDetailService;
        this.jwtTokenUtil = jwtTokenUtil;
    }






}















