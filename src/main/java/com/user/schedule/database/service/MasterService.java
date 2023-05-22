package com.user.schedule.database.service;

import com.user.schedule.database.model.Master;
import com.user.schedule.database.repository.MasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasterService {
    @Autowired
    private MasterRepo masterRepo;

    public Master getMasterById(int id) {
        return masterRepo.getById(id);
    }
}
