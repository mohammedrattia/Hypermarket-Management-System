package com.hypermarket.service;

import com.hypermarket.data.*;
import com.hypermarket.entities.*;

public class Authenticator {
    public static void authenticate(String email, String password) {
        for (User user : DataStore.getDataStore().getUsers()) {
            if (user.getEmail() == email && user.getPassword() == password) {
                Session.getInstance().setUser(user);
            }
        }
    }

    public static void logout() {
        Session.getInstance().setUser(null);
    }

    public static void changePassword(String oldPass, String newPass) throws Exception {
        User currentUser = Session.getInstance().getUser();
        if (currentUser.getPassword() != oldPass) {
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
        currentUser.setPassword(newEmail);
    }
}
