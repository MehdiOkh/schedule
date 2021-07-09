package com.user.schedule.database.repository;

import com.user.schedule.database.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student,Integer> {
}
