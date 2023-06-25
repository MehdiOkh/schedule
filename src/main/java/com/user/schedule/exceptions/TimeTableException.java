package com.user.schedule.exceptions;

public class TimeTableException {
    public static class StudentNotFound extends RuntimeException {
        public StudentNotFound(String message) {
            super(message);
        }
    }

    public static class TimeTableNotFound extends RuntimeException {
        public TimeTableNotFound(String message) {
            super(message);
        }
    }

}
