package com.user.schedule.database.repository;

import com.user.schedule.database.model.Master;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterRepo extends JpaRepository<Master,Integer> {
}
