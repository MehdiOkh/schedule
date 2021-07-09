package com.user.schedule.database.service;

import com.user.schedule.database.model.*;
import com.user.schedule.database.repository.BellRepo;
import com.user.schedule.database.repository.DayRepo;
import com.user.schedule.database.repository.TimeTableBellRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TimeTableBellsService {

    @Autowired
    private TimeTableBellRepo timeTableBellRepo;




    public TimeTableBells getTimeTableBellList(int pageSize, int page){
        return new TimeTableBells(pageSize,page);
    }

    public TimeTableBell addTimeTableBell(TimeTableBell timeTableBell){

        return timeTableBellRepo.save(timeTableBell);
    }

    public TimeTableBell editTimeTableBell(int id,TimeTableBell timeTableBell) throws Exception {
        TimeTableBell formerTimeTableBell;
        if (timeTableBellRepo.findById(id).isPresent()){
            formerTimeTableBell = timeTableBellRepo.findById(id).get();
        }else {
            throw new Exception();
        }
        if (timeTableBell.getBell().getId()!=0) {
            formerTimeTableBell.setBell(timeTableBell.getBell());
        }
        if (timeTableBell.getDay().getId()!=0){
            formerTimeTableBell.setBell(timeTableBell.getBell());
        }
        timeTableBellRepo.flush();
        return timeTableBellRepo.findById(id).get();
    }

    public void deleteTimeTableBell(int id) throws Exception {

        if (timeTableBellRepo.findById(id).isPresent()){
            timeTableBellRepo.deleteById(id);
        }else {
            throw new Exception();
        }

    }

    public TimeTableBell getById(int id) throws Exception {
        TimeTableBell timeTableBell;
        if (timeTableBellRepo.findById(id).isPresent()){
            timeTableBell = timeTableBellRepo.findById(id).get();
        }else {
            throw new Exception();
        }
        return timeTableBell;
    }

    public class TimeTableBells{
        private List<TimeTableBell> list = null;

        private int pageSize;
        private int page;
        private int totalPage;


        public TimeTableBells(int pageSize, int page) {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(pageSize, page);

        }
        public List<TimeTableBell> listMaker(int pageSize, int page){
            List<TimeTableBell> list = timeTableBellRepo.findAll();
            List<TimeTableBell> temp = new ArrayList<>();


            int begin = (page-1)*pageSize==0? 0:(page-1)*pageSize;
            int end = page*pageSize;

            this.totalPage =(int) Math.ceil((double)list.size()/(double)pageSize);

            while (begin<end && list.size()>begin){
                temp.add(list.get(begin++));
            }

            return temp;
        }


        public List<TimeTableBell> getList() {
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
