package com.user.schedule.controller.handlers;

import com.user.schedule.database.service.ResponseForm;
import com.user.schedule.exceptions.CourseException;
import com.user.schedule.exceptions.DayAndBellException;
import com.user.schedule.exceptions.MajorException;
import com.user.schedule.exceptions.UnitPickException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler
    public ResponseEntity<ResponseForm> majorNotFoundHandler(MajorException.MajorNotFoundException exception) {
        ResponseForm response = new ResponseForm("error", exception.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseForm> courseNotFound(CourseException.CourseNotFoundException exception) {
        ResponseForm response = new ResponseForm("error", exception.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseForm> dayNotFound(DayAndBellException.DayNotFoundException exception) {
        ResponseForm response = new ResponseForm("error", exception.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseForm> bellNotFound(DayAndBellException.BellNotFoundException exception) {
        ResponseForm response = new ResponseForm("error", exception.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseForm> unitNotFound(UnitPickException.UnitNotFound exception) {
        ResponseForm response = new ResponseForm("error", exception.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseForm> accessTimeOver(UnitPickException.AccessTimeOver exception) {
        ResponseForm response = new ResponseForm("error", exception.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseForm> studentNotFound(UnitPickException.StudentNotFound exception) {
        ResponseForm response = new ResponseForm("error", exception.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseForm> announcementNotFound(CourseException.AnnouncementNotFound exception) {
        ResponseForm response = new ResponseForm("error", exception.getMessage(), null);
        System.out.println("ehy");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
