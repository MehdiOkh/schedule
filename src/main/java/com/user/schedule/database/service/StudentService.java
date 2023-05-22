package com.user.schedule.database.service;

import com.user.schedule.database.model.Student;
import com.user.schedule.database.model.StudentUnit;
import com.user.schedule.database.repository.StudentRepo;
import com.user.schedule.database.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepo studentRepo;


    public List<StudentUnit> getStudentUnits(int studentId){
        Student currentStudent = studentRepo.getById(studentId);
        return currentStudent.getStudentUnits();
    }

}
