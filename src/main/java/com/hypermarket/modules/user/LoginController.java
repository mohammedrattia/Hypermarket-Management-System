package com.hypermarket.modules.user;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

import com.hypermarket.service.*;

public class LoginController implements Initializable {

    @FXML
    private TextField emailField;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passField;

    private Runnable onLoginSuccess;

    public void setOnLoginSuccess(Runnable handler) {
        this.onLoginSuccess = handler;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setDefaultButton(true);
    }

    @FXML
    void onLoginButtonClick() {
        if (!validateInput()) {
            return;
        }
        try {
            Authenticator.authenticate(emailField.getText(), passField.getText());
        } catch (Exception e) {
            makeAlert(e.getMessage());
        }
        if (onLoginSuccess != null) {
            onLoginSuccess.run();
        }
    }

    private boolean validateInput() {

        if (passField.getText().isEmpty() ||
                emailField.getText().isEmpty()) {
            makeAlert("- Please fill in all required fields.\n");
            return false;
        }

        return true;
    }

    private void makeAlert(String alertText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Validation Error");
        alert.setContentText(alertText.toString());

        alert.showAndWait();
    }
}
