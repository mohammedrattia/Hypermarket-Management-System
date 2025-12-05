package com.hypermarket.modules.components;

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
    @FXML
    private Label emailLabel;

    public void setData(String name, String title, double salary, String phone, String email) {
        nameLabel.setText(name);
        titleLabel.setText(title);
        salaryLabel.setText("Salary: $" + salary);
        phoneLabel.setText("Phone: " + phone);
        emailLabel.setText("Email: " + email);
    }

}
