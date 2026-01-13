package com.hypermarket.modules.admin;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;
import com.hypermarket.entities.User;
import com.hypermarket.service.Toast;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.kordamp.ikonli.javafx.FontIcon;

public class AddEmployeeController implements Initializable {

    @FXML
    private ImageView userImage;
    @FXML
    private Button uploadImageBtn;
    @FXML
    private TextField fnameField;
    @FXML
    private TextField lnameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
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
    private TextField salaryField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private Button saveBtn;

    File defaultImageFile = new File(
            "src/main/resources/com/hypermarket/images/greenUser.png");
    Image defaultUserImage = new Image(defaultImageFile.toURI().toString());

    private File selectedImageFile;
    private int placeholderID;
    private String imageName;

    private final DataStore dataStore = DataStore.getDataStore();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roleComboBox.getItems().addAll(
                "Admin",
                "Inventory",
                "Sales",
                "Marketing");
        placeholderID = getNextAvailableID();
        imageName = "user_" + String.format("%03d", placeholderID) + ".png";
        roleComboBox.setPromptText("Select Role");

        userImage.setImage(defaultUserImage);
        selectedImageFile = defaultImageFile;
        passTextField.textProperty().bindBidirectional(passField.textProperty());
        confirmPassTextField.textProperty().bindBidirectional(confirmPassField.textProperty());
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
    private void handleImageSelection(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files",
                FileManager.IMAGEEXTENSIONS);
        fileChooser.getExtensionFilters().add(imageFilter);

        File file = fileChooser.showOpenDialog(uploadImageBtn.getScene().getWindow());

        if (file != null) {
            this.selectedImageFile = file;
            Image newImage = new Image(selectedImageFile.toURI().toString());
            userImage.setImage(newImage);
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        try {
            File userImageFile = new File(FileManager.USER_IMAGE_PATH + imageName);
            FileManager.copyImage(selectedImageFile, userImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        User newUser = new User(
                roleComboBox.getValue(),
                placeholderID,
                fnameField.getText().trim(),
                lnameField.getText().trim(),
                imageName,
                phoneField.getText().trim(),
                emailField.getText().trim(),
                passField.getText(),
                Double.valueOf(salaryField.getText()));

        dataStore.getUsers().add(newUser);
        String addEmployeeSuccessMsg = "Employee " + newUser.getFullName() + " added successfully.";
        Toast.showToast(addEmployeeSuccessMsg, Toast.NotificationType.INFORMATION);
        clearForm();
    }

    private int getNextAvailableID() {
        if (dataStore.getUsers().isEmpty()) {
            return 1;
        }
        return dataStore.getUsers().get(dataStore.getUsers().size() - 1).getID() + 1;
    }

    private boolean validateInput() {
        String errorMessage = "";
        if (fnameField.getText().isEmpty() || lnameField.getText().isEmpty() || phoneField.getText().isEmpty()
                || emailField.getText().isEmpty() || salaryField.getText().isEmpty()
                || passField.getText().isEmpty()
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
        } else if (roleComboBox.getValue() == null) {
            errorMessage = "No Role chosen.";
        }
        if (!errorMessage.isEmpty()) {
            Toast.showToast(errorMessage, Toast.NotificationType.ERROR);
            return false;
        }
        return true;
    }

    private void clearForm() {
        fnameField.clear();
        lnameField.clear();
        phoneField.clear();
        emailField.clear();
        salaryField.clear();
        passField.clear();
        confirmPassField.clear();
        roleComboBox.getSelectionModel().clearSelection();
        userImage.setImage(defaultUserImage);
        selectedImageFile = null;
    }

}