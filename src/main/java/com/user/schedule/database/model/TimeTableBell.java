package com.user.schedule.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "time_table_bell")
public class TimeTableBell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "bell_id")
    private Bell bell;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "day_id")
    private Day day;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "time_table_id")
    @JsonIgnore
    private TimeTable timeTable;

    @Column(name = "week_type")
    @Enumerated(EnumType.STRING)
    private WeekType weekType;

    @Column(name = "room_number")
    private int roomNumber;

    public TimeTableBell(Bell bell, Day day) {
        this.bell = bell;
        this.day = day;
    }

    public TimeTableBell(Bell bell, Day day, WeekType weekType, int roomNumber) {
        this.bell = bell;
        this.day = day;
        this.weekType = weekType;
        this.roomNumber = roomNumber;
    }


//    @JsonIgnore
//    @OneToMany(mappedBy = "timeTableBell",cascade = CascadeType.ALL)
//    private List<TimeTable> timeTableList = new ArrayList<>();

    public TimeTableBell() {
    }

    public int getId() {
        return id;
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

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    public WeekType getWeekType() {
        return weekType;
    }

    public void setWeekType(WeekType weekType) {
        this.weekType = weekType;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public enum WeekType {
        odd, even, both
    }
}
