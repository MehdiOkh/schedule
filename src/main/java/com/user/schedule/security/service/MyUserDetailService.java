package com.user.schedule.security.service;

import com.user.schedule.database.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String code) throws UsernameNotFoundException {
        com.user.schedule.database.model.User user = userRepo.findByCode(code);
        List<SimpleGrantedAuthority> roles=null;

        if (user.getRole().equals("ADMIN")) {
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new User(user.getCode(),user.getPassword(),roles);

        }
        if (user.getRole().equals("MASTER")){
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_MASTER"));
            return new User(user.getCode(),user.getPassword(),roles);

        }
        if (user.getRole().equals("STUDENT")){
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_STUDENT"));
            return new User(user.getCode(),user.getPassword(),roles);

        }

        throw new UsernameNotFoundException("User not found with Code: " + code);
    }

}









