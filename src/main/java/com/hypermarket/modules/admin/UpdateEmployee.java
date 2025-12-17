package com.hypermarket.modules.admin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hypermarket.entities.User;
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
    private PasswordField newPassField;

    @FXML
    private PasswordField confirmPassField;

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

    }

    public void setUserData(User user) {
        this.user = user;

        if (user != null) {
            File imageFile = new File(FileManager.IMAGE_PATH + user.getImage());
            idField.setText(String.valueOf(user.getID()));
            fnameField.setText(user.getFName());
            lnameField.setText(user.getLName());
            userImage.setImage(new Image(imageFile.toURI().toString()));
            phoneField.setText(user.getPhone());
            emailField.setText(user.getEmail());
            roleComboBox.setValue(user.getRole().toString());
            salaryField.setText(String.valueOf(user.getSalary()));
            confirmPassField.setText(user.getPassword());
            newPassField.setText(user.getPassword());
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (user == null) {
            System.out.println("Error: No user loaded!");
            return;
        }

        if (!newPassField.getText().equals(confirmPassField.getText())) {
            new Alert(Alert.AlertType.ERROR, "Passwords must match!").showAndWait();
            return;
        }

        user.setFName(fnameField.getText());
        user.setLName(lnameField.getText());
        user.setPhone(phoneField.getText());
        user.setEmail(emailField.getText());

        // Only update password if user entered a new one
        if (!newPassField.getText().trim().isEmpty()) {
            user.setPassword(newPassField.getText());
        }

        if (selectedImageFile != null) {
            try {
                File userImageFile = new File(FileManager.IMAGE_PATH + user.getImage());
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

    @FXML
    private void handleImageSelection(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png");
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
