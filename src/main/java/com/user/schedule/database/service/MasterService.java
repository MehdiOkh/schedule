package com.user.schedule.database.service;

import com.user.schedule.database.model.Master;
import com.user.schedule.database.model.TimeTable;
import com.user.schedule.database.repository.MasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MasterService {
    @Autowired
    private MasterRepo masterRepo;

    public Master getMasterById(int id) {
        return masterRepo.getById(id);
    }

    public List<TimeTable> getMaterTimeTables(int masterId) {
        Master currentMaster = masterRepo.getById(masterId);
        return currentMaster.getTimeTableList();
    }

    public List<Master> getMastersList(String lastName, int pageSize, int page) {
        return new Masters(lastName, pageSize, page).getList();
    }

    public class Masters {

        private List<Master> list = null;

        private int pageSize;
        private int page;
        private int totalPage;


        public Masters(String lastName, int pageSize, int page) {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(lastName, pageSize, page);

        }

        public List<Master> listMaker(String name, int pageSize, int page) {
            List<Master> list = new ArrayList<>();
            List<Master> temp = new ArrayList<>();

            int begin = (page - 1) * pageSize == 0 ? 0 : (page - 1) * pageSize;
            int end = page * pageSize;

            if (!name.equals("")) {
                for (Master master : masterRepo.findAll()) {
                    if (master.getUser().getLastName().equalsIgnoreCase(name)) {
                        list.add(master);
                    }
                }
            } else {
                list = masterRepo.findAll();
            }
            this.totalPage = (int) Math.ceil((double) list.size() / (double) pageSize);

            while (begin < end && list.size() > begin) {
                temp.add(list.get(begin++));
            }

            return temp;
        }

        public List<Master> getList() {
            return list;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getPage() {
            return page;
        }

        public int getTotalPage() {
            return totalPage;
        }
    }
}
