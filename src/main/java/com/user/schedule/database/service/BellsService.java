package com.user.schedule.database.service;

import com.user.schedule.database.model.Bell;
import com.user.schedule.database.model.User;
import com.user.schedule.database.repository.BellRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BellsService {

    @Autowired
    private BellRepo bellRepo;

    public Bells getBellList(int pageSize, int page){
        return new Bells(pageSize,page);
    }

    public Bell addBell(Bell bell){
        return bellRepo.save(bell);
    }

    public Bell editBell(int id,Bell bell) throws Exception {
        Bell formerBell;
        if (bellRepo.findById(id).isPresent()){
            formerBell = bellRepo.findById(id).get();
        }else {
            throw new Exception();
        }
        if (!bell.getLabel().equals("")) {
            formerBell.setLabel(bell.getLabel());
        }
        if (bell.getBellOfDay()!=0){
            formerBell.setBellOfDay(bell.getBellOfDay());
        }
        bellRepo.flush();
        return bellRepo.findById(id).get();
    }

    public void deleteBell(int id) throws Exception {

        if (bellRepo.findById(id).isPresent()){
            bellRepo.deleteById(id);
        }else {
            throw new Exception();
        }

    }


    public Bell getById(int id) throws Exception {
        Bell bell;
        if (bellRepo.findById(id).isPresent()){
            bell = bellRepo.findById(id).get();
        }else {
            throw new Exception();
        }
        return bell;
    }



    public class Bells{
        private List<Bell> list = null;

        private int pageSize;
        private int page;
        private int totalPage;


        public Bells(int pageSize, int page) {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(pageSize, page);

        }
        public List<Bell> listMaker(int pageSize, int page){
            List<Bell> list = bellRepo.findAll();
            List<Bell> temp = new ArrayList<>();

            int begin = (page-1)*pageSize==0? 0:(page-1)*pageSize;
            int end = page*pageSize;

            this.totalPage =(int) Math.ceil((double)list.size()/(double)pageSize);

            while (begin<end && list.size()>begin){
                temp.add(list.get(begin++));
            }

            return temp;
        }


        public List<Bell> getList() {
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
















