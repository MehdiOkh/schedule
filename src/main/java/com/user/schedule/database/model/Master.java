package com.user.schedule.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "masters_table")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Master {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

//    @JsonIgnore
//    @OneToMany(mappedBy = "master", cascade = CascadeType.ALL)
//    private List<TimeTableBell> timeTableBellList = new ArrayList<>();

//    @JsonIgnore
//    @OneToMany(mappedBy = "master",cascade = CascadeType.ALL)
//    private List<Course> courseList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "master", cascade = CascadeType.ALL)
    private List<TimeTable> timeTableList = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "master_course_join",
            joinColumns = @JoinColumn(name = "master_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courseList = new ArrayList<>();


    public Master(User user, Major major) {
        this.user = user;
        this.major = major;
    }

    public Master() {
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public List<TimeTableBell> getTimeTableBellList() {
//        return timeTableBellList;
//    }
//
//    public void setTimeTableBellList(List<TimeTableBell> timeTableBellList) {
//        this.timeTableBellList = timeTableBellList;
//    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public List<TimeTable> getTimeTableList() {
        return timeTableList;
    }

    public void setTimeTableList(List<TimeTable> timeTableList) {
        this.timeTableList = timeTableList;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }
}
