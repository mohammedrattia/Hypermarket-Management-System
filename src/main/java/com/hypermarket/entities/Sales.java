package com.hypermarket.entities;

public class Sales extends User {

    public Sales(String recordLine) {
        super(recordLine);
    }

    public Sales(String role, int id, String fName, String lName, String phone,
            String email, String password,
            double salary) {
        super(role, id, fName, lName, phone, email, password, salary);
    }
}
