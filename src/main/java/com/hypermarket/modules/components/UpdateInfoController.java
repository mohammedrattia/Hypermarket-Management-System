package com.hypermarket.modules.components;

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
        if (!newPassField.getText().equals(confirmPassField.getText())) {
            new Alert(Alert.AlertType.ERROR, "Passwords must match!").showAndWait();
            return;
        }

        currentUser.setFName(fnameField.getText());
        currentUser.setLName(lnameField.getText());
        currentUser.setPhone(phoneField.getText());
        currentUser.setEmail(emailField.getText());
        currentUser.setPassword(newPassField.getText());
        currentUser.setPassword(confirmPassField.getText());

        if (!newPassField.getText().isEmpty()) {
            currentUser.setPassword(newPassField.getText());
        }

        DataStore.getDataStore().saveAllData();

        new Alert(Alert.AlertType.INFORMATION,
                "User info updated successfully!").showAndWait();
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

}