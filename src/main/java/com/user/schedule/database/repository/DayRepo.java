package com.user.schedule.database.repository;

import com.user.schedule.database.model.Day;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayRepo extends JpaRepository<Day,Integer> {
}
