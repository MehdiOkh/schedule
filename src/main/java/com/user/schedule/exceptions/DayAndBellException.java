package com.user.schedule.exceptions;

public class DayAndBellException {
    public static class DayNotFoundException extends RuntimeException {
        public DayNotFoundException(String message) {
            super(message);
        }
    }
    public static class BellNotFoundException extends RuntimeException {
        public BellNotFoundException(String message) {
            super(message);
        }
    }
}
