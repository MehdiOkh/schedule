package com.user.schedule.database.repository;

import com.user.schedule.database.model.UnitPickTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitPickTimeRepo extends JpaRepository<UnitPickTime, Integer> {
}