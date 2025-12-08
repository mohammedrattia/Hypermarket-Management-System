package com.hypermarket.entities;

public class Admin extends User {
    public Admin(String recordLine) {
        super(recordLine);
    }

    public Admin(String role, int id, String fName, String lName, String phone,
            String email, String password,
            double salary) {
        super(role, id, fName, lName, phone, email, password, salary);
    }

    public void addUser(String role, int id, String fName, String lName, String phone, String email, String password,
            double salary) {

    }

    public void updateUser(int id, User user) {
    }

    public void deleteUser(int id) {

    }
}
