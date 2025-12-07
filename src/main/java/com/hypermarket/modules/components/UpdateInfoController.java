package com.hypermarket.modules.components;

import com.hypermarket.entities.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UpdateInfoController {
    private User currentUser;

    @FXML
    private TextField emailField;

    @FXML
    private TextField fnameField;

    @FXML
    private TextField lnameField;

    @FXML
    private PasswordField confirmPassField;

    @FXML
    private TextField phoneField;

    @FXML
    private Button saveBtn;
    @FXML
    private PasswordField newPassField;

    @FXML
    private void handleSave(ActionEvent event) {
        if (currentUser == null) {
            System.out.println("Error: No user loaded!");
            return;
        }

        currentUser.setFName(fnameField.getText());
        currentUser.setLName(lnameField.getText());
        currentUser.setPhone(phoneField.getText());
        currentUser.setEmail(emailField.getText());
        currentUser.setPassword(newPassField.getText());
        currentUser.setPassword(confirmPassField.getText());
        if (confirmPassField != newPassField) {
            System.out.println("Passwords must be identical!");

        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("User info has been updated successfully.");
        alert.showAndWait();

    }
}