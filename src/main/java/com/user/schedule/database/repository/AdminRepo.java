package com.user.schedule.database.repository;

import com.user.schedule.database.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<Admin,Integer> {
}
