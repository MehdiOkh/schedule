package com.user.schedule.database.service;

import com.user.schedule.database.model.Major;
import com.user.schedule.database.repository.MajorRepo;
import com.user.schedule.exceptions.MajorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MajorService {
    @Autowired
    private MajorRepo majorRepo;

    public Major addMajor(Major major) {
        return majorRepo.save(major);
    }

    public Majors getMajorList(int pageSize, int page) {
        return new Majors(pageSize, page);
    }

    public Major editMajor(int id, Major Major) throws Exception {
        Major formerMajor;
        if (majorRepo.findById(id).isPresent()) {
            formerMajor = majorRepo.findById(id).get();
        } else {
            throw new Exception();
        }
        if (!Major.getMajorName().equals("")) {
            formerMajor.setMajorName(Major.getMajorName());
        }
        majorRepo.flush();
        return majorRepo.findById(id).get();
    }

    public Major getById(int id) throws Exception {
        Major major;
        if (majorRepo.findById(id).isPresent()) {
            major = majorRepo.findById(id).get();
        } else {
            throw new Exception();
        }
        return major;
    }

    public void deleteById(int id) throws Exception {

        if (majorRepo.findById(id).isPresent()) {
            majorRepo.deleteById(id);
        } else {
            throw new Exception();
        }
    }

    public Major getByName(String name) throws Exception {

        List<Major> majors = majorRepo.findAll();
        for (Major major : majors) {
            if (major.getMajorName().equals(name)) return major;
        }

        throw new MajorException.MajorNotFoundException("Major with specified name not found!");

    }

    public class Majors {
        private List<Major> list = null;

        private int pageSize;
        private int page;
        private int totalPage;


        public Majors(int pageSize, int page) {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(pageSize, page);

        }

        public List<Major> listMaker(int pageSize, int page) {
            List<Major> list = majorRepo.findAll();
            List<Major> temp = new ArrayList<>();

            int begin = (page - 1) * pageSize == 0 ? 0 : (page - 1) * pageSize;
            int end = page * pageSize;

            this.totalPage = (int) Math.ceil((double) list.size() / (double) pageSize);

            while (begin < end && list.size() > begin) {
                temp.add(list.get(begin++));
            }

            return temp;
        }


        public List<Major> getList() {
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
