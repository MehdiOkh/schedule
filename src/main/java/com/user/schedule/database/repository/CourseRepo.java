package com.user.schedule.database.repository;

import com.user.schedule.database.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepo extends JpaRepository<Course,Integer> {
}
