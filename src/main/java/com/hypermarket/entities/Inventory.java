package com.hypermarket.entities;

public class Inventory extends User {
    public Inventory(String recordLine) {
        super(recordLine);
    }

    public Inventory(String role, int id, String fName, String lName, String phone,
            String email, String password,
            double salary) {
        super(role, id, fName, lName, phone, email, password, salary);
    }
}
