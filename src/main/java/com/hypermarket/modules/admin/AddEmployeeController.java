package com.hypermarket.modules.admin;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;
import com.hypermarket.entities.User;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPassField;
    @FXML
    private TextField salaryField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private Button saveBtn;

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
            File userImageFile = new File(FileManager.IMAGE_PATH + imageName);
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
                passwordField.getText(),
                Double.valueOf(salaryField.getText()));

        dataStore.getUsers().add(newUser);

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
        StringBuilder alertText = new StringBuilder();

        if (fnameField.getText().trim().isEmpty() ||
                lnameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                passwordField.getText().isEmpty() ||
                confirmPassField.getText().isEmpty()) {
            alertText.append("- Please fill in all required fields.\n");
        }

        if (!passwordField.getText().equals(confirmPassField.getText())) {
            alertText.append("- Passwords do not match.\n");
        }

        if (roleComboBox.getValue() == null) {
            alertText.append("- Please select a Role.\n");
        }

        if (selectedImageFile == null) {
            alertText.append("- Please select an image.\n");
        }

        try {
            Double.parseDouble(salaryField.getText().trim());
        } catch (NumberFormatException e) {
            alertText.append("- Please enter a valid salary.");
        }

        if (!alertText.isEmpty()) {
            makeAlert(alertText);
            return false;
        }

        return true;
    }

    private void makeAlert(StringBuilder alertText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Validation Error");
        alert.setContentText(alertText.toString());

        alert.showAndWait();
    }

    private void clearForm() {
        fnameField.clear();
        lnameField.clear();
        phoneField.clear();
        emailField.clear();
        salaryField.clear();
        passwordField.clear();
        confirmPassField.clear();
        roleComboBox.getSelectionModel().clearSelection();
        userImage.setImage(null);
        selectedImageFile = null;
    }

}