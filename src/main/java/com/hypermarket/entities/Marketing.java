package com.hypermarket.entities;

public class Marketing extends User {
    public Marketing(String recordLine) {
        super(recordLine);
    }

    public Marketing(String role, int id, String fName, String lName, String phone,
            String email, String password,
            double salary) {
        super(role, id, fName, lName, phone, email, password, salary);
    }
}
