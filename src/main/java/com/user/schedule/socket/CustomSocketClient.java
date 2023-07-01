package com.user.schedule.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.user.schedule.database.model.User;

public class CustomSocketClient extends User {
    private SocketIOClient socketIOClient;

    public CustomSocketClient(String firstName, String lastName, String password, String role, String code, SocketIOClient socketIOClient) {
        super(firstName, lastName, password, role, code);
        this.socketIOClient = socketIOClient;
    }

    public SocketIOClient getSocketIOClient() {
        return socketIOClient;
    }
}
