package com.user.schedule.exceptions;

public class UnitPickException {
    public static class UnitNotFound extends RuntimeException {
        public UnitNotFound(String message) {
            super(message);
        }
    }
    public static class StudentNotFound extends RuntimeException {
        public StudentNotFound(String message) {
            super(message);
        }
    }
    public static class AccessTimeOver extends RuntimeException {
        public AccessTimeOver(String message) {
            super(message);
        }
    }
}
