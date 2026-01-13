package com.hypermarket.modules.user;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.kordamp.ikonli.javafx.FontIcon;

import com.hypermarket.entities.User;
import com.hypermarket.service.Session;
import com.hypermarket.service.Toast;
import com.hypermarket.data.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

public class UpdateInfoController implements Initializable {
    private User currentUser;

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
    private TextField roleField;

    @FXML
    private TextField salaryField;

    @FXML
    private Button saveBtn;

    @FXML
    private Button uploadImageBtn;

    private File selectedImageFile;
    private Runnable onUpdateImage;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        currentUser = Session.getInstance().getUser();

        if (currentUser != null) {
            File imageFile = new File(FileManager.USER_IMAGE_PATH + currentUser.getImage());
            idField.setText(String.valueOf(currentUser.getID()));
            fnameField.setText(currentUser.getFName());
            lnameField.setText(currentUser.getLName());
            userImage.setImage(new Image(imageFile.toURI().toString()));
            phoneField.setText(currentUser.getPhone());
            emailField.setText(currentUser.getEmail());
            roleField.setText(currentUser.getRole().toString());
            salaryField.setText(String.valueOf(currentUser.getSalary()));
            confirmPassField.setText(currentUser.getPassword());
            passField.setText(currentUser.getPassword());
        }

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

    public void setOnUpdateImage(Runnable onUpdateImage) {
        this.onUpdateImage = onUpdateImage;
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
        if (currentUser == null) {
            System.out.println("Error: No user loaded!");
            return;
        }

        if (!passField.getText().equals(confirmPassField.getText())) {
            new Alert(Alert.AlertType.ERROR, "Passwords must match!").showAndWait();
            return;
        }

        currentUser.setFName(fnameField.getText());
        currentUser.setLName(lnameField.getText());
        currentUser.setPhone(phoneField.getText());
        currentUser.setEmail(emailField.getText());

        if (!passField.getText().trim().isEmpty()) {
            currentUser.setPassword(passField.getText());
        }

        if (selectedImageFile != null) {
            try {
                File userImageFile = new File(FileManager.USER_IMAGE_PATH + currentUser.getImage());
                FileManager.copyImage(selectedImageFile, userImageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        onUpdateImage.run();

        DataStore.getDataStore().saveAllData();
        String updateUserInfoSuccessMsg = "User info updated successfully!";
        Toast.showToast(updateUserInfoSuccessMsg, Toast.NotificationType.INFORMATION);
    }

    private boolean validateInput() {
        String errorMessage = "";
        if (fnameField.getText().isEmpty() || lnameField.getText().isEmpty() || phoneField.getText().isEmpty()
                || emailField.getText().isEmpty() || salaryField.getText().isEmpty() || passField.getText().isEmpty()
                || confirmPassField.getText().isEmpty()) {
            errorMessage = "Please fill in all required fields.";
        } else if (!fnameField.getText().matches("^\\D{1,}$") || !lnameField.getText().matches("^\\D{1,}$")) {
            errorMessage = "Invalid name.";
        } else if (!phoneField.getText().matches("^\\d{7,15}$")) {
            errorMessage = "Invalid phone number.";
        } else if (!emailField.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$")) {
            errorMessage = "Invalid Email.";
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

    public javafx.scene.Parent getView() {
        return saveBtn.getScene().getRoot();
    }

    @FXML
    private void handleImageSelection(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);

        File file = fileChooser.showOpenDialog(uploadImageBtn.getScene().getWindow());

        if (file != null) {
            this.selectedImageFile = file;
            String imageUrl = file.toURI().toString();
            Image image = new Image(imageUrl);

            userImage.setImage(image);
        }
    }

}
