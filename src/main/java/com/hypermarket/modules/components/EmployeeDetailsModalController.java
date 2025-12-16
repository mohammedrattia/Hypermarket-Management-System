package com.hypermarket.modules.components;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.NumberFormat;
import java.util.Locale;

import com.hypermarket.data.FileManager;
import com.hypermarket.entities.Admin;
import com.hypermarket.entities.User;
import com.hypermarket.modules.admin.UpdateEmployee;
import com.hypermarket.service.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;

public class EmployeeDetailsModalController {
    @FXML
    private Label modalNameLabel;
    @FXML
    private Label modalRoleLabel;
    @FXML
    private Label modalIdLabel;
    @FXML
    private Label modalEmailLabel;
    @FXML
    private Label modalPhoneLabel;
    @FXML
    private Label modalSalaryLabel;

    @FXML
    private ImageView popupImage;

    private User currentUser;

    private Runnable onDeleteCallBack;
    private Runnable onUpdateCallback;

    public void setOnDeleteCallBack(Runnable callback) {
        this.onDeleteCallBack = callback;
    }

    public void setOnUpdateCallback(Runnable callback) {
        this.onUpdateCallback = callback;
    }

    public void setUserData(User user) {
        this.currentUser = user;

        if (user != null) {
            modalNameLabel.setText(user.getFullName());
            modalRoleLabel.setText(user.getRole().toString());
            modalIdLabel.setText("ID: #" + user.getID());
            modalEmailLabel.setText(user.getEmail());
            modalPhoneLabel.setText(user.getPhone());

            NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
            modalSalaryLabel.setText(currency.format(user.getSalary()));
        }
        try {
            File imageFile = new File(FileManager.IMAGE_PATH + currentUser.getImage());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toURL().toString());
                popupImage.setImage(image);

                makeImageRound(popupImage, 80);
            }
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/admin/UpdateEmployee.fxml"));
            Parent root = loader.load();

            UpdateEmployee controller = loader.getController();
            controller.setUserData(this.currentUser);

            controller.setOnUpdateCallback(this.onUpdateCallback);

            Scene scene = modalNameLabel.getScene();
            scene.setRoot(root);

            Stage stage = (Stage) scene.getWindow();
            stage.sizeToScene();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Delete Employee: " + currentUser.getFullName() + "?");
        alert.setContentText("Are you sure? This action cannot be undone");

        alert.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {

                ((Admin) Session.getInstance().getUser()).deleteUser(currentUser.getID());

                System.out.println("User " + currentUser.getID() + " deleted");

                if (onDeleteCallBack != null) {
                    onDeleteCallBack.run();
                }

                closeModal();
            }
        });
    }

    private void closeModal() {
        Stage stage = (Stage) modalNameLabel.getScene().getWindow();
        stage.close();
    }

    private void makeImageRound(ImageView imageView, double size) {
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(false);

        Circle clip = new Circle(size / 2);
        clip.setCenterX(size / 2);
        clip.setCenterY(size / 2);

        imageView.setClip(clip);
    }
}
