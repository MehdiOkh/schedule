package com.user.schedule.database.repository;

import com.user.schedule.database.model.StudentUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentUnitRepo extends JpaRepository<StudentUnit, Integer> {
}