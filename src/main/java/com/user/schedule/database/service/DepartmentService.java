package com.user.schedule.database.service;

import com.user.schedule.database.model.Department;
import com.user.schedule.database.repository.DepartmentRepo;
import com.user.schedule.exceptions.MajorException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {
    @Autowired
    private DepartmentRepo departmentRepo;

    public Department addMajor(Department department) {
        return departmentRepo.save(department);
    }

    public Departments getDayList(int pageSize, int page) {
        return new Departments(pageSize, page);
    }

    public Department editDay(int id, Department Department) throws Exception {
        Department formerDepartment;
        if (departmentRepo.findById(id).isPresent()){
            formerDepartment = departmentRepo.findById(id).get();
        }else {
            throw new Exception();
        }
        if (!formerDepartment.getDepartmentName().equals("")) {
            formerDepartment.setDepartmentName(Department.getDepartmentName());
        }
        departmentRepo.flush();
        return departmentRepo.findById(id).get();
    }

    public Department getById(int id) throws Exception {
        Department department;
        if (departmentRepo.findById(id).isPresent()) {
            department = departmentRepo.findById(id).get();
        } else {
            throw new Exception();
        }
        return department;
    }

    public void deleteById(int id) throws Exception {

        if (departmentRepo.findById(id).isPresent()) {
            departmentRepo.deleteById(id);
        } else {
            throw new Exception();
        }
    }

    public Department getByName(String name) throws Exception {

        List<Department> departments = departmentRepo.findAll();
        for (Department department : departments) {
            if (department.getDepartmentName().equals(name)) return department;
        }

        throw new MajorException.MajorNotFoundException("Department with specified name not found!");

    }

    public class Departments {
        private List<Department> list = null;

        private int pageSize;
        private int page;
        private int totalPage;


        public Departments(int pageSize, int page) {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(pageSize, page);

        }

        public List<Department> listMaker(int pageSize, int page) {
            List<Department> list = departmentRepo.findAll();
            List<Department> temp = new ArrayList<>();

            int begin = (page - 1) * pageSize == 0 ? 0 : (page - 1) * pageSize;
            int end = page * pageSize;

            this.totalPage = (int) Math.ceil((double) list.size() / (double) pageSize);

            while (begin < end && list.size() > begin) {
                temp.add(list.get(begin++));
            }

            return temp;
        }


        public List<Department> getList() {
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
