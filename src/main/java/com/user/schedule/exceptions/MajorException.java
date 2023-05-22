package com.user.schedule.exceptions;

public class MajorException {
    public static class MajorNotFoundException extends RuntimeException {
        public MajorNotFoundException(String message) {
            super(message);
        }

    }

}
