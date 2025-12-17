package com.hypermarket.modules.user;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hypermarket.entities.User;
import com.hypermarket.service.Session;
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
    private PasswordField newPassField;

    @FXML
    private PasswordField confirmPassField;

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
            File imageFile = new File(FileManager.IMAGE_PATH + currentUser.getImage());
            idField.setText(String.valueOf(currentUser.getID()));
            fnameField.setText(currentUser.getFName());
            lnameField.setText(currentUser.getLName());
            userImage.setImage(new Image(imageFile.toURI().toString()));
            phoneField.setText(currentUser.getPhone());
            emailField.setText(currentUser.getEmail());
            roleField.setText(currentUser.getRole().toString());
            salaryField.setText(String.valueOf(currentUser.getSalary()));
            confirmPassField.setText(currentUser.getPassword());
            newPassField.setText(currentUser.getPassword());
        }

        userImage.setFitWidth(150);
        userImage.setFitHeight(150);
        userImage.setPreserveRatio(false);

        Circle clip = new Circle();
        clip.setCenterX(75);
        clip.setCenterY(75);
        clip.setRadius(75);

        userImage.setClip(clip);

    }

    public void setOnUpdateImage(Runnable onUpdateImage) {
        System.out.println("DEBUG: Callback has been connected!");
        this.onUpdateImage = onUpdateImage;
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (currentUser == null) {
            System.out.println("Error: No user loaded!");
            return;
        }

        if (!newPassField.getText().equals(confirmPassField.getText())) {
            new Alert(Alert.AlertType.ERROR, "Passwords must match!").showAndWait();
            return;
        }

        currentUser.setFName(fnameField.getText());
        currentUser.setLName(lnameField.getText());
        currentUser.setPhone(phoneField.getText());
        currentUser.setEmail(emailField.getText());

        // Only update password if user entered a new one
        if (!newPassField.getText().trim().isEmpty()) {
            currentUser.setPassword(newPassField.getText());
        }

        // Save selected image
        if (selectedImageFile != null) {
            try {
                File userImageFile = new File(FileManager.IMAGE_PATH + currentUser.getImage());
                FileManager.copyImage(selectedImageFile, userImageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        onUpdateImage.run();

        DataStore.getDataStore().saveAllData();
        new Alert(Alert.AlertType.INFORMATION, "User info updated successfully!").showAndWait();
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
