package com.user.schedule.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "bell_table")
public class Bell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "label")
    private String label;

    @Column(name = "bell_of_day")
    private int bellOfDay;


    @JsonIgnore
    @OneToMany(mappedBy = "bell",cascade = CascadeType.ALL)
    private List<TimeTableBell> timeTableBellList = new ArrayList<>();

    public Bell(String label, int bellOfDay) {
        this.label = label;
        this.bellOfDay = bellOfDay;
    }

    public Bell() {
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

    public int getBellOfDay() {
        return bellOfDay;
    }

    public void setBellOfDay(int bellOfDay) {
        this.bellOfDay = bellOfDay;
    }

    public List<TimeTableBell> getTimeTableBellList() {
        return timeTableBellList;
    }

    public void setTimeTableBellList(List<TimeTableBell> timeTableBellList) {
        this.timeTableBellList = timeTableBellList;
    }
}
