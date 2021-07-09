package com.user.schedule.database.service;

import com.user.schedule.database.model.Admin;
import com.user.schedule.database.model.Master;
import com.user.schedule.database.model.Student;
import com.user.schedule.database.model.User;
import com.user.schedule.database.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Service
public class UsersService {

    @Autowired
    private UserRepo userRepo;

    public Users getUserList(String lastName, int pageSize, int page) {
        return new Users(lastName,pageSize,page);
    }
    public User findByCode(String code){
        return userRepo.findByCode(code);
    }

    public User addUser(User user){
        switch (user.getRole()){
            case "ADMIN":
                Admin admin = new Admin(user);
                user.getAdminList().add(admin);
                break;
            case "STUDENT":
                Student student = new Student(user);
                user.getStudentList().add(student);
                break;
            case "MASTER":
                Master master = new Master(user);
                user.getMasterList().add(master);
                break;
        }
        return userRepo.save(user);
    }

    public List<User> addUsersList(List<User> userList){

        List<User> temp = new LinkedList<>();
        for (User user : userList){
            temp.add(addUser(user));
        }
        return temp;
    }

    public User getById(int id) throws Exception {

        User user;

        if (userRepo.findById(id).isPresent()){
            user = userRepo.findById(id).get();
        }else {
            throw new Exception();
        }

        return user;
    }

    public User editUser(int id, User user ) throws Exception {
        User formerUser;
        if (userRepo.findById(id).isPresent()){
            formerUser = userRepo.findById(id).get();
        }else {
            throw new Exception();
        }

        if (!user.getFirstName().equals("")) {
            formerUser.setFirstName(user.getFirstName());
        }
        if (!user.getLastName().equals("")){
            formerUser.setLastName(user.getLastName());
        }
        if (!user.getPassword().equals("")){
            formerUser.setPassword(user.getPassword());
        }
        if (!user.getRole().equals("")){
            formerUser.setRole(user.getRole());
        }
        if (!user.getCode().equals("")){
            formerUser.setCode(user.getCode());
        }
        userRepo.flush();
        return userRepo.findById(id).get();
    }

    public void deleteUser(int id) throws Exception {

        if (userRepo.findById(id).isPresent()){
            userRepo.deleteById(id);
        }else {
            throw new Exception();
        }

    }

    public class Users{

        private List<User> list = null;

        private int pageSize;
        private int page;
        private int totalPage;


        public Users(String lastName, int pageSize, int page) {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(lastName, pageSize, page);

        }
        public List<User> listMaker(String lastName, int pageSize, int page){
            List<User> list = new ArrayList<>();
            List<User> temp = new ArrayList<>();

            int begin = (page-1)*pageSize==0? 0:(page-1)*pageSize;
            int end = page*pageSize;

            if (!lastName.equals("")){
                for (User user : userRepo.findAll()){
                    if(user.getLastName().equalsIgnoreCase(lastName)){
                        list.add(user);
                    }
                }
            }else {
                list = userRepo.findAll();
            }
            this.totalPage =(int) Math.ceil((double)list.size()/(double)pageSize);

            while (begin<end && list.size()>begin){
                temp.add(list.get(begin++));
            }

            return temp;
        }


        public List<User> getList() {
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
