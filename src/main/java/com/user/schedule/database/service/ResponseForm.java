package com.user.schedule.database.service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Objects;

public class ResponseForm {
    private String status;
    private String message;
    private Object data;

    public ResponseForm(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = Objects.requireNonNullElseGet(data, () -> new ObjectNode(JsonNodeFactory.instance));
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
