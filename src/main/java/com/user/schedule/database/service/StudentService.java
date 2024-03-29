package com.user.schedule.database.service;

import com.user.schedule.database.model.Student;
import com.user.schedule.database.model.StudentUnit;
import com.user.schedule.database.model.TimeTable;
import com.user.schedule.database.repository.StudentRepo;
import com.user.schedule.database.repository.TimeTableRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private TimeTableRepo timeTableRepo;


    public List<StudentUnit> getStudentUnits(int studentId, String term) {
        List<StudentUnit> temp = new ArrayList<>();
        Student currentStudent = studentRepo.getById(studentId);
        if (!term.isEmpty()) {
            for (StudentUnit unit : currentStudent.getStudentUnits()
            ) {
                if (unit.getTimeTable().getTerm().split("_")[0].equals(term.split("_")[0])
                        && unit.getTimeTable().getTerm().split("_")[1].equals(term.split("_")[1])) {

                }
                temp.add(unit);
            }
            return temp;
        } else {
            return currentStudent.getStudentUnits();
        }
    }

    public Students getTimeTableStudents(int timeTableId, int pageSize, int page) {
        return new Students(timeTableId, pageSize, page);
    }

    public void submitStudentsGrade(int timeTableId, List<ReportGrade.StudentGrade> studentsGrade) {
        for (StudentUnit studentUnit : timeTableRepo.getById(timeTableId).getStudentUnits()
        ) {
            for (ReportGrade.StudentGrade studentGrade : studentsGrade
            ) {
                if (studentGrade.getId() == studentUnit.getStudent().getId()) {
                    studentUnit.setGrade(Float.parseFloat(studentGrade.getGrade()));
                }
            }

        }
        studentRepo.flush();
    }

    public static class ReportGrade {
        private List<StudentGrade> reportGrade;

        public ReportGrade(List<StudentGrade> reportGrade) {
            this.reportGrade = reportGrade;
        }

        public ReportGrade() {
        }

        public List<StudentGrade> getReportGrade() {
            return reportGrade;
        }

        public void setReportGrade(List<StudentGrade> reportGrade) {
            this.reportGrade = reportGrade;
        }

        public static class StudentGrade {
            private int id;

            private String grade;

            public StudentGrade(int id, String grade) {
                this.id = id;
                this.grade = grade;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }
        }

    }

    public class Students {
        private List<StudentUnit> list = null;

        private int pageSize;
        private int page;
        private int totalPage;

        public Students(int timeTableId, int pageSize, int page) {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(timeTableId);
        }

        private List<StudentUnit> listMaker(int timeTableId) {

            List<StudentUnit> list = new ArrayList<>();
            List<StudentUnit> temp = new ArrayList<>();

            int begin = (page - 1) * pageSize == 0 ? 0 : (page - 1) * pageSize;
            int end = page * pageSize;
            TimeTable t = timeTableRepo.getById(timeTableId);
            for (StudentUnit studentUnit : timeTableRepo.getById(timeTableId).getStudentUnits()) {
                list.add(studentUnit);

            }

            this.totalPage = (int) Math.ceil((double) list.size() / (double) pageSize);

            while (begin < end && list.size() > begin) {
                temp.add(list.get(begin++));
            }

            return temp;
        }

        public List<StudentUnit> getList() {
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
