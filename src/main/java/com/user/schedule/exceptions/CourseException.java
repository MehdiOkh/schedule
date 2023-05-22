package com.user.schedule.exceptions;

public class CourseException {
    public static class CourseNotFoundException extends RuntimeException {
        public CourseNotFoundException(String message) {
            super(message);
        }

    }
    public static class AnnouncementNotFound extends RuntimeException {
        public AnnouncementNotFound(String message) {
            super(message);
        }

    }
}
