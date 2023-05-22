package com.user.schedule.database.service;

import com.user.schedule.database.model.UnitPickTime;
import com.user.schedule.database.repository.UnitPickTimeRepo;
import com.user.schedule.exceptions.MajorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UnitPickTimeService {
    @Autowired
    private UnitPickTimeRepo unitPickTimeRepo;

    public UnitPickTime addUnitPickTime(UnitPickTime time) {
        return unitPickTimeRepo.save(time);
    }

    public UnitPickTimes getUnitPickTimeList(int pageSize, int page) {
        return new UnitPickTimes(pageSize, page);
    }

    public UnitPickTime editUnitPickTime(int id, UnitPickTime unitPickTime) throws Exception {
        UnitPickTime formerUnitPickTime;
        if (unitPickTimeRepo.findById(id).isPresent()) {
            formerUnitPickTime = unitPickTimeRepo.findById(id).get();
        } else {
            throw new Exception();
        }

        formerUnitPickTime.setEntranceYear(unitPickTime.getEntranceYear());
        formerUnitPickTime.setPickTime(unitPickTime.getPickTime());
        formerUnitPickTime.setModifyTime(unitPickTime.getModifyTime());

        unitPickTimeRepo.flush();
        return unitPickTimeRepo.findById(id).get();
    }

    public UnitPickTime getById(int id) throws Exception {
        UnitPickTime time;
        if (unitPickTimeRepo.findById(id).isPresent()) {
            time = unitPickTimeRepo.findById(id).get();
        } else {
            throw new Exception();
        }
        return time;
    }

    public void deleteById(int id) throws Exception {

        if (unitPickTimeRepo.findById(id).isPresent()) {
            unitPickTimeRepo.deleteById(id);
        } else {
            throw new Exception();
        }
    }

    public UnitPickTime getByYear(int year) throws Exception {

        List<UnitPickTime> times = unitPickTimeRepo.findAll();
        for (UnitPickTime time : times) {
            if (time.getEntranceYear() == year) return time;
        }

        throw new MajorException.MajorNotFoundException("UnitPickTime with specified name not found!");

    }

    public class UnitPickTimes {
        private List<UnitPickTime> list = null;

        private int pageSize;
        private int page;
        private int totalPage;


        public UnitPickTimes(int pageSize, int page) {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(pageSize, page);

        }

        public List<UnitPickTime> listMaker(int pageSize, int page) {
            List<UnitPickTime> list = unitPickTimeRepo.findAll();
            List<UnitPickTime> temp = new ArrayList<>();

            int begin = (page - 1) * pageSize == 0 ? 0 : (page - 1) * pageSize;
            int end = page * pageSize;

            this.totalPage = (int) Math.ceil((double) list.size() / (double) pageSize);

            while (begin < end && list.size() > begin) {
                temp.add(list.get(begin++));
            }

            return temp;
        }


        public List<UnitPickTime> getList() {
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
