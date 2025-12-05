package com.hypermarket.entities;

public class Admin extends User{
    public Admin(Role role, int id, String fname, String lname,String phone, String email, String password, double salary){
        super(role,id,fname,lname,phone,email,password,salary);
    }

    public void addUser(Role role, int id, String fname, String lname,String phone, String email, String password, double salary){

    }

    public void updateUser(int id, User user){
    }

    public void deleteUser(int id){

    }
}
