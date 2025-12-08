package com.hypermarket.entities;

import java.util.ArrayList;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;

public class User {
    private int ID;
    private String fName;
    private String lName;
    private String fullName;
    private Role role;
    private String phone;
    private String email;
    private String password;
    private double salary;

    public User(String role, int id, String fName, String lName, String phone, String email,
            String password, double salary) {
        try {
            this.role = Role.valueOf(role.trim().toUpperCase());
        } catch (Exception ex) {
            this.role = Role.SALES;
        }

        ArrayList<User> users = DataStore.getDataStore().getUsers();
        if (users.isEmpty()) {
            this.ID = 1;
        } else {
            this.ID = users.get(users.size() - 1).getID() + 1;
        }

        this.fName = fName;
        this.lName = lName;
        updateFullName();
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.salary = salary;
    }

    public User(String recordLine) {
        parseString(recordLine);
    }

    @Override
    public String toString() {
        return this.ID + FileManager.DELIMETER
                + this.fName + FileManager.DELIMETER
                + this.lName + FileManager.DELIMETER
                + this.role.toString() + FileManager.DELIMETER
                + this.phone + FileManager.DELIMETER
                + this.email + FileManager.DELIMETER
                + this.password
                + this.salary;
    }

    private void parseString(String line) {
        String[] values = line.split(FileManager.DELIMETER);

        try {
            this.ID = Integer.parseInt(values[0]);
            this.fName = values[1];
            this.lName = values[2];
            this.updateFullName();
            this.role = Role.valueOf(values[3].toUpperCase().trim());
            this.phone = values[4];
            this.email = values[5];
            this.password = values[6];

            if (values.length > 7) {
                this.salary = Double.parseDouble(values[7]);
            } else {
                this.salary = 0.0;
            }

        } catch (NumberFormatException e) {
            System.err.println("Error parsing ID (not a number): " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error Choosing Role (enum not found): " + values[4]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error parsing data: Line is missing fields.");
        } catch (Exception e) {
            System.err.println("Error parsing data: " + e.getMessage());
        }
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getID() {
        return ID;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
        updateFullName();
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
        updateFullName();
    }

    public String getFullName() {
        return fullName;
    }

    private void updateFullName() {
        this.fullName = this.fName + " " + this.lName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void updateInfo(String fName, String lName,
            String phone, String email, String password,
            String role, double salary) {

        setFName(fName);
        setLName(lName);
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.role = Role.valueOf(role.trim().toUpperCase());
        this.salary = salary;
    }
}
