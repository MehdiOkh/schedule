package com.user.schedule.database.service;


import com.user.schedule.database.model.TimeTableBell;

public class TableBellsReference {

    private int dayId;
    private int bellId;

    private TimeTableBell.WeekType weekType;

    private int roomNumber;

    public TableBellsReference(int dayId, int bellId, TimeTableBell.WeekType weekType, int roomNumber) {
        this.dayId = dayId;
        this.bellId = bellId;
        this.weekType = weekType;
        this.roomNumber = roomNumber;
    }


    public int getDayId() {
        return dayId;
    }

    public int getBellId() {
        return bellId;
    }

    public TimeTableBell.WeekType getWeekType() {
        return weekType;
    }

    public int getRoomNumber() {
        return roomNumber;
    }
}
