package com.hypermarket.service;

import com.hypermarket.data.*;
import com.hypermarket.entities.*;

public class Authenticator {
    public static void authenticate(String email, String password) throws Exception {
        for (User user : DataStore.getDataStore().getUsers()) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                Session.getInstance().setUser(user);
                return;
            }
        }
        throw new Exception("Invalid Credentials!");
    }

    public static void logout() {
        Session.getInstance().setUser(null);
    }

    public static void changePassword(String oldPass, String newPass) throws Exception {
        User currentUser = Session.getInstance().getUser();

        if (currentUser.getPassword().equals(oldPass)) {
            currentUser.setPassword(newPass);
        } else {
            throw new Exception("The old password is incorrect.");
        }
    }

    public static void changeEmail(String newEmail) throws Exception {
        User currentUser = Session.getInstance().getUser();
        if (currentUser == null) {
            throw new Exception("No session available.");
        }
        currentUser.setEmail(newEmail);
    }
}
