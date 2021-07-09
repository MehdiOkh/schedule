package com.user.schedule.database.repository;

import com.user.schedule.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
    User findByCode(String code);
}
