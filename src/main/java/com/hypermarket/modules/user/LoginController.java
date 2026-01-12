package com.hypermarket.modules.user;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.util.ResourceBundle;

import org.kordamp.ikonli.javafx.FontIcon;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.User;
import com.hypermarket.service.*;
import com.hypermarket.service.Toast;

public class LoginController implements Initializable {

    @FXML
    private Button loginButton;

    @FXML
    private Button testAdminButton;
    @FXML
    private Button testInventoryButton;
    @FXML
    private Button testMarketingButton;
    @FXML
    private Button testSalesButton;

    @FXML
    private TextField emailField;
    @FXML
    private TextField passTextField;
    @FXML
    private FontIcon passToggleIcon;

    @FXML
    private PasswordField passField;

    @FXML
    void togglePasswordVisibility() {
        if (passField.isVisible()) {
            passField.setVisible(false);
            passField.setManaged(false);

            passTextField.setVisible(true);
            passTextField.setManaged(true);

            passToggleIcon.setIconLiteral("fas-eye-slash");
        } else {
            passTextField.setVisible(false);
            passTextField.setManaged(false);

            passField.setVisible(true);
            passField.setManaged(true);

            passToggleIcon.setIconLiteral("fas-eye");
        }
    }

    private Runnable onLoginSuccess;

    public void setOnLoginSuccess(Runnable handler) {
        this.onLoginSuccess = handler;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setDefaultButton(true);
        testCredential();
        passTextField.textProperty().bindBidirectional(passField.textProperty());
    }

    public void testCredential() {
        testAdminButton.setOnMouseClicked(event -> {
            testButton(testAdminButton);
            Toast.showNotification("This is Admin User Credentials");
        });
        testInventoryButton.setOnMouseClicked(event -> {
            testButton(testInventoryButton);
            Toast.showAlert("This is Inventory User Credentials", AlertType.WARNING);
        });
        testMarketingButton.setOnMouseClicked(event -> {
            testButton(testMarketingButton);
            Toast.showAlert("This is Marketing User Credentials", AlertType.ERROR);
        });
        testSalesButton.setOnMouseClicked(event -> {
            testButton(testSalesButton);
            Toast.showAlert("Are you sure?", AlertType.CONFIRMATION);
        });
    }

    public void testButton(Button button) {
        ObservableList<User> users = DataStore.getDataStore().getUsers();
        for (User user : users) {
            if (user.getRole().name().toLowerCase().equals(button.getText().toLowerCase())) {
                emailField.setText(user.getEmail());
                passField.setText(user.getPassword());
                return;
            }
        }
        System.out.println("Couldn't Find " + button.getText() + "!!");
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
            return;
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
