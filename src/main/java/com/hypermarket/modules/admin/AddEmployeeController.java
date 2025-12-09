package com.hypermarket.modules.admin;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.User;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Optional;

public class AddEmployeeController {

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
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPassField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private Button saveBtn;

    private File selectedImageFile;

    private final DataStore dataStore = DataStore.getDataStore();

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll(
                "Admin",
                "Inventory",
                "Sales",
                "Marketing");

        roleComboBox.setPromptText("Select Role");
    }

    @FXML
    private void handleImageSelection(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg",
                "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);

        File file = fileChooser.showOpenDialog(uploadImageBtn.getScene().getWindow());

        if (file != null) {
            try {
                this.selectedImageFile = file;
                String imageUrl = file.toURI().toURL().toString();
                Image image = new Image(imageUrl);
                userImage.setImage(image);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        Optional<String> selectedRole = Optional.ofNullable(roleComboBox.getValue());

        int placeholderID = getNextAvailableID();

        User newUser = new User(
                selectedRole.get(),
                placeholderID,
                fnameField.getText().trim(),
                lnameField.getText().trim(),
                phoneField.getText().trim(),
                emailField.getText().trim(),
                passwordField.getText(),
                0.0);

        dataStore.getUsers().add(newUser);

        dataStore.saveAllData();

        System.out
                .println("New employee added and data saved to file: " + newUser.getFName() + " " + newUser.getLName());

        clearForm();
    }

    private int getNextAvailableID() {
        if (dataStore.getUsers().isEmpty()) {
            return 1;
        }
        return dataStore.getUsers().get(dataStore.getUsers().size() - 1).getID() + 1;
    }

    private boolean validateInput() {
        if (fnameField.getText().trim().isEmpty() ||
                lnameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                passwordField.getText().isEmpty() ||
                confirmPassField.getText().isEmpty()) {

            System.err.println("Validation Error: Please fill in all required fields.");
            return false;
        }

        if (!passwordField.getText().equals(confirmPassField.getText())) {
            System.err.println("Validation Error: Passwords do not match.");
            return false;
        }

        if (roleComboBox.getValue() == null) {
            System.err.println("Validation Error: Please select a Role.");
            return false;
        }

        return true;
    }

    private void clearForm() {
        fnameField.clear();
        lnameField.clear();
        phoneField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPassField.clear();
        roleComboBox.getSelectionModel().clearSelection();
        userImage.setImage(null);
        selectedImageFile = null;
    }
}