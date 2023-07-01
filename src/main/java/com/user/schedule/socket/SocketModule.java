package com.user.schedule.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.user.schedule.database.model.User;
import com.user.schedule.database.service.UsersService;
import com.user.schedule.security.service.JwtUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SocketModule {

    private final SocketIOServer server;
    private final List<SocketIOClient> clients = new ArrayList<>();
    private final List<User> clientsName = new ArrayList<>();
    private final JwtUtil jwtTokenUtil;
    private final UsersService usersService;
    private SocketIOClient master;


    public SocketModule(SocketIOServer server, JwtUtil jwtTokenUtil, UsersService usersService) {
        this.server = server;
        this.jwtTokenUtil = jwtTokenUtil;
        this.usersService = usersService;
        System.out.println("SOCKET");
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", Message.class, onChatReceived());
        server.addEventListener("offer_request", Message.class, onOfferRequest());
        server.addEventListener("offer", Message.class, onOffer());
        server.addEventListener("candidate", Message.class, onCandidate());
        server.addEventListener("answer", Message.class, onAnswer());
        server.addEventListener("get_master_id", Message.class, getMasterId());
        server.addEventListener("join_master", Message.class, joinMaster());

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

    private DataListener<Message> getMasterId() {
        return (senderClient, data, ackSender) -> {

            senderClient.sendEvent("master_id", master.getSessionId());

        };
    }

    private DataListener<Message> joinMaster() {
        return (senderClient, data, ackSender) -> {
            master = senderClient;
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
            String authorization = client.getHandshakeData().getSingleUrlParam("tkn");
            try {
                String userCode;
                if (authorization.startsWith("Bearer ")) {
                    userCode = jwtTokenUtil.extractUsername(authorization.substring(7));
                } else {
                    userCode = jwtTokenUtil.extractUsername(authorization);
                }
                User user = usersService.findByCode(userCode);
                System.out.println(user.getLastName());
                if (!clientsName.contains(user)) {

                    System.out.println("ADD: " + user.getLastName());
                    clientsName.add(user);
                }
                List<String> names = new ArrayList<>();
                for (User usr : clientsName
                ) {
                    names.add(usr.getFirstName() + " " + usr.getLastName());
                }
                server.getBroadcastOperations().sendEvent("members", names.stream().distinct().collect(Collectors.toList()));

            } catch (Exception e) {
                System.out.println(e);
            }

        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
//            log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
            System.out.println("client disconnected!");
            clients.removeIf(c -> client.getSessionId().equals(c.getSessionId()));
            String authorization = client.getHandshakeData().getSingleUrlParam("tkn");
            try {
                String userCode;
                if (authorization.startsWith("Bearer ")) {
                    userCode = jwtTokenUtil.extractUsername(authorization.substring(7));
                } else {
                    userCode = jwtTokenUtil.extractUsername(authorization);
                }
                User user = usersService.findByCode(userCode);
//                if (!clients.contains(client)) {
                System.out.println("remove: " + user.getLastName());

                clientsName.removeIf(cl -> cl.getLastName().equals(user.getLastName()));
                List<String> names = new ArrayList<>();
                for (User usr : clientsName
                ) {
                    names.add(usr.getFirstName() + " " + usr.getLastName());
                }
                server.getBroadcastOperations().sendEvent("members", names.stream().distinct().collect(Collectors.toList()));
//                }

            } catch (Exception e) {
                System.out.println(e);
            }
        };
    }

}