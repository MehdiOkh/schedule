package com.user.schedule.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SocketModule {

    private final SocketIOServer server;
    private final List<SocketIOClient> clients = new ArrayList<>();

    public SocketModule(SocketIOServer server) {
        this.server = server;
        System.out.println("SOCKET");
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", Message.class, onChatReceived());
        server.addEventListener("offer_request", Message.class, onOfferRequest());
        server.addEventListener("offer", Message.class, onOffer());
        server.addEventListener("candidate", Message.class, onCandidate());
        server.addEventListener("answer", Message.class, onAnswer());

    }

    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
//            log.info(data.toString());
            for (SocketIOClient client : senderClient.getNamespace().getAllClients()
            ) {
                if (!senderClient.getSessionId().equals(client.getSessionId())) {
                    client.sendEvent("get_message", data.getMessage());
                }
            }

        };
    }

    private DataListener<Message> onOfferRequest() {
        return (senderClient, data, ackSender) -> {
//            log.info(data.toString());
            for (SocketIOClient client : clients
            ) {
                if (data.getMessage().equals(client.getSessionId().toString())) {
                    client.sendEvent("offer_request", senderClient.getSessionId());
                }
            }

        };
    }

    private DataListener<Message> onAnswer() {
        return (senderClient, data, ackSender) -> {
//            log.info(data.toString());
            for (SocketIOClient client : clients
            ) {
                final JSONObject message = new JSONObject(data.getMessage());
                if (message.getString("to").equals(client.getSessionId().toString())) {
                    client.sendEvent("answer", message.getString("answer"));
                }
            }

        };
    }

    private DataListener<Message> onCandidate() {
        return (senderClient, data, ackSender) -> {
//            log.info(data.toString());
            for (SocketIOClient client : clients
            ) {
                final JSONObject message = new JSONObject(data.getMessage());
                if (message.getString("to").equals(client.getSessionId().toString())) {
                    client.sendEvent("candidate", message.getString("candidate"));
                }
            }

        };
    }
    private DataListener<Message> onOffer() {
        return (senderClient, data, ackSender) -> {
//            log.info(data.toString());
            for (SocketIOClient client : clients
            ) {
                final JSONObject message = new JSONObject(data.getMessage());
                if (message.getString("to").equals(client.getSessionId().toString())) {
                    client.sendEvent("offer", message.getString("offer"));
                }
            }

        };
    }

    private ConnectListener onConnected() {
        return (client) -> {
//            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
            System.out.println("client connected!");
            if (!clients.contains(client)) {
                clients.add(client);
                System.out.println(client.getSessionId());
            }
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
//            log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
            System.out.println("client disconnected!");

        };
    }

}