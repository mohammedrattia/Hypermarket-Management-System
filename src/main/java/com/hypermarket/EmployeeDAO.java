package com.hypermarket;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public List<Employee> getAllEmployees() {
        List<Employee> ls = new ArrayList<>();

        ls.add(new Employee("Hana AbdelHamid", "Fullstack Engineer", "90000", "+20 102 334"));
        ls.add(new Employee("Hana AbdelHamid", "Fullstack Engineer", "90000", "+20 102 334"));
        ls.add(new Employee("Hana AbdelHamid", "Fullstack Engineer", "90000", "+20 102 334"));
        ls.add(new Employee("Hana AbdelHamid", "Fullstack Engineer", "90000", "+20 102 334"));
        ls.add(new Employee("Hana AbdelHamid", "Fullstack Engineer", "90000", "+20 102 334"));

        return ls;
    }
}