package com.user.schedule.database.service;

import com.user.schedule.database.model.Bell;
import com.user.schedule.database.model.Day;
import com.user.schedule.database.repository.BellRepo;
import com.user.schedule.database.repository.DayRepo;
import org.springframework.beans.factory.annotation.Autowired;



public class TableBellsReference {

    private int dayId;
    private int bellId;

    public TableBellsReference(int dayId, int bellId) {
        this.dayId = dayId;
        this.bellId = bellId;

    }

    public int getDayId() {
        return dayId;
    }

    public int getBellId() {
        return bellId;
    }
}
