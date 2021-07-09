package com.user.schedule.security;

import com.user.schedule.database.model.User;

public class AuthenticateResponse {
    private String token;
    private String expireAt;
    private User user;

    public AuthenticateResponse(String token, String expireAt, User user) {
        this.token = token;
        this.expireAt = expireAt;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public String getExpireAt() {
        return expireAt;
    }

    public User getUser() {
        return user;
    }

}
