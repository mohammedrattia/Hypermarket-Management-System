package com.hypermarket.modules.user;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import com.hypermarket.service.*;

public class LoginController {

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

    @FXML
    void onLoginButtonClick() {
        try {
            Authenticator.authenticate(emailField.getText(), passField.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (onLoginSuccess != null) {
            onLoginSuccess.run();
        }
    }

}
