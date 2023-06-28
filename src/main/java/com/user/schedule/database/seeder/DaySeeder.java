package com.user.schedule.database.seeder;

import com.user.schedule.database.model.Day;
import com.user.schedule.database.repository.DayRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DaySeeder {
    @Autowired
    private DayRepo dayRepo;

    public void populateWeekDays(){
        dayRepo.deleteAll();
        dayRepo.flush();
        List<Day> daysOfWeek = new ArrayList<>();
        daysOfWeek.add(new Day("شنبه",1));
        daysOfWeek.add(new Day("یکشنبه",2));
        daysOfWeek.add(new Day("دوشنبه",3));
        daysOfWeek.add(new Day("سه شنبه",4));
        daysOfWeek.add(new Day("چهارشنبه",5));
        dayRepo.saveAll(daysOfWeek);
    }
}
