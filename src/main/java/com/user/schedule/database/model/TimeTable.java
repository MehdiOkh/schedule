package com.user.schedule.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "time_table")
public class TimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "master_id")
    private Master master;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "course_id")
    private Course course;

    @JsonIgnore
    @OneToMany(mappedBy = "timeTable", cascade = CascadeType.ALL)
    private List<TimeTableBell> timeTableBellList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "timeTable", cascade = CascadeType.ALL)
    private List<Announcement> announcementList = new ArrayList<>();

//    @JsonIgnore
//    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
//    @JoinTable(
//            name = "student_units",
//            joinColumns = @JoinColumn(name = "time_table_id"),
//            inverseJoinColumns = @JoinColumn(name = "student_id")
//    )
//    private List<Student> studentList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentUnit> studentUnits = new ArrayList<>();

    public TimeTable(Master master, Course course) {
        this.master = master;
        this.course = course;
    }

    public TimeTable() {
    }

    public List<Announcement> getAnnouncementList() {
        return announcementList;
    }

    public void setAnnouncementList(List<Announcement> announcementList) {
        this.announcementList = announcementList;
    }


    public int getId() {
        return id;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

//    public TimeTableBell getTimeTableBell() {
//        return timeTableBell;
//    }
//
//    public void setTimeTableBell(TimeTableBell timeTableBell) {
//        this.timeTableBell = timeTableBell;
//    }

//    public List<Student> getStudentList() {
//        return studentList;
//    }
//
//    public void setStudentList(List<Student> studentList) {
//        this.studentList = studentList;
//    }

    public List<TimeTableBell> getTimeTableBellList() {
        return timeTableBellList;
    }

    public void setTimeTableBellList(List<TimeTableBell> timeTableBellList) {
        this.timeTableBellList = timeTableBellList;
    }

    public List<StudentUnit> getStudentUnits() {
        return studentUnits;
    }

    public void setStudentUnits(List<StudentUnit> studentUnits) {
        this.studentUnits = studentUnits;
    }
}
