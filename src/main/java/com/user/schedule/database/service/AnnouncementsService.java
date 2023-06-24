package com.user.schedule.database.service;

import com.user.schedule.database.repository.AnnouncementRepo;
import com.user.schedule.database.repository.MasterRepo;
import com.user.schedule.database.repository.TimeTableRepo;
import com.user.schedule.database.model.Announcement;
import com.user.schedule.database.model.Course;
import com.user.schedule.database.model.TimeTable;
import com.user.schedule.exceptions.CourseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnnouncementsService {
    @Autowired
    private AnnouncementRepo announcementRepo;
    @Autowired
    private TimeTableRepo timeTableRepo;
    @Autowired
    private MasterRepo masterRepo;

    public Announcements getAnnouncementList(int masterId, int tableId, int pageSize, int page){
        return new Announcements(masterId,tableId,pageSize,page);
    }
    public Announcement addAnnouncement(Announcement announcement){
        return announcementRepo.save(announcement);
    }

    public Announcement editAnnouncement(int id,Announcement announcement) throws Exception {
        Announcement formerAnnouncement;
        if (announcementRepo.findById(id).isPresent()){
            formerAnnouncement = announcementRepo.findById(id).get();
        }else {
            throw new Exception();
        }
        if (!announcement.getMessage().equals("")) {
            formerAnnouncement.setMessage(announcement.getMessage());
        }
        if (announcement.getTimeTable()!=null) {
            formerAnnouncement.setTimeTable(announcement.getTimeTable());
        }

        announcementRepo.flush();
        return announcementRepo.findById(id).get();
    }

    public void deleteAnnouncement(int id) throws Exception {

        if (announcementRepo.findById(id).isPresent()){
            announcementRepo.deleteById(id);
        }else {
            throw new CourseException.AnnouncementNotFound("announcement not found");
        }

    }


    public Announcement getById(int id) throws Exception {
        Announcement announcement;
        if (announcementRepo.findById(id).isPresent()){
            announcement = announcementRepo.findById(id).get();
        }else {
            throw new CourseException.AnnouncementNotFound("announcement not found");
        }
        return announcement;
    }

    public class Announcements{
        private List<Announcement> list = null;

        private int pageSize;
        private int page;
        private int totalPage;


        public Announcements(int masterId, int tableId, int pageSize, int page) {
            this.pageSize = pageSize;
            this.page = page;
            this.list = listMaker(masterId,tableId,pageSize, page);

        }
        public List<Announcement> listMaker(int masterId, int tableId, int pageSize, int page){
            List<Announcement> list = new ArrayList<>();
            List<Announcement> temp = new ArrayList<>();
            if (masterId!=0 && tableId !=0){
                for (TimeTable timeTable : timeTableRepo.findAll()){
                    if (timeTable.getMaster().getId()==masterId){
                        list=timeTable.getAnnouncementList();
                    }
                }
            }else if (tableId!=0){
                list = timeTableRepo.findById(tableId).get().getAnnouncementList();
            }else if (masterId!=0){
                for (TimeTable timeTable : masterRepo.findById(masterId).get().getTimeTableList()){
//                    for (TimeTable timeTable : course.getTimeTableList()){
                        list.addAll(timeTable.getAnnouncementList());
//                    }
                }
            }else {
                for (TimeTable timeTable : timeTableRepo.findAll()){
                    list.addAll(timeTable.getAnnouncementList());
                }
            }

            int begin = (page-1)*pageSize==0? 0:(page-1)*pageSize;
            int end = page*pageSize;

            this.totalPage =(int) Math.ceil((double)list.size()/(double)pageSize);

            while (begin<end && list.size()>begin){
                temp.add(list.get(begin++));
            }

            return temp;
        }


        public List<Announcement> getList() {
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
