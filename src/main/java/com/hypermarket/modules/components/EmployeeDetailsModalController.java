package com.hypermarket.modules.components;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

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
import javafx.stage.Stage;

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

    private User currentUser;

    private Runnable onDeleteCallBack;

    public void setOnDeleteCallBack(Runnable callback) {
        this.onDeleteCallBack = callback;
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
    }

    @FXML
    private void handleUpdate() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/admin/UpdateEmployee.fxml"));
            Parent root = loader.load();

            UpdateEmployee controller = loader.getController();
            controller.setUserData(currentUser);

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
}
