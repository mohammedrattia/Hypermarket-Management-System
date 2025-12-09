package com.hypermarket.entities;

import java.util.ArrayList;

import com.hypermarket.data.DataStore;

public class Admin extends User {
    public Admin(String recordLine) {
        super(recordLine);
    }

    public Admin(String role, int id, String fName, String lName, String phone, String email, String password,
            double salary) {
        super(role, id, fName, lName, phone, email, password, salary);
    }

    public void addUser(String role, int id, String fName, String lName, String phone, String email, String password,
            double salary) {
        User newUser;
        String normRole = role.trim().toUpperCase();

        switch (normRole) {
            case "ADMIN":
                newUser = new Admin(normRole, 0, fName, lName, phone, email, password, salary);
                break;
            case "SALES":
                newUser = new Sales(normRole, 0, fName, lName, phone, email, password, salary);
                break;
            case "MARKETING":
                newUser = new Marketing(normRole, 0, fName, lName, phone, email, password, salary);
                break;
            case "INVENTORY":
                newUser = new Inventory(normRole, 0, fName, lName, phone, email, password, salary);
                break;
            default:
                System.out.println("Unknown role " + role + " creating generic user for fallback");
                newUser = new User(normRole, 0, fName, lName, phone, email, password, salary);
        }

        DataStore.getDataStore().getUsers().add(newUser);
        DataStore.getDataStore().saveAllData();
        System.out.println("Employee Added Successfully");
    }

    public void updateUser(int id, User updatedInfo) {
        ArrayList<User> users = DataStore.getDataStore().getUsers();

        for (User user : users) {
            if (user.getID() == id) {
                user.setFName(updatedInfo.getFName());
                user.setLName(updatedInfo.getLName());
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
        ArrayList<User> users = DataStore.getDataStore().getUsers();
        boolean removed = users.removeIf(user -> user.getID() == id);

        if (removed) {
            DataStore.getDataStore().saveAllData();
            System.out.println("User deleted");
        }
    }
}
