package com.user.schedule.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "unit_pick_time_table")
public class UnitPickTime {

    @Id
    @JoinColumn(name = "entrance_year")
    private int entranceYear;

    @JoinColumn(name = "pick_time")
    private Timestamp pickTime;

    @JoinColumn(name = "modify_time")
    private Timestamp modifyTime;

    @JsonIgnore
    @OneToMany(mappedBy = "unitPickTimeTable")
    private List<Student> studentList = new ArrayList<>();

    public UnitPickTime() {
    }

    public UnitPickTime(int entranceYear) {
        this.entranceYear = entranceYear;
    }

    public int getEntranceYear() {
        return entranceYear;
    }

    public void setEntranceYear(int entranceYear) {
        this.entranceYear = entranceYear;
    }

    public Timestamp getPickTime() {
        return pickTime;
    }

    public void setPickTime(Timestamp pickTime) {
        this.pickTime = pickTime;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }
}
