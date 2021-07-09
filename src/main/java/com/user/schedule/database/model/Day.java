package com.user.schedule.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "day_table")
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "label")
    private String label;

    @Column(name = "day_of_week")
    private int dayOfWeek;

    @JsonIgnore
    @OneToMany(mappedBy = "day",cascade = CascadeType.ALL)
    private List<TimeTableBell> timeTableBellList = new ArrayList<>();

    public Day() {
    }

    public Day(String label, int dayOfWeek) {
        this.label = label;
        this.dayOfWeek = dayOfWeek;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public List<TimeTableBell> getTimeTableBellList() {
        return timeTableBellList;
    }

    public void setTimeTableBellList(List<TimeTableBell> timeTableBellList) {
        this.timeTableBellList = timeTableBellList;
    }
}
