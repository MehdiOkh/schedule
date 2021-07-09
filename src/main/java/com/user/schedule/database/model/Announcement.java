package com.user.schedule.database.model;

import javax.persistence.*;

@Entity
@Table(name = "announcement_table")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "time_table_id")
    private TimeTable timeTable;

    @Column(name = "message")
    private String message;

    public Announcement() {

    }

    public Announcement(TimeTable timeTable, String message) {
        this.timeTable = timeTable;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
