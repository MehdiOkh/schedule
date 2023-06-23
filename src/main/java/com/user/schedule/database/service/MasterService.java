package com.user.schedule.database.service;

import com.user.schedule.database.model.Master;
import com.user.schedule.database.model.Student;
import com.user.schedule.database.model.StudentUnit;
import com.user.schedule.database.model.TimeTable;
import com.user.schedule.database.repository.MasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterService {
    @Autowired
    private MasterRepo masterRepo;

    public Master getMasterById(int id) {
        return masterRepo.getById(id);
    }

    public List<TimeTable> getMaterTimeTables(int masterId){
        Master currentMaster = masterRepo.getById(masterId);
        return currentMaster.getTimeTableList();
    }
}
