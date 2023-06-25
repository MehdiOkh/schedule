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


    public List<StudentUnit> getStudentUnits(int studentId) {
        Student currentStudent = studentRepo.getById(studentId);
        return currentStudent.getStudentUnits();
    }

    public Students getTimeTableStudents(int timeTableId, int pageSize, int page) {
        return new Students(timeTableId, pageSize, page);
    }

    public class Students {
        private List<Student> list = null;

        private int pageSize;
        private int page;
        private int totalPage;

        public Students(int timeTableId, int pageSize, int page) {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(timeTableId);
        }

        private List<Student> listMaker(int timeTableId) {

            List<Student> list = new ArrayList<>();
            List<Student> temp = new ArrayList<>();

            int begin = (page - 1) * pageSize == 0 ? 0 : (page - 1) * pageSize;
            int end = page * pageSize;
            TimeTable t = timeTableRepo.getById(timeTableId);
            for (StudentUnit studentUnit : timeTableRepo.getById(timeTableId).getStudentUnits()) {
                list.add(studentUnit.getStudent());

            }

            this.totalPage = (int) Math.ceil((double) list.size() / (double) pageSize);

            while (begin < end && list.size() > begin) {
                temp.add(list.get(begin++));
            }

            return temp;
        }

        public List<Student> getList() {
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
