package com.hypermarket.modules.admin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hypermarket.data.FileManager;
import com.hypermarket.entities.User;
import com.hypermarket.service.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

public class AdminViewController implements Initializable {

    @FXML
    private AnchorPane contentArea;

    @FXML
    private Label pageTitle;

    @FXML
    private Label menuDashboard;

    @FXML
    private Label menuEmployees;

    @FXML
    private Label menuAddEmployees;

    @FXML
    private Label menuUpdateUserInfo;

    @FXML
    private Label menuLogout;

    @FXML
    private HBox menuDashboardItem;

    @FXML
    private HBox menuEmployeesItem;

    @FXML
    private HBox menuAddEmployeesItem;

    @FXML
    private HBox menuUpdateUserInfoItem;

    @FXML
    private ImageView userImage;

    Runnable onLogout;

    private DashboardHome dashboardHome;
    private EmployeeGrid employeeGrid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User currentUser = Session.getInstance().getUser();
        showDashboard();
        setUpNavigation();
        updateTitleAndActiveTab(menuDashboard);

        try {
            File imageFile = new File(FileManager.IMAGE_PATH + currentUser.getImage());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toURL().toString());
                userImage.setImage(image);
               
                userImage.setPreserveRatio(false);
                Circle clip = new Circle();
                clip.setCenterX(25);
                clip.setCenterY(25);
                clip.setRadius(25);

                userImage.setClip(clip);
            }
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setOnLogout(Runnable onLogout) {
        this.onLogout = onLogout;
    }

    private void setUpNavigation() {
        menuDashboardItem.setOnMouseClicked(event -> {
            showDashboard();
        });

        menuEmployeesItem.setOnMouseClicked(event -> {
            showEmployees();
        });

        menuAddEmployeesItem.setOnMouseClicked(event -> {
            showAddEmployee();
        });

        menuUpdateUserInfoItem.setOnMouseClicked(event -> {
            showUpdateUserInfo();
        });

        menuLogout.setOnMouseClicked(event -> {
            onLogout.run();
        });
    }

    private void showDashboard() {
        updateTitleAndActiveTab(menuDashboard);
        if (dashboardHome == null)
            dashboardHome = new DashboardHome();
        contentArea.getChildren().clear();
        contentArea.getChildren().add(dashboardHome.getView());
        fitToAnchor(contentArea.getChildren().get(0));
    }

    private void showEmployees() {
        updateTitleAndActiveTab(menuEmployees);
        if (employeeGrid == null)
            employeeGrid = new EmployeeGrid();
        contentArea.getChildren().clear();
        contentArea.getChildren().add(employeeGrid.getView());
        fitToAnchor(contentArea.getChildren().get(0));
    }

    private void showUpdateUserInfo() {
        try {
            Parent updateUserUI = FXMLLoader.load(
                    getClass().getResource("/com/hypermarket/view/user/UpdateUserInfo.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(updateUserUI);
            fitToAnchor(updateUserUI);
            updateTitleAndActiveTab(menuUpdateUserInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddEmployee() {
        try {
            Parent addEmployeeUI = FXMLLoader.load(
                    getClass().getResource("/com/hypermarket/view/admin/AdminAddEmployee.fxml"));

            contentArea.getChildren().clear();
            contentArea.getChildren().add(addEmployeeUI);
            fitToAnchor(addEmployeeUI);
            updateTitleAndActiveTab(menuAddEmployees);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fitToAnchor(javafx.scene.Node node) {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

    private void updateTitleAndActiveTab(Label activeBox) {
        menuDashboard.getStyleClass().remove("active-label");
        menuEmployees.getStyleClass().remove("active-label");
        menuAddEmployees.getStyleClass().remove("active-label");
        menuUpdateUserInfo.getStyleClass().remove("active-label");

        activeBox.getStyleClass().add("active-label");
        pageTitle.setText(activeBox.getText());
    }
}
