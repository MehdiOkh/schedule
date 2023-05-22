package com.user.schedule.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students_table")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;
    @ManyToOne
    @JoinColumn(name = "entrance_year")
    private UnitPickTime unitPickTimeTable;

//    @JsonIgnore
//    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinTable(
//            name = "student_units",
//            joinColumns = @JoinColumn(name = "student_id"),
//            inverseJoinColumns = @JoinColumn(name = "time_table_id")
//    )
//    private List<TimeTable> timeTableList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentUnit> studentUnits = new ArrayList<>();

    public Student() {
    }

    public Student(User user, Major major, UnitPickTime unitPickTime) {
        this.user = user;
        this.major = major;
        this.unitPickTimeTable = unitPickTime;
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

    public List<StudentUnit> getStudentUnits() {
        return studentUnits;
    }

    public void setStudentUnits(List<StudentUnit> studentUnits) {
        this.studentUnits = studentUnits;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public UnitPickTime getUnitPickTimeTable() {
        return unitPickTimeTable;
    }

    public void setUnitPickTimeTable(UnitPickTime unitPickTimeTable) {
        this.unitPickTimeTable = unitPickTimeTable;
    }
    //    public List<TimeTable> getTimeTableList() {
//        return timeTableList;
//    }
//
//    public void setTimeTableList(List<TimeTable> timeTableList) {
//        this.timeTableList = timeTableList;
//    }

}
