package com.user.schedule.database.service;

import com.user.schedule.database.model.*;
import com.user.schedule.database.repository.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoursesService {
    
    @Autowired
    private CourseRepo courseRepo;
    
    public Courses getCourseList(String courseName, int pageSize, int page){
        return new Courses(courseName,pageSize,page);
    }

    public Course addCourse(Course course){
        return courseRepo.save(course);
    }

    public Course getById(int id) throws Exception {

        Course course;

        if (courseRepo.findById(id).isPresent()){
            course = courseRepo.findById(id).get();
        }else {
            throw new Exception();
        }

        return course;
    }

    public Course editCourse(int id, Course course ) throws Exception {
        Course formerCourse;
        if (courseRepo.findById(id).isPresent()){
            formerCourse = courseRepo.findById(id).get();
        }else {
            throw new Exception();
        }

        if (!course.getTitle().equals("")) {
            formerCourse.setTitle(course.getTitle());
        }
        if (course.getUnitsCount()!=0){
            formerCourse.setUnitsCount(course.getUnitsCount());
        }
        courseRepo.flush();
        return courseRepo.findById(id).get();
    }

    public void deleteCourse(int id) throws Exception {

        if (courseRepo.findById(id).isPresent()){
            courseRepo.deleteById(id);
        }else {
            throw new Exception();
        }

    }

    public CourseTimeTables getCourseTimeTables(int id, int pageSize, int page) throws Exception {
        return new CourseTimeTables(id,pageSize,page);
    }

    public void masterCourseChoose(Master master, int courseId) throws Exception {
        Course course = getById(courseId);
        course.getMasterList().add(master);
//        master.getCourseList().add(course);
        courseRepo.flush();
    }

    public Masters getCourseMasters(int id, int pageSize, int page) throws Exception {
        return new Masters(id,pageSize,page);
    }


    public class Masters{
        private List<Master> list = null;

        private int pageSize;
        private int page;
        private int totalPage;


        public Masters(int id, int pageSize, int page) throws Exception {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(id, pageSize, page);

        }

        public List<Master> listMaker(int id, int pageSize, int page) throws Exception {
            List<Master> list = getById(id).getMasterList();
            List<Master> temp = new ArrayList<>();

            int begin = (page-1)*pageSize==0? 0:(page-1)*pageSize;
            int end = page*pageSize;


            this.totalPage =(int) Math.ceil((double)list.size()/(double)pageSize);

            while (begin<end && list.size()>begin){
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

    public class  CourseTimeTables{
        private List<TimeTable> list = null;

        private int pageSize;
        private int page;
        private int totalPage;


        public CourseTimeTables(int id, int pageSize, int page) throws Exception {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(id, pageSize, page);

        }

        public List<TimeTable> listMaker(int id, int pageSize, int page) throws Exception {
            List<TimeTable> list = new ArrayList<>();
            List<TimeTable> temp = new ArrayList<>();

            int begin = (page-1)*pageSize==0? 0:(page-1)*pageSize;
            int end = page*pageSize;

            list = getById(id).getTimeTableList();


            this.totalPage =(int) Math.ceil((double)list.size()/(double)pageSize);

            while (begin<end && list.size()>begin){
                temp.add(list.get(begin++));
            }

            return temp;
        }


        public List<TimeTable> getList() {
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

    public class Courses{

        private List<Course> list = null;

        private int pageSize;
        private int page;
        private int totalPage;


        public Courses(String lastName, int pageSize, int page) {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(lastName, pageSize, page);

        }
        
        public List<Course> listMaker(String courseName, int pageSize, int page){
            List<Course> list = new ArrayList<>();
            List<Course> temp = new ArrayList<>();

            int begin = (page-1)*pageSize==0? 0:(page-1)*pageSize;
            int end = page*pageSize;

            if (!courseName.equals("")){
                for (Course course : courseRepo.findAll()){
                    if(course.getTitle().equalsIgnoreCase(courseName)){
                        list.add(course);
                    }
                }
            }else {
                list = courseRepo.findAll();
            }
            this.totalPage =(int) Math.ceil((double)list.size()/(double)pageSize);

            while (begin<end && list.size()>begin){
                temp.add(list.get(begin++));
            }

            return temp;
        }


        public List<Course> getList() {
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

    public static class TodayClass{
        private String course;
        private String day;
        private String bell;

        public TodayClass(String course, String day, String bell) {
            this.course = course;
            this.day = day;
            this.bell = bell;
        }

        public String getCourse() {
            return course;
        }

        public String getDay() {
            return day;
        }

        public String getBell() {
            return bell;
        }
    }
}












