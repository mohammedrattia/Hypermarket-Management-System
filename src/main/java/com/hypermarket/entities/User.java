package com.hypermarket.entities;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;

public class User {
    private Role role;
    private int id;
    private String fName;
    private String lName;
    private String fullName;
    private String phone;
    private String email;
    private String password;
    private double salary;
    private int age;

    public User(String role, int id, String fName, String lName, String phone, String email,
            String password, double salary) {
        this.role = Role.valueOf(role.trim().toUpperCase());
        this.id = DataStore.getDataStore().getUsers().get(DataStore.getDataStore().getUsers().size()-1).getID();
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
        // return this.attribute01 + FileManager.delimeter + this.attribute02 + FileManager.delimeter + FileManager.dateFormat.format(this.attribute03) + FileManager.delimeter + FileManager.dateTimeFormat.format(this.attribute04) + FileManager.delimeter + this.attribute05.toString() + ....;
        // attribute03 type is Date (it has date only and time is set to 00:00:00)
        // attribute04 type is Date (it has both date and time) 
        // attribute05 type is Role (only for user to know his role) 
        return "ُExample";
    }

    private void parseString(String line) {
        String[] values = line.split(FileManager.delimeter);
        // Look at the following examples and make the parseString Function
        try {
            // this.attribute01 = values[0]; // Read String
            // this.attribute02 = Integer(values[1]); // Convert String to Int    // attribute02 is int
            // this.attribute03 = FileManager.dateFormat.parse(values[2]); // Read Date only    // attribute03 type is Date (it has date only and time is set to 00:00:00)
            // this.attribute04 = FileManager.dateTimeFormat.parse(values[3]); // Read Date + Time    // attribute04 type is Date (it has both date and time) 
            // this.attribute05 = Role.valueOf(values[4].toUpperCase().trim()); // Convert String to Role (must be UpperCase)    // attribute05 type is Role (only for user to know his role)
        } 
        catch (IllegalArgumentException e) {
            System.err.println("Error Chosing Role: " + e.getMessage());
        }
        catch (Exception e) {
            System.err.println("Error parsing data: " + e.getMessage());
        }
    }

    public String getRole() {
        return role.toString();
    }

    public void setRole(String role) {
        this.role = Role.valueOf(role.trim().toUpperCase());
    }

    public int getID() {
        return id;
    }

    public String getFname() {
        return fName;
    }

    public void setFname(String fName) {
        this.fName = fName;
        updateFullName();
    }

    public String getLname() {
        return lName;
    }

    public void setLname(String lName) {
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

        setFname(fName);
        setLname(lName);
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.role = Role.valueOf(role.trim().toUpperCase());
        this.salary = salary;
    }
}
