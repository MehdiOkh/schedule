package com.user.schedule.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course_table")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "units_count")
    private int unitsCount;

    @Column(name = "title")
    private String title;

    @Transient
    @JsonIgnore
    private String[] prerequisiteList;

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "master_id")
//    private Master master;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "master_course_join",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "master_id")
    )
    private List<Master> masterList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<TimeTable> timeTableList = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "course_prerequisite_table",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    private List<Course> coursePrerequisiteList = new ArrayList<>();


    public Course(int unitsCount, String title) {
        this.unitsCount = unitsCount;
        this.title = title;
    }

    public Course() {
    }

    public int getId() {
        return id;
    }

    public int getUnitsCount() {
        return unitsCount;
    }

    public void setUnitsCount(int unitCount) {
        this.unitsCount = unitCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public Master getMaster() {
//        return master;
//    }
//
//    public void setMaster(Master master) {
//        this.master = master;
//    }

    public List<TimeTable> getTimeTableList() {
        return timeTableList;
    }

    public void setTimeTableList(List<TimeTable> timeTableList) {
        this.timeTableList = timeTableList;
    }

    public List<Master> getMasterList() {
        return masterList;
    }

    public void setMasterList(List<Master> masterList) {
        this.masterList = masterList;
    }

    public List<Course> getCoursePrerequisiteList() {
        return coursePrerequisiteList;
    }

    public void setCoursePrerequisiteList(List<Course> coursePrerequisiteList) {
        this.coursePrerequisiteList = coursePrerequisiteList;
    }

    public String[] getPrerequisiteList() {
        return prerequisiteList;
    }

    public void setPrerequisiteList(String[] prerequisiteList) {
        this.prerequisiteList = prerequisiteList;
    }
}
