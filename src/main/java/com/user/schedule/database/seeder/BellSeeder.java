package com.user.schedule.database.seeder;

import com.user.schedule.database.model.Bell;
import com.user.schedule.database.model.Day;
import com.user.schedule.database.repository.BellRepo;
import com.user.schedule.database.repository.DayRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BellSeeder {
    @Autowired
    private BellRepo bellRepo;

    public void populateBellsOfDay(){
        bellRepo.deleteAll();
        bellRepo.flush();
        List<Bell> bellsOfDay = new ArrayList<>();
        bellsOfDay.add(new Bell("8-10",1));
        bellsOfDay.add(new Bell("10-12",2));
        bellsOfDay.add(new Bell("14-16",3));
        bellsOfDay.add(new Bell("16-18",4));
        bellRepo.saveAll(bellsOfDay);
    }
}
