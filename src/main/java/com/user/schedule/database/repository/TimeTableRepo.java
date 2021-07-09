package com.user.schedule.database.repository;

import com.user.schedule.database.model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableRepo extends JpaRepository<TimeTable,Integer> {
}
