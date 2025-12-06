package com.hypermarket.entities;

public class User {
    private String role;
    private int id;
    private String fName;
    private String lName;
    private String fullName;
    private String phone;
    private String email;
    private String password;
    private double salary;

    public User(String role, int id, String fName, String lName, String phone, String email,
            String password, double salary) {
        this.role = role;
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        updateFullName();
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.salary = salary;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
        this.role = role;
        this.salary = salary;
    }

}
