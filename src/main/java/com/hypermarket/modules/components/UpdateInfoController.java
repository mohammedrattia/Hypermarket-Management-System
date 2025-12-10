package com.hypermarket.modules.components;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
    private Button uploadImageBtn;

    private File selectedImageFile;

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
            currentUser.setImage(selectedImageFile.toURI().toString());
        }

        DataStore.getDataStore().saveAllData();
        new Alert(Alert.AlertType.INFORMATION, "User info updated successfully!").showAndWait();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        currentUser = com.hypermarket.service.Session.getInstance().getUser();

        if (currentUser != null) {
            fnameField.setText(currentUser.getFName());
            lnameField.setText(currentUser.getLName());
            phoneField.setText(currentUser.getPhone());
            emailField.setText(currentUser.getEmail());
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

    public javafx.scene.Parent getView() {
        return saveBtn.getScene().getRoot();
    }

    public class UpdateUserInfoView {

        private Parent view;

        public UpdateUserInfoView() {
            try {
                view = FXMLLoader.load(getClass().getResource("UpdateUserInfo.fxml"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public Parent getView() {
            return view;
        }
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
                currentUser.setImage(imageUrl);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

}
