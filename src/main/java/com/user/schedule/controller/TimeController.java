package com.user.schedule.controller;

import com.user.schedule.database.model.*;
import com.user.schedule.database.seeder.BellSeeder;
import com.user.schedule.database.seeder.DaySeeder;
import com.user.schedule.database.service.*;
import com.user.schedule.exceptions.UnitPickException;
import com.user.schedule.security.service.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin(origins = "*")
@RestController
public class TimeController {

    private final UsersService usersService;

    private final BellsService bellsService;

    private final DaysService daysService;


    private final TimeTableBellsService timeTableBellsService;

    private final TimeTableService timeTableService;


    private final JwtUtil jwtTokenUtil;

    private final UnitPickTimeService unitPickTimeService;

    private final MasterService masterService;

    private final CoursesService coursesService;

    private final StudentService studentService;

    private final DaySeeder daySeeder;

    private final BellSeeder bellSeeder;


    public TimeController(UsersService usersService, BellsService bellsService, DaysService daysService,
                          TimeTableBellsService timeTableBellsService, TimeTableService timeTableService,
                          JwtUtil jwtTokenUtil, UnitPickTimeService unitPickTimeService, MasterService masterService, CoursesService coursesService, StudentService studentService, DaySeeder daySeeder, BellSeeder bellSeeder) {
        this.usersService = usersService;
        this.bellsService = bellsService;
        this.daysService = daysService;
        this.timeTableBellsService = timeTableBellsService;
        this.timeTableService = timeTableService;
        this.unitPickTimeService = unitPickTimeService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.masterService = masterService;
        this.coursesService = coursesService;
        this.studentService = studentService;
        this.daySeeder = daySeeder;
        this.bellSeeder = bellSeeder;
    }


    // <-----------------   UNIT-PICK-TIME PART ---------------------->
    @PostMapping("/api/unit-pick-time")
    public ResponseForm addUnitPickTime(@RequestBody UnitPickTime unitPickTime) {
        return new ResponseForm("success", null, unitPickTimeService.addUnitPickTime(unitPickTime));
    }

    @GetMapping("/api/unit-pick-time/{id}")
    public ResponseForm getUnitPickTime(@PathVariable int id) throws Exception {
        return new ResponseForm("success", null, unitPickTimeService.getById(id));
    }

    @GetMapping("/api/unit-pick-time/")
    public ResponseForm getUnitPickTimeList(
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) throws Exception {
        return new ResponseForm("success", null, unitPickTimeService.getUnitPickTimeList(pageSize, page));
    }

    @PutMapping("/api/unit-pick-time/{id}")
    public ResponseForm updateUnitPickTime(@RequestBody UnitPickTime unitPickTime,
                                           @PathVariable int id) throws Exception {
        return new ResponseForm("success", null, unitPickTimeService.editUnitPickTime(id, unitPickTime));
    }

    @DeleteMapping("/api/unit-pick-time/{id}")
    public ResponseForm updateUnitPickTime(@PathVariable int id) throws Exception {
        unitPickTimeService.deleteById(id);
        return new ResponseForm("success", null, null);
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
            return new ResponseForm("failed", "invalid id " + e.getMessage(), null);
        }
    }

    @PutMapping("/api/bells/{id}")
    public ResponseForm updateBell(@PathVariable int id, @RequestBody Bell bell) {
        try {
            Bell updatedBell = bellsService.editBell(id, bell);
            return new ResponseForm("success", null, updatedBell);

        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id or body " + e.getMessage(), null);
        }
    }

    @DeleteMapping("/api/bells/{id}")
    public ResponseForm deleteBell(@PathVariable int id) {
        try {
            bellsService.deleteBell(id);
            return new ResponseForm("success", null, null);

        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id " + e.getMessage(), null);
        }

    }

    @PostMapping("/api/bells/seed")
    public ResponseForm seedBells() {
        bellSeeder.populateBellsOfDay();
        return new ResponseForm("success", null, null);
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
            return new ResponseForm("failed", "invalid id " + e.getMessage(), null);
        }
    }

    @PutMapping("/api/days/{id}")
    public ResponseForm updateDay(@PathVariable int id, @RequestBody Day day) {
        try {
            Day updatedDay = daysService.editDay(id, day);
            return new ResponseForm("success", null, updatedDay);
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id or body " + e.getMessage(), null);
        }
    }

    @DeleteMapping("/api/days/{id}")
    public ResponseForm deleteDay(@PathVariable int id) {
        try {
            daysService.deleteDay(id);
            return new ResponseForm("success", null, null);
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id " + e.getMessage(), null);
        }

    }

    @PostMapping("/api/days/seed")
    public ResponseForm seedDays() {
        daySeeder.populateWeekDays();
        return new ResponseForm("success", null, null);
    }

    // <-----------------   TIMETABLEBELL PART ---------------------->

    @GetMapping("/api/time-table-bells")
    public ResponseForm getTimeTableBells(
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        return new ResponseForm("success", null, timeTableBellsService.getTimeTableBellList(pageSize, page));
    }

    @PostMapping("/api/time-table-bells")
    public ResponseForm addTimeTableBell(@RequestBody TableBellsReference reference) throws Exception {

        TimeTableBell timeTableBell = new TimeTableBell(bellsService.getById(reference.getBellId()), daysService.getById(reference.getDayId()));
        timeTableBell.setWeekType(reference.getWeekType());
        timeTableBell.setRoomNumber(reference.getRoomNumber());
        return new ResponseForm("success", null, timeTableBellsService.addTimeTableBell(timeTableBell));

    }

    @GetMapping("/api/time-table-bells/{id}")
    public ResponseForm getTimeTableBellById(@PathVariable int id) {
        try {
            return new ResponseForm("success", null, timeTableBellsService.getById(id));

        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id " + e.getMessage(), null);
        }
    }

    @PutMapping("/api/time-table-bells/{id}")
    public ResponseForm updateTimeTableBell(@PathVariable int id, @RequestBody TableBellsReference reference) throws Exception {
        TimeTableBell timeTableBell = new TimeTableBell(bellsService.getById(reference.getBellId()), daysService.getById(reference.getDayId()));
        timeTableBell.setWeekType(reference.getWeekType());
        timeTableBell.setRoomNumber(reference.getRoomNumber());
        return new ResponseForm("success", null, timeTableBellsService.editTimeTableBell(id, timeTableBell));

    }

    @DeleteMapping("/api/time-table-bells/{id}")
    public ResponseForm deleteTimeTableBell(@PathVariable int id) {
        try {
            timeTableBellsService.deleteTimeTableBell(id);
            return new ResponseForm("success", null, null);

        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id " + e.getMessage(), null);
        }

    }

    // <-----------------   TIMETABLE PART ---------------------->

    @PostMapping("/api/time-tables")
    public ResponseForm addTimeTables(@RequestBody TimeTableReq timeTableReq) throws Exception {
        Master master = masterService.getMasterById(timeTableReq.getMasterId());
        Course course = coursesService.getById(timeTableReq.getCourseId());
        TimeTable timeTable = new TimeTable(master, course);
        timeTable.setTerm(timeTableReq.getTerm());
        for (int id : timeTableReq.timeTableBellsId) {
            TimeTableBell timeTableBell = timeTableBellsService.getById(id);
            timeTable.getTimeTableBellList().add(timeTableBell);
            timeTableBell.setTimeTable(timeTable);
        }

        return new ResponseForm("success", null, timeTableService.addTimeTable(timeTable));
    }

    @GetMapping("/api/time-tables")
    public ResponseForm getTimeTables(
            @RequestParam(value = "studentId", required = false, defaultValue = "0") int studentId,
            @RequestParam(value = "courseId", required = false, defaultValue = "0") int courseId,
            @RequestParam(value = "masterId", required = false, defaultValue = "0") int masterId,
            @RequestParam(value = "term", required = false, defaultValue = "") String term,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        return new ResponseForm("success", null, timeTableService.getTimeTableList(studentId, courseId, masterId, term, pageSize, page));
    }

    @GetMapping("/api/time-tables/{id}")
    public ResponseForm getTimeTableById(@PathVariable int id) {
        try {

            return new ResponseForm("success", null, timeTableService.getById(id));
        } catch (Exception e) {
            return new ResponseForm("failed", "invalid id " + e.getMessage(), null);

        }
    }

    @PostMapping("/api/time-tables/{id}/choose")
    public ResponseForm studentTimeTableChoose(@PathVariable int id, @RequestHeader String authorization) throws Exception {
        //**************** STUDENT DYNAMIC *****************
        String studentCode;

        if (authorization.startsWith("Bearer ")) {
            studentCode = jwtTokenUtil.extractUsername(authorization.substring(7));
        } else {
            studentCode = jwtTokenUtil.extractUsername(authorization);
        }
        Student student = usersService.findByCode(studentCode).getStudentList().get(0);
        if (student == null) throw new UnitPickException.StudentNotFound("Illegal access");
        //**************** STUDENT DYNAMIC *****************

        timeTableService.studentCourseChoose(student, id);
        return new ResponseForm("success", null, null);

    }

    @DeleteMapping("/api/time-tables/{id}/remove")
    public ResponseForm studentTimeTableRemove(@PathVariable int id, @RequestHeader String authorization) throws Exception {
        //**************** STUDENT DYNAMIC *****************
        String studentCode;

        if (authorization.startsWith("Bearer ")) {
            studentCode = jwtTokenUtil.extractUsername(authorization.substring(7));
        } else {
            studentCode = jwtTokenUtil.extractUsername(authorization);
        }
        Student student = usersService.findByCode(studentCode).getStudentList().get(0);
        if (student == null) throw new UnitPickException.StudentNotFound("Illegal access");
        //**************** STUDENT DYNAMIC *****************

        timeTableService.studentCourseRemove(student, id);
        return new ResponseForm("success", null, null);

    }

    @GetMapping("/api/time-tables/student-units")
    public ResponseForm studentUnits(@RequestHeader String authorization, @RequestParam(value = "term", required = false, defaultValue = "") String term
    ) {

        String studentCode;

        if (authorization.startsWith("Bearer ")) {
            studentCode = jwtTokenUtil.extractUsername(authorization.substring(7));
        } else {
            studentCode = jwtTokenUtil.extractUsername(authorization);
        }
        Student student = usersService.findByCode(studentCode).getStudentList().get(0);

        return new ResponseForm("success", null, studentService.getStudentUnits(student.getId(), term));


    }

    @GetMapping("/api/time-tables/master")
    public ResponseForm masterTimeTable(@RequestHeader String authorization,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                        @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        String masterCode;

        if (authorization.startsWith("Bearer ")) {
            masterCode = jwtTokenUtil.extractUsername(authorization.substring(7));
        } else {
            masterCode = jwtTokenUtil.extractUsername(authorization);
        }
        Master master = usersService.findByCode(masterCode).getMasterList().get(0);

        return new ResponseForm("success", null, timeTableService.getTimeTableList(0, 0, master.getId(), "", pageSize, page));

    }

    @GetMapping("/api/time-tables/{timeTableId}/students")
    public ResponseForm timeTableStudents(
            @PathVariable int timeTableId,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) throws Exception {

        timeTableService.getById(timeTableId);
        return new ResponseForm("success", null, studentService.getTimeTableStudents(timeTableId, pageSize, page));

    }

    @PostMapping("/api/time-tables/{timeTableId}/students-grade")
    public ResponseForm submitStudentsGrade(
            @PathVariable int timeTableId, @RequestBody() StudentService.ReportGrade reportGrade) throws Exception {

        timeTableService.getById(timeTableId);
        studentService.submitStudentsGrade(timeTableId, reportGrade.getReportGrade());
        return new ResponseForm("success", null, null);
    }

    @PutMapping("/api/time-tables/{timeTableId}/accept")
    public ResponseForm acceptTimeTable(
            @PathVariable int timeTableId) {

        timeTableService.acceptTimeTable(timeTableId);

        return new ResponseForm("success", null, null);
    }

//    @PostMapping("/api/time-tables/StartProcess")
//    public ResponseForm startProcess(
//            @RequestParam(value = "maxClassPerBell", required = false, defaultValue = "10") int maxClassPerBell) {
//        try {
//            timeTableService.startProcess(maxClassPerBell);
//            return new ResponseForm("success", null, null);
//
//        } catch (Exception e) {
//            return new ResponseForm("failed", e.getMessage(), null);
//        }
//
//    }

    private static class TimeTableReq {
        private int courseId;
        private int masterId;

        private String term;
        private ArrayList<Integer> timeTableBellsId;

        public TimeTableReq(int courseId, int masterId, String term, ArrayList<Integer> timeTableBellsId) {
            this.courseId = courseId;
            this.masterId = masterId;
            this.timeTableBellsId = timeTableBellsId;
            this.term = term;
        }

        public int getCourseId() {
            return courseId;
        }

        public int getMasterId() {
            return masterId;
        }

        public ArrayList<Integer> getTimeTableBellsId() {
            return timeTableBellsId;
        }

        public String getTerm() {
            return term;
        }
    }

}
