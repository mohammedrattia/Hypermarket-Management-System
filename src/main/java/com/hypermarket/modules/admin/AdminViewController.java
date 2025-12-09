package com.hypermarket.modules.admin;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

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

    private DashboardHome dashboardHome;
    private EmployeeGrid employeeGrid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showDashboard();
        setUpNavigation();
        updateTitleAndActiveTab(menuDashboard);
    }

    private void setUpNavigation() {
        menuDashboard.setOnMouseClicked(event -> {
            showDashboard();
        });

        menuEmployees.setOnMouseClicked(event -> {
            showEmployees();
        });

        menuAddEmployees.setOnMouseClicked(event -> {
            showAddEmployee();
        });

        menuUpdateUserInfo.setOnMouseClicked(event -> {
            showUpdateUserInfo();
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
                    getClass().getResource("/com/hypermarket/view/UpdateUserInfo.fxml"));
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
                    getClass().getResource("/com/hypermarket/view/AdminAddEmployee.fxml"));

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
