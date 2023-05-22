package com.user.schedule.database.service;

import com.user.schedule.database.model.*;
import com.user.schedule.database.repository.UserRepo;
import com.user.schedule.exceptions.MajorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Service
public class UsersService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MajorService majorService;
    @Autowired
    private UnitPickTimeService unitPickTimeService;

    public Users getUserList(String lastName, int pageSize, int page) {
        return new Users(lastName, pageSize, page);
    }

    public User findByCode(String code) {
        return userRepo.findByCode(code);
    }

    public User addUser(User user) throws Exception {
        Major major = null;
        UnitPickTime unitPickTime = null;
        if (user.getMajor() != null) {
            major = majorService.getByName(user.getMajor());
        }

        if (user.getEntranceYear() != 0) {
            unitPickTime = unitPickTimeService.getByYear(user.getEntranceYear());
        }
        switch (user.getRole()) {
            case "ADMIN":
                Admin admin = new Admin(user);
                user.getAdminList().add(admin);
                break;
            case "STUDENT":
                if (major == null) throw new MajorException.MajorNotFoundException("Major is Required");
                if (unitPickTime == null) throw new MajorException.MajorNotFoundException("Entrance year is Required");
                Student student = new Student(user, major, unitPickTime);
                user.getStudentList().add(student);
                break;
            case "MASTER":
                if (major == null) throw new MajorException.MajorNotFoundException("Major is Required");
                Master master = new Master(user, major);
                user.getMasterList().add(master);
                break;
        }
        return userRepo.save(user);
    }

    public List<User> addUsersList(List<User> userList) throws Exception {

        List<User> temp = new LinkedList<>();
        for (User user : userList) {
            temp.add(addUser(user));
        }
        return temp;
    }

    public User getById(int id) throws Exception {

        User user;

        if (userRepo.findById(id).isPresent()) {
            user = userRepo.findById(id).get();
        } else {
            throw new Exception();
        }

        return user;
    }

    public User putUser(int id, UpdateUser user) throws Exception {
        User formerUser;
        if (userRepo.findById(id).isPresent()) {
            formerUser = userRepo.findById(id).get();
        } else {
            throw new Exception();
        }

        if (!user.getFirstName().equals("")) {
            formerUser.setFirstName(user.getFirstName());
        }
        if (!user.getLastName().equals("")) {
            formerUser.setLastName(user.getLastName());
        }
        userRepo.flush();
        return userRepo.findById(id).get();
    }

    public User editUser(int id, User user) throws Exception {
        User formerUser;
        if (userRepo.findById(id).isPresent()) {
            formerUser = userRepo.findById(id).get();
        } else {
            throw new Exception();
        }

        if (!user.getFirstName().equals("")) {
            formerUser.setFirstName(user.getFirstName());
        }
        if (!user.getLastName().equals("")) {
            formerUser.setLastName(user.getLastName());
        }
        if (!user.getPassword().equals("")) {
            formerUser.setPassword(user.getPassword());
        }
        if (!user.getRole().equals("")) {
            formerUser.setRole(user.getRole());
        }
        if (!user.getCode().equals("")) {
            formerUser.setCode(user.getCode());
        }
        userRepo.flush();
        return userRepo.findById(id).get();
    }

    public void deleteUser(int id) throws Exception {

        if (userRepo.findById(id).isPresent()) {
            userRepo.deleteById(id);
        } else {
            throw new Exception();
        }

    }

    public static class UpdateUser {
        String firstName;
        String lastName;

        public UpdateUser(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }

    public class Users {

        private List<User> list = null;

        private int pageSize;
        private int page;
        private int totalPage;


        public Users(String lastName, int pageSize, int page) {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(lastName, pageSize, page);

        }

        public List<User> listMaker(String lastName, int pageSize, int page) {
            List<User> list = new ArrayList<>();
            List<User> temp = new ArrayList<>();

            int begin = (page - 1) * pageSize == 0 ? 0 : (page - 1) * pageSize;
            int end = page * pageSize;

            if (!lastName.equals("")) {
                for (User user : userRepo.findAll()) {
                    if (user.getLastName().equalsIgnoreCase(lastName)) {
                        list.add(user);
                    }
                }
            } else {
                list = userRepo.findAll();
            }
            this.totalPage = (int) Math.ceil((double) list.size() / (double) pageSize);

            while (begin < end && list.size() > begin) {
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
