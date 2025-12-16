package com.hypermarket.modules.components;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.NumberFormat;
import java.util.Locale;

import com.hypermarket.data.FileManager;
import com.hypermarket.entities.User;

import javafx.scene.shape.Circle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    @FXML
    private ImageView userImage;

    private User currentUser;

    private Runnable onDeleteAction;
    private Runnable onUpdateAction;

    public void setOnDeleteAction(Runnable action) {
        this.onDeleteAction = action;
    }

    public void setonUpdateAction(Runnable action) {
        this.onUpdateAction = action;
    }

    public void setData(User user) {
        this.currentUser = user;
        nameLabel.setText(user.getFullName());
        titleLabel.setText(user.getRole().toString());
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
        salaryLabel.setText("Salary: " + currency.format(user.getSalary()));
        phoneLabel.setText(user.getPhone());
        emailLabel.setText(user.getEmail());
        try {
            File imageFile = new File(FileManager.IMAGE_PATH + currentUser.getImage());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toURL().toString());
                userImage.setImage(image);

                makeImageRound(userImage, 80);
            }
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
        }
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
            controller.setOnUpdateCallback(this.onUpdateAction);

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initStyle(StageStyle.UTILITY);
            modalStage.setTitle("Employee Details");
            modalStage.setScene(new Scene(modalView));

            modalStage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error loading modal");
        }
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
