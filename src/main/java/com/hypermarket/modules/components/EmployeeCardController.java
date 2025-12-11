package com.hypermarket.modules.components;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import com.hypermarket.entities.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EmployeeCardController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label salaryLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label emailLabel;

    private User currentUser;

    private Runnable onDeleteAction;

    public void setOnDeleteAction(Runnable action) {
        this.onDeleteAction = action;
    }

    public void setData(User user) {
        this.currentUser = user;
        nameLabel.setText(user.getFullName());
        titleLabel.setText(user.getRole().toString());
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
        salaryLabel.setText("Salary: " + currency.format(user.getSalary()));
        phoneLabel.setText(user.getPhone());
        emailLabel.setText(user.getEmail());
    }

    @FXML
    private void handleShowDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/EmployeeDetailsModal.fxml"));
            Parent modalView = loader.load();

            EmployeeDetailsModalController controller = loader.getController();
            controller.setUserData(this.currentUser);

            controller.setOnDeleteCallBack(this.onDeleteAction);

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initStyle(StageStyle.UTILITY);
            modalStage.setTitle("Employee Details");
            modalStage.setScene(new Scene(modalView));

            modalStage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error loading modal");
        }
    }

}
