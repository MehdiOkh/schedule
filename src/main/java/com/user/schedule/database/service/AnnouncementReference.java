package com.user.schedule.database.service;



public class AnnouncementReference {
    private int timeTableId;
    private String message;

    public AnnouncementReference(int timeTableId, String message) {
        this.timeTableId = timeTableId;
        this.message = message;
    }

    public int getTimeTable() {
        return timeTableId;
    }

    public String getMessage() {
        return message;
    }
}
