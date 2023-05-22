package com.user.schedule.database.service;

import com.user.schedule.database.model.*;
import com.user.schedule.database.repository.CourseRepo;
import com.user.schedule.database.repository.MasterRepo;
import com.user.schedule.database.repository.TimeTableBellRepo;
import com.user.schedule.database.repository.TimeTableRepo;
import com.user.schedule.exceptions.UnitPickException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TimeTableService {

    @Autowired
    private TimeTableRepo timeTableRepo;
    @Autowired
    private TimeTableBellRepo timeTableBellRepo;
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private MasterRepo masterRepo;

    public TimeTables getTimeTableList(int studentId, int courseId, int masterId, int pageSize, int page) {
        return new TimeTables(studentId, courseId, masterId, pageSize, page);
    }

    public TimeTable addTimeTable(TimeTable timeTable) {
        return timeTableRepo.save(timeTable);
    }

    public TimeTable getById(int id) throws Exception {
        TimeTable timeTableBell;

        if (timeTableRepo.findById(id).isPresent()) {
            timeTableBell = timeTableRepo.findById(id).get();
        } else {
            throw new UnitPickException.UnitNotFound("Your picked unit not found");
        }
        return timeTableBell;
    }

    public void studentCourseChoose(Student student, int tableId) throws Exception {
        TimeTable timeTable = getById(tableId);
        for (StudentUnit unit : student.getStudentUnits()) {
            if (unit.getTimeTable().getId() == timeTable.getId()) {
                throw new UnitPickException.UnitNotFound("Student already has this unit.");
            }
        }
        StudentUnit studentUnit = new StudentUnit(student);
        studentUnit.setGrade(0.25F);
        studentUnit.setTerm(1);
        studentUnit.setTimeTable(timeTable);
        timeTable.getStudentUnits().add(studentUnit);
//        master.getCourseList().add(course);
        timeTableRepo.flush();
    }

    public void startProcess(int maxClassPerBell) throws Exception {

        //TODO:################ ALGORITHM GOES HERE ##################


        // make sure that there is enough classroom for starting process
        for (TimeTableBell timeTableBell1 : timeTableBellRepo.findAll()) {
            int maxClassMount = maxClassPerBell;
            for (TimeTableBell timeTableBell2 : timeTableBellRepo.findAll()) {
//                if (timeTableBell1!=timeTableBell2){
                if (timeTableBell1.getDay().getLabel().equals(timeTableBell2.getDay().getLabel()) && timeTableBell1.getBell().getLabel().equals(timeTableBell2.getBell().getLabel())) {
                    maxClassMount -= 1;
                }
//                }
                if (maxClassMount < 0) {
                    throw new Exception("Master :" +
//                            timeTableBell2.getMaster().getUser().getLastName()+
                            " should change his/her timeTableBells. There is no available class on Day: " +
                            timeTableBell2.getDay() + " and Bell : " + timeTableBell2.getBell());
                }
            }
        }

        // make sure that there is enough timeTableBells for chosen units
        for (Master master : masterRepo.findAll()) {
            int masterUnitsCount = 0;
            for (Course course : master.getCourseList()) {
                if (course.getUnitsCount() % 2 == 1) {
                    masterUnitsCount += course.getUnitsCount() + 1;
                } else {
                    masterUnitsCount += course.getUnitsCount();
                }
            }
//            if (masterUnitsCount/2 > master.getTimeTableBellList().size()){
//                throw new Exception("Master : " + master.getUser().getLastName() + " should add more timeTableBells.");
//            }
        }


        for (Course course : courseRepo.findAll()) {
            for (Master master : course.getMasterList()) {
                TimeTable timeTable = new TimeTable();
                for (int sessionsMount = (int) Math.ceil((double) course.getUnitsCount() / 2.0);
                     sessionsMount > 0; sessionsMount--) {
//                    for (TimeTableBell timeTableBell : master.getTimeTableBellList()){
//                        if (timeTableBell.getTimeTable() == null){
//                            timeTable.setMaster(master);
//                            timeTable.setCourse(course);
//                            timeTable.getTimeTableBellList().add(timeTableBell);
//                            timeTableBell.setTimeTable(timeTable);
//                            timeTableBellRepo.flush();
//                            break;
//
//                        }
//                    }
                }
            }
        }

        // Add TimeTable
//        TimeTable timeTable = new TimeTable();
//        timeTable.setMaster(timeTableBellRepo.findById(14).get().getMaster());
//        timeTable.setCourse(timeTableBellRepo.findById(14).get().getMaster().getCourseList().get(1));
//        timeTable.getTimeTableBellList().add(timeTableBellRepo.findById(13).get());
//        timeTable.getTimeTableBellList().add(timeTableBellRepo.findById(14).get());
//        timeTableBellRepo.findById(13).get().setTimeTable(timeTable);
//        timeTableBellRepo.findById(14).get().setTimeTable(timeTable);
//        timeTableBellRepo.flush();
//        timeTableRepo.save(timeTable);

    }

    public class TimeTables {
        private List<TimeTable> list = null;

        private int pageSize;
        private int page;
        private int totalPage;


        public TimeTables(int studentId, int courseId, int masterId, int pageSize, int page) {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(studentId, courseId, masterId, pageSize, page);

        }

        public List<TimeTable> listMaker(int studentId, int courseId, int masterId, int pageSize, int page) {
            List<TimeTable> list = new ArrayList<>();
            List<TimeTable> temp = new ArrayList<>();
            if (studentId != 0) {
                for (TimeTable timeTable : timeTableRepo.findAll()) {
                    for (StudentUnit unit : timeTable.getStudentUnits()) {
                        if (unit.getStudent().getId() == studentId) {
                            list.add(timeTable);
                        }
                    }
                }
            } else if (courseId != 0) {
                for (TimeTable timeTable : timeTableRepo.findAll()) {
                    if (timeTable.getCourse().getId() == courseId) {
                        list.add(timeTable);
                    }
                }
            } else if (masterId != 0) {
                for (TimeTable timeTable : timeTableRepo.findAll()) {
                    if (timeTable.getMaster().getId() == masterId) {
                        list.add(timeTable);
                    }
                }
            } else {
                list = timeTableRepo.findAll();
            }


            int begin = (page - 1) * pageSize == 0 ? 0 : (page - 1) * pageSize;
            int end = page * pageSize;

            this.totalPage = (int) Math.ceil((double) list.size() / (double) pageSize);

            while (begin < end && list.size() > begin) {
                temp.add(list.get(begin++));
            }

            return temp;
        }


        public List<TimeTable> getList() {
            return list;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getPage() {
            return page;
        }

        public int getTotalPage() {
            return totalPage;
        }

    }
}
