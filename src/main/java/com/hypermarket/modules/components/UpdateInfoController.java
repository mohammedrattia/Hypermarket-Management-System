package com.hypermarket.modules.components;

import java.net.URL;
import java.util.ResourceBundle;

import com.hypermarket.entities.User;

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
    private void handleSave(ActionEvent event) {
        if (currentUser == null) {
            System.out.println("Error: No user loaded!");
            return;
        }

        currentUser.setFname(fnameField.getText());
        currentUser.setLname(lnameField.getText());
        currentUser.setPhone(phoneField.getText());
        currentUser.setEmail(emailField.getText());
        currentUser.setPassword(newPassField.getText());
        currentUser.setPassword(confirmPassField.getText());
        if (confirmPassField != newPassField) {
            System.out.println("Passwords must be identical!");

        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("User info has been updated successfully.");
        alert.showAndWait();

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // userImage.setImage(new Image("/com/hypermarket/images/tomato.jpg"));
        userImage.setFitWidth(150);
        userImage.setFitHeight(150);
        userImage.setPreserveRatio(false);

        // Create a circular clip
        Circle clip = new Circle();
        clip.setCenterX(75); // half of width
        clip.setCenterY(75); // half of height
        clip.setRadius(75);

        userImage.setClip(clip);

        // currentUser=new User();
    }

}