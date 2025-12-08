package com.hypermarket.service;

import com.hypermarket.entities.*;

public class Session {
    private static Session instance;
    private User user;

    Session() {

    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
