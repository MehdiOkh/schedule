package com.user.schedule.database.service;

import com.user.schedule.database.model.Course;
import com.user.schedule.database.model.Master;
import com.user.schedule.database.model.TimeTable;
import com.user.schedule.database.repository.CourseRepo;
import com.user.schedule.exceptions.CourseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoursesService {

    @Autowired
    private CourseRepo courseRepo;

    public Courses getCourseList(String courseName, int pageSize, int page) {
        return new Courses(courseName, pageSize, page);
    }

    public Course getCourseByName(String courseName) throws Exception {
        List<Course> courses = courseRepo.findAll();
        for (Course course : courses
        ) {
            if (course.getTitle().equals(courseName)) return course;
        }
        throw new CourseException.CourseNotFoundException("Such course is not defined yet");
    }

    //    public CoursePrerequisite addPrerequisite(String courseName, String prerequisiteName) throws Exception {
//        Course course = coursesService.getCourseByName(courseName);
//        Course prerequisite = coursesService.getCourseByName(prerequisiteName);
//        CoursePrerequisite coursePrerequisite = new CoursePrerequisite(course, prerequisite);
//        coursePrerequisiteRepo.save(coursePrerequisite);
//        return coursePrerequisite;
//    }
//
    public List<Course> getCoursePrerequisites(String courseName) throws Exception {
        return getCourseByName(courseName).getCoursePrerequisiteList();
    }

    @Transactional
    public List<Course> addPrerequisite(String courseName, String[] prerequisiteList) throws Exception {
        Course course = getCourseByName(courseName);
        List<Course> prerequisites = course.getCoursePrerequisiteList();

        if (prerequisiteList.length != 0) {
            for (String name : prerequisiteList
            ) {
                prerequisites.add(getCourseByName(name));
            }

            courseRepo.flush();
        }
        return prerequisites;
    }

    @Transactional
    public void deletePrerequisite(String courseName, String[] prerequisiteList) throws Exception {
        ArrayList<Integer> ids = new ArrayList<>();
        List<Course> prerequisites = getCourseByName(courseName).getCoursePrerequisiteList();
        for (String name : prerequisiteList) {
            for (Course prerequisite : prerequisites) {
                if (name.equals(prerequisite.getTitle())) ids.add(prerequisite.getId());
            }
        }
        prerequisites.removeIf(course -> ids.contains(course.getId()));

//        return prerequisiteService.getCoursePrerequisites(courseName);
    }

    @Transactional
    public Course addCourse(Course course) throws Exception {
        List<Course> prerequisites = new ArrayList<>();
        Course newCourse = courseRepo.save(course);

        if (course.getPrerequisiteList() != null) {
            for (String courseName : course.getPrerequisiteList()
            ) {
                prerequisites.add(getCourseByName(courseName));
            }
            newCourse.setCoursePrerequisiteList(prerequisites);
        }
        return newCourse;

    }

    public Course getById(int id) throws Exception {

        Course course;

        if (courseRepo.findById(id).isPresent()) {
            course = courseRepo.findById(id).get();
        } else {
            throw new Exception();
        }

        return course;
    }

    public Course editCourse(int id, Course course) throws Exception {
        Course formerCourse;
        List<Course> prerequisites = new ArrayList<>();
        if (courseRepo.findById(id).isPresent()) {
            formerCourse = courseRepo.findById(id).get();
        } else {
            throw new Exception();
        }

        if (!course.getTitle().equals("")) {
            formerCourse.setTitle(course.getTitle());
        }
        if (course.getUnitsCount() != 0) {
            formerCourse.setUnitsCount(course.getUnitsCount());
        }
        if (course.getPrerequisiteList() != null) {
            for (String courseName : course.getPrerequisiteList()
            ) {
                prerequisites.add(getCourseByName(courseName));
            }
            formerCourse.setCoursePrerequisiteList(prerequisites);
        }
        courseRepo.flush();
        return courseRepo.findById(id).get();
    }

    public void deleteCourse(int id) throws Exception {

        if (courseRepo.findById(id).isPresent()) {
            courseRepo.deleteById(id);
        } else {
            throw new CourseException.CourseNotFoundException("course not found");
        }

    }

    public CourseTimeTables getCourseTimeTables(int id, int pageSize, int page) throws Exception {
        return new CourseTimeTables(id, pageSize, page);
    }

    public void masterCourseChoose(Master master, int courseId) throws Exception {
        Course course = getById(courseId);
        course.getMasterList().add(master);
//        master.getCourseList().add(course);
        courseRepo.flush();
    }

    public Masters getCourseMasters(int id, int pageSize, int page) throws Exception {

        return new Masters(id, pageSize, page);
    }

    public static class TodayClass {
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

    public class Masters {
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
            Course course = getById(id);
            List<TimeTable> timeTables = course.getTimeTableList();
            for (TimeTable timeTable : timeTables
            ) {
                list.add(timeTable.getMaster());
            }
            List<Master> temp = new ArrayList<>();

            int begin = (page - 1) * pageSize == 0 ? 0 : (page - 1) * pageSize;
            int end = page * pageSize;


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

    public class CourseTimeTables {
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

            int begin = (page - 1) * pageSize == 0 ? 0 : (page - 1) * pageSize;
            int end = page * pageSize;

            list = getById(id).getTimeTableList();


            this.totalPage = (int) Math.ceil((double) list.size() / (double) pageSize);

            while (begin < end && list.size() > begin) {
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

    public class Courses {

        private List<Course> list = null;

        private int pageSize;
        private int page;
        private int totalPage;


        public Courses(String lastName, int pageSize, int page) {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(lastName, pageSize, page);

        }

        public List<Course> listMaker(String courseName, int pageSize, int page) {
            List<Course> list = new ArrayList<>();
            List<Course> temp = new ArrayList<>();

            int begin = (page - 1) * pageSize == 0 ? 0 : (page - 1) * pageSize;
            int end = page * pageSize;

            if (!courseName.equals("")) {
                for (Course course : courseRepo.findAll()) {
                    if (course.getTitle().equalsIgnoreCase(courseName)) {
                        list.add(course);
                    }
                }
            } else {
                list = courseRepo.findAll();
            }
            this.totalPage = (int) Math.ceil((double) list.size() / (double) pageSize);

            while (begin < end && list.size() > begin) {
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
}












