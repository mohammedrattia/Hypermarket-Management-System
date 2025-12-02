package com.hypermarket;

public class Employee {
    private String name;
    private String title;
    private String salary;
    private String phone;

    public Employee(String name, String title, String salary, String phone) {
        this.name = name;
        this.title = title;
        this.salary = salary;
        this.phone = phone;
    }

    public String getName() { 
        return name;
    }
    public String getTitle() { 
        return title;
    }
    public String getSalary() { 
        return salary;
    }
    public String getPhone() { 
        return phone;
    }
}
