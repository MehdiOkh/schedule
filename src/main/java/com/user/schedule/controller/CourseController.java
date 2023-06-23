package com.user.schedule.controller;

import com.user.schedule.database.model.*;
import com.user.schedule.database.service.*;
import com.user.schedule.security.service.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class CourseController {
    private final CoursesService coursesService;

    private final JwtUtil jwtTokenUtil;

    private final AnnouncementsService announcementsService;

    private final UsersService usersService;

    private final TimeTableService timeTableService;

    private final DaysService daysService;

    private final MajorService majorService;


    @Autowired
    public CourseController(CoursesService coursesService, JwtUtil jwtTokenUtil, AnnouncementsService announcementsService,
                            UsersService usersService, TimeTableService timeTableService, DaysService daysService, MajorService majorService) {
        this.coursesService = coursesService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.announcementsService = announcementsService;
        this.usersService = usersService;
        this.timeTableService = timeTableService;
        this.daysService = daysService;
        this.majorService = majorService;
    }

    // <-----------------   COURSES PART ---------------------->
    @GetMapping("/api/courses")
    public ResponseForm getCourses(
            @RequestParam(value = "search", required = false, defaultValue = "") String courseName,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        return new ResponseForm("success", null, coursesService.getCourseList(courseName, pageSize, page));
    }

    @Transactional
    @PostMapping("/api/courses")
    public ResponseForm addCourse(@RequestBody Course course) throws Exception {
        return new ResponseForm("success", null, coursesService.addCourse(course));

    }


    @GetMapping("/api/courses/{id}")
    public ResponseForm getCourseById(@PathVariable int id) throws Exception {
            Course course = coursesService.getById(id);
            return new ResponseForm("success", null, course);
    }

    @PutMapping("/api/courses/{id}")
    public ResponseForm updateCourse(@PathVariable int id, @RequestBody Course course) throws Exception {
            Course updatedCourse = coursesService.editCourse(id, course);
            return new ResponseForm("success", null, updatedCourse);
    }

    @DeleteMapping("/api/courses/{id}")
    public ResponseForm deleteCourse(@PathVariable int id) throws Exception {
        coursesService.deleteCourse(id);
        return new ResponseForm("success", null, null);

    }

    @GetMapping("/api/courses/{id}/timetables")
    public ResponseForm getCourseTimeTable(
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @PathVariable int id) throws Exception {
            CoursesService.CourseTimeTables timeTables = coursesService.getCourseTimeTables(id, pageSize, page);
            return new ResponseForm("success", null, timeTables);
    }

    @GetMapping("/api/courses/{id}/masters")
    public ResponseForm getCourseMasters(
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @PathVariable int id) throws Exception {
        return new ResponseForm("success", null, coursesService.getCourseMasters(id, pageSize, page));
    }

    @PostMapping("/api/courses/{id}/choose")
    public ResponseForm masterCourseChoose(@PathVariable int id, @RequestHeader String authorization) throws Exception {
        //**************** MASTER DYNAMIC *****************
        String masterCode = jwtTokenUtil.extractUsername(authorization);

        if (authorization.startsWith("Bearer ")) {
            masterCode = jwtTokenUtil.extractUsername(authorization.substring(7));
        }

        Master master = usersService.findByCode(masterCode).getMasterList().get(0);
        //**************** MASTER DYNAMIC *****************

        coursesService.masterCourseChoose(master, id);
        return new ResponseForm("success", null, null);


    }

    @GetMapping("/api/courses/today-courses")
    public ResponseForm masterTodayClassList(@RequestHeader String authorization) {
        try {
            String masterCode = jwtTokenUtil.extractUsername(authorization);

            if (authorization.startsWith("Bearer ")) {
                masterCode = jwtTokenUtil.extractUsername(authorization.substring(7));
            }
            Master master = usersService.findByCode(masterCode).getMasterList().get(0);

            List<CoursesService.TodayClass> todayTimeTables = new LinkedList<>();
            String today = new SimpleDateFormat("EEEE").format(new Date());

            for (TimeTable timeTable : master.getTimeTableList()) {

                for (TimeTableBell timeTableBell : timeTable.getTimeTableBellList()) {
                    if (timeTableBell.getDay().getLabel().equalsIgnoreCase(today)) {
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

        } catch (Exception e) {
            return new ResponseForm("failed", "bad credential " + e.getMessage(), null);
        }

    }


    // <-----------------   MAJORS PART ---------------------->
    @PostMapping("/api/majors")
    public ResponseForm addMajor(@RequestBody Major major) {
        return new ResponseForm("success", null, majorService.addMajor(major));
    }

    @GetMapping("/api/majors")
    public ResponseForm getMajorList(@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        return new ResponseForm("success", null, majorService.getMajorList(pageSize, page));
    }

    @GetMapping("/api/majors/{id}")
    public ResponseForm getMajorByName(@PathVariable int id) throws Exception {
        return new ResponseForm("success", null, majorService.getById(id));
    }


    @PutMapping("/api/majors/{id}")
    public ResponseForm editMajor(@PathVariable int id, @RequestBody Major major) throws Exception {
//        Major currentMajor = majorService.getById(id);
        return new ResponseForm("success", null, majorService.editMajor(id, major));
    }

    @DeleteMapping("/api/majors/{id}")
    public ResponseForm deleteMajor(@PathVariable int id) throws Exception {
//        Major currentMajor = majorService.getById(id);
        majorService.deleteById(id);
        return new ResponseForm("success", null, null);
    }

    // <-----------------   COURSES PREREQUISITE PART ---------------------->
    @GetMapping("/api/courses/{name}/prerequisite")
    public ResponseForm getCourses(@PathVariable String name) throws Exception {

        return new ResponseForm("success", null, coursesService.getCoursePrerequisites(name));
    }

    @Transactional
    @PostMapping("/api/courses/{name}/prerequisite")
    public ResponseForm addPrerequisite(@PathVariable String name, @RequestBody Map<String, String[]> prerequisiteList) throws Exception {
        return new ResponseForm("success", null, coursesService.addPrerequisite(name, prerequisiteList.get("prerequisiteList")));
    }

    @Transactional
    @DeleteMapping("/api/courses/{name}/prerequisite")
    public ResponseForm deletePrerequisite(@PathVariable String name, @RequestBody Map<String, String[]> prerequisiteList) throws Exception {
        coursesService.deletePrerequisite(name, prerequisiteList.get("prerequisiteList"));
        return new ResponseForm("success", null, null);
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
    public ResponseForm addAnnouncement(@RequestBody AnnouncementReference reference) throws Exception {
        Announcement announcement = new Announcement(timeTableService.getById(reference.getTimeTable()), reference.getMessage());
        return new ResponseForm("success", null, announcementsService.addAnnouncement(announcement));


    }

    @GetMapping("/api/announcements/{id}")
    public ResponseForm getAnnouncementById(@PathVariable int id) throws Exception {
        Announcement announcement = announcementsService.getById(id);
        return new ResponseForm("success", null, announcement);

    }

    @PutMapping("/api/announcements/{id}")
    public ResponseForm updateAnnouncement(@PathVariable int id, @RequestBody AnnouncementReference reference) throws Exception {
        Announcement announcement = new Announcement(timeTableService.getById(reference.getTimeTable()), reference.getMessage());
        return new ResponseForm("success", null, announcementsService.editAnnouncement(id, announcement));
    }

    @DeleteMapping("/api/announcements/{id}")
    public ResponseForm deleteAnnouncement(@PathVariable int id) throws Exception {
        announcementsService.deleteAnnouncement(id);
        return new ResponseForm("success", null, null);

    }

    @GetMapping("/api/announcements/student-announcements")
    public ResponseForm studentAnnouncements(@RequestHeader String authorization) {
        String studentCode;
        if (authorization.startsWith("Bearer ")) {
            studentCode = jwtTokenUtil.extractUsername(authorization.substring(7));
        } else {
            studentCode = jwtTokenUtil.extractUsername(authorization);

        }
        Student student = usersService.findByCode(studentCode).getStudentList().get(0);

        List<Announcement> announcements = new LinkedList<>();

        for (StudentUnit unit : student.getStudentUnits()) {
            announcements.addAll(unit.getTimeTable().getAnnouncementList());
        }

        return new ResponseForm("success", null, announcements);

    }

    @GetMapping("/api/announcements/master-announcements")
    public ResponseForm masterAnnouncements(@RequestHeader String authorization) {
        String masterCode;
        if (authorization.startsWith("Bearer ")) {
            masterCode = jwtTokenUtil.extractUsername(authorization.substring(7));
        } else {
            masterCode = jwtTokenUtil.extractUsername(authorization);

        }
        Master master = usersService.findByCode(masterCode).getMasterList().get(0);

        List<Announcement> announcements = new LinkedList<>();

        for (TimeTable timeTable : master.getTimeTableList()) {
            announcements.addAll(timeTable.getAnnouncementList());
        }

        return new ResponseForm("success", null, announcements);

    }

}
