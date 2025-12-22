package com.hypermarket.entities;

import java.io.File;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;

import javafx.collections.ObservableList;

public class Admin extends User {
    public Admin(String recordLine) {
        super(recordLine);
    }

    public Admin(String role, int id, String fName, String lName, String image, String phone, String email,
            String password,
            double salary) {
        super(role, id, fName, lName, image, phone, email, password, salary);
    }

    public void addUser(String role, int id, String fName, String lName, String image, String phone, String email,
            String password,
            double salary) {
        User newUser;
        String normRole = role.trim().toUpperCase();

        switch (normRole) {
            case "ADMIN":
                newUser = new Admin(normRole, 0, fName, lName, image, phone, email, password, salary);
                break;
            case "SALES":
                newUser = new Sales(normRole, 0, fName, lName, image, phone, email, password, salary);
                break;
            case "MARKETING":
                newUser = new Marketing(normRole, 0, fName, lName, image, phone, email, password, salary);
                break;
            case "INVENTORY":
                newUser = new Inventory(normRole, 0, fName, lName, image, phone, email, password, salary);
                break;
            default:
                System.out.println("Unknown role " + role + " creating generic user for fallback");
                newUser = new User(normRole, 0, fName, lName, image, phone, email, password, salary);
        }

        DataStore.getDataStore().getUsers().add(newUser);
        DataStore.getDataStore().saveAllData();
        System.out.println("Employee Added Successfully");
    }

    public void updateUser(int id, User updatedInfo) {
        ObservableList<User> users = DataStore.getDataStore().getUsers();

        for (User user : users) {
            if (user.getID() == id) {
                user.setFName(updatedInfo.getFName());
                user.setLName(updatedInfo.getLName());
                user.setImage(updatedInfo.getImage());
                user.setPhone(updatedInfo.getPhone());
                user.setEmail(updatedInfo.getEmail());
                user.setPassword(updatedInfo.getPassword());
                user.setRole(updatedInfo.getRole());
                user.setSalary(updatedInfo.getSalary());

                DataStore.getDataStore().saveAllData();
                System.out.println("User " + id + " updated");
                return;
            }
        }
    }

    public void deleteUser(int id) {
        ObservableList<User> users = DataStore.getDataStore().getUsers();
        User userToDelete = null;

        for (User user : users) {
            if (user.getID() == id) {
                userToDelete = user;
                break;
            }
        }
        if (userToDelete != null) {
            deleteUserPhoto(userToDelete.getImage());

            users.remove(userToDelete);
        } else {
            System.out.println("User not found.");
        }
    }

    private void deleteUserPhoto(String imageName) {
        try {
            File file = new File(FileManager.USER_IMAGE_PATH + imageName);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Image file deleted: " + imageName);
                } else {
                    System.err.println("Failed to delete image file: " + imageName);
                }
            }
        } catch (Exception e) {
            System.err.println("Error deleting photo: " + e.getMessage());
        }
    }
}
