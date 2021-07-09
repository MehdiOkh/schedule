package com.user.schedule.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "time_table_bell")
public class TimeTableBell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "master_id")
    private Master master;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "bell_id")
    private Bell bell;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "day_id")
    private Day day;

//    @JsonIgnore
//    @OneToMany(mappedBy = "timeTableBell",cascade = CascadeType.ALL)
//    private List<TimeTable> timeTableList = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "time_table_id")
    @JsonIgnore
    private TimeTable timeTable;

    public TimeTableBell(Bell bell, Day day) {
        this.bell = bell;
        this.day = day;
    }

    public TimeTableBell() {
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

    public Bell getBell() {
        return bell;
    }

    public void setBell(Bell bell) {
        this.bell = bell;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

//    public List<TimeTable> getTimeTableList() {
//        return timeTableList;
//    }
//
//    public void setTimeTableList(List<TimeTable> timeTableList) {
//        this.timeTableList = timeTableList;
//    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }
}
