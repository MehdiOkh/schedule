package com.user.schedule.database.service;

public class ResponseForm {
    private String status;
    private String message;
    private Object data;

    public ResponseForm(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
