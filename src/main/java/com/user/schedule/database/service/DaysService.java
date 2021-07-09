package com.user.schedule.database.service;

import com.user.schedule.database.model.Day;
import com.user.schedule.database.repository.DayRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DaysService {
    @Autowired
    private DayRepo dayRepo;
    
    public Days getDayList(int pageSize, int page){
        return new Days(pageSize,page);
    }

    public Day addDay(Day Day){
        return dayRepo.save(Day);
    }

    public Day editDay(int id,Day Day) throws Exception {
        Day formerDay;
        if (dayRepo.findById(id).isPresent()){
            formerDay = dayRepo.findById(id).get();
        }else {
            throw new Exception();
        }
        if (!Day.getLabel().equals("")) {
            formerDay.setLabel(Day.getLabel());
        }
        if (Day.getDayOfWeek()!=0){
            formerDay.setDayOfWeek(Day.getDayOfWeek());
        }
        dayRepo.flush();
        return dayRepo.findById(id).get();
    }

    public void deleteDay(int id) throws Exception {

        if (dayRepo.findById(id).isPresent()){
            dayRepo.deleteById(id);
        }else {
            throw new Exception();
        }

    }


    public Day getById(int id) throws Exception {
        Day Day;
        if (dayRepo.findById(id).isPresent()){
            Day = dayRepo.findById(id).get();
        }else {
            throw new Exception();
        }
        return Day;
    }

    public class Days{
        private List<Day> list = null;

        private int pageSize;
        private int page;
        private int totalPage;


        public Days(int pageSize, int page) {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(pageSize, page);

        }
        public List<Day> listMaker(int pageSize, int page){
            List<Day> list = dayRepo.findAll();
            List<Day> temp = new ArrayList<>();

            int begin = (page-1)*pageSize==0? 0:(page-1)*pageSize;
            int end = page*pageSize;

            this.totalPage =(int) Math.ceil((double)list.size()/(double)pageSize);

            while (begin<end && list.size()>begin){
                temp.add(list.get(begin++));
            }

            return temp;
        }


        public List<Day> getList() {
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
