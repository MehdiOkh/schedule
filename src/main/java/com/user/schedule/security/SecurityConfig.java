package com.user.schedule.security;


import com.user.schedule.security.service.JwtRequestFilter;
import com.user.schedule.security.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/days", "/api/days/*",
                        "/api/bells/*", "/api/bells",
                        "/api/announcements", "/api/announcements/*",
                        "/api/courses/*/masters", "/api/courses/*/timetables", "/api/time-tables/**").hasAnyRole("ADMIN", "MASTER", "STUDENT")
                .antMatchers("/api/users/profile", "/api/users/profile/change-password").hasAnyRole("ADMIN", "MASTER", "STUDENT")
                .antMatchers("/api/announcements/**", "/api/time-table-bells/**", "/api/courses/**").hasAnyRole("ADMIN", "MASTER")
                .antMatchers("/api/users/**", "/api/days/**", "/api/bells/**", "/api/time-tables/StartProcess", "api/majors/**", "api/unit-pick-time/**", "/api/masters").hasRole("ADMIN")
                .antMatchers("/api/time-tables/*/Choose", "/api/announcements/student-announcements", "/api/time-tables/*/student-units").hasRole("STUDENT")
                .antMatchers("/api/auth/login", "/v3/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
