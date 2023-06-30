package com.user.schedule.socket;

//@Data
public class Message {
    private MessageType type;
    private String message;
//    private String room;

    public Message() {
    }

    public Message(MessageType type, String message) {
        this.type = type;
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
