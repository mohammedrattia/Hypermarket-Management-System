package com.hypermarket.modules.admin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import org.kordamp.ikonli.javafx.FontIcon;

import com.hypermarket.entities.Role;
import com.hypermarket.entities.User;
import com.hypermarket.service.Toast;
import com.hypermarket.data.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UpdateEmployee implements Initializable {
    private User user;

    @FXML
    private ImageView userImage;

    @FXML
    private TextField fnameField;

    @FXML
    private TextField lnameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField passField;
    @FXML
    private TextField passTextField;
    @FXML
    private FontIcon passToggleIcon;
    @FXML
    private PasswordField confirmPassField;
    @FXML
    private TextField confirmPassTextField;
    @FXML
    private FontIcon confirmPassToggleIcon;

    @FXML
    private TextField idField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private TextField salaryField;

    @FXML
    private Button saveBtn;

    @FXML
    private Button uploadImageBtn;

    private File selectedImageFile;

    private Runnable onUpdateCallback;

    public void setOnUpdateCallback(Runnable onUpdateCallback) {
        this.onUpdateCallback = onUpdateCallback;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        roleComboBox.getItems().addAll(
                "Admin",
                "Inventory",
                "Sales",
                "Marketing");

        userImage.setFitWidth(150);
        userImage.setFitHeight(150);
        userImage.setPreserveRatio(false);

        Circle clip = new Circle();
        clip.setCenterX(75);
        clip.setCenterY(75);
        clip.setRadius(75);

        userImage.setClip(clip);
        passTextField.textProperty().bindBidirectional(passField.textProperty());
        confirmPassTextField.textProperty().bindBidirectional(confirmPassField.textProperty());
    }

    public void setUserData(User user) {
        this.user = user;

        if (user != null) {
            File imageFile = new File(FileManager.USER_IMAGE_PATH + user.getImage());
            idField.setText(String.valueOf(user.getID()));
            fnameField.setText(user.getFName());
            lnameField.setText(user.getLName());
            userImage.setImage(new Image(imageFile.toURI().toString()));
            phoneField.setText(user.getPhone());
            emailField.setText(user.getEmail());
            roleComboBox.setValue(user.getRole().toString());
            salaryField.setText(String.valueOf((int) user.getSalary()));
            confirmPassField.setText(user.getPassword());
            passField.setText(user.getPassword());
        }
    }

    @FXML
    void togglePasswordVisibility() {
        if (passField.isVisible()) {
            passField.setVisible(false);
            passField.setManaged(false);

            passTextField.setVisible(true);
            passTextField.setManaged(true);

            passTextField.requestFocus();
            passTextField.selectEnd();

            passToggleIcon.setIconLiteral("fas-eye-slash");
        } else {
            passTextField.setVisible(false);
            passTextField.setManaged(false);

            passField.setVisible(true);
            passField.setManaged(true);

            passTextField.requestFocus();
            passTextField.selectEnd();

            passToggleIcon.setIconLiteral("fas-eye");
        }
    }

    @FXML
    void toggleConfirmPasswordVisibility() {
        if (confirmPassField.isVisible()) {
            confirmPassField.setVisible(false);
            confirmPassField.setManaged(false);

            confirmPassTextField.setVisible(true);
            confirmPassTextField.setManaged(true);

            passTextField.requestFocus();
            passTextField.selectEnd();

            confirmPassToggleIcon.setIconLiteral("fas-eye-slash");
        } else {
            confirmPassTextField.setVisible(false);
            confirmPassTextField.setManaged(false);

            confirmPassField.setVisible(true);
            confirmPassField.setManaged(true);

            passTextField.requestFocus();
            passTextField.selectEnd();

            confirmPassToggleIcon.setIconLiteral("fas-eye");
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInput()) {
            return;
        }
        user.setFName(fnameField.getText());
        user.setLName(lnameField.getText());
        user.setPhone(phoneField.getText());
        user.setEmail(emailField.getText());
        user.setRole(Role.valueOf(roleComboBox.getValue().toUpperCase()));
        user.setSalary(Double.parseDouble(salaryField.getText()));

        if (!passField.getText().trim().isEmpty()) {
            user.setPassword(passField.getText());
        }

        if (selectedImageFile != null) {
            try {
                File userImageFile = new File(FileManager.USER_IMAGE_PATH + user.getImage());
                FileManager.copyImage(selectedImageFile, userImageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        new Alert(Alert.AlertType.INFORMATION, "User info updated successfully!").showAndWait();

        if (onUpdateCallback != null) {
            onUpdateCallback.run();
        }

        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    private boolean validateInput() {
        String errorMessage = "";
        if (user == null) {
            System.out.println("Error: No user loaded!");
            return false;
        } else if (fnameField.getText().isEmpty() || lnameField.getText().isEmpty() || phoneField.getText().isEmpty()
                || emailField.getText().isEmpty() || salaryField.getText().isEmpty() || passField.getText().isEmpty()
                || confirmPassField.getText().isEmpty()) {
            errorMessage = "Please fill in all required fields.";
        } else if (!fnameField.getText().matches("^\\D{1,}$") || !lnameField.getText().matches("^\\D{1,}$")) {
            errorMessage = "Invalid name.";
        } else if (!phoneField.getText().matches("^\\d{7,15}$")) {
            errorMessage = "Invalid phone number.";
        } else if (!emailField.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$")) {
            errorMessage = "Invalid Email.";
        } else if (!salaryField.getText().matches("^\\d{5,7}$")) {
            errorMessage = "Invalid salary entered.";
        } else if (!passField.getText().matches("^[^\\s]{8,}$")) {
            errorMessage = "Password must be at least 8 characters long.";
        } else if (!passField.getText().equals(confirmPassField.getText())) {
            errorMessage = "Passwords must match!";
        }
        if (!errorMessage.isEmpty()) {
            Toast.showToast(errorMessage, Toast.NotificationType.ERROR);
            return false;
        }
        return true;
    }

    @FXML
    private void handleImageSelection(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files",
                FileManager.IMAGEEXTENSIONS);
        fileChooser.getExtensionFilters().add(imageFilter);

        File file = fileChooser.showOpenDialog(uploadImageBtn.getScene().getWindow());

        if (file != null) {
            try {
                this.selectedImageFile = file;
                String imageUrl = selectedImageFile.toURI().toURL().toString();
                Image image = new Image(imageUrl);
                userImage.setImage(image);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

}
