package com.user.schedule.database.repository;

import com.user.schedule.database.model.Bell;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BellRepo extends JpaRepository<Bell,Integer> {
}
