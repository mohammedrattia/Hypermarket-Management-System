package com.hypermarket;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EmployeeCardController {
    @FXML
    private Label nameLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label salaryLabel;

    @FXML
    private Label phoneLabel;
    

    public void setData(Employee employee){
        nameLabel.setText(employee.getName());
        titleLabel.setText(employee.getTitle());
        salaryLabel.setText("Salary: $" + employee.getSalary());
        phoneLabel.setText("Phone: " + employee.getPhone());
    }
}
