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

    private final DashboardHome dashboardHome = new DashboardHome();
    private final EmployeeGrid employeeGrid = new EmployeeGrid();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showDashboard();
        setUpNavigation();
        setActiveTab(menuDashboard);
    }

    private void setUpNavigation() {
        if (menuDashboard != null) {
            menuDashboard.setOnMouseClicked(event -> {
                setActiveTab(menuDashboard);
                showDashboard();
            });
        }

        if (menuEmployees != null) {
            menuEmployees.setOnMouseClicked(event -> {
                setActiveTab(menuEmployees);
                showEmployees();
            });
        }

        if (menuUpdateUserInfo != null) {
            menuUpdateUserInfo.setOnMouseClicked(event -> {
                setActiveTab(menuUpdateUserInfo);
                showUpdateUserInfo();
            });
        }
        if (menuAddEmployees != null) {
            menuAddEmployees.setOnMouseClicked(event -> {
                setActiveTab(menuAddEmployees);
                showAddEmployee();
            });
        }

    }

    private void showDashboard() {
        if (pageTitle != null)
            pageTitle.setText("Dashboard");
        setActiveTab(menuDashboard);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(dashboardHome.getView());
        fitToAnchor(contentArea.getChildren().get(0));
    }

    private void showEmployees() {
        if (pageTitle != null)
            pageTitle.setText("Employees List");
        setActiveTab(menuEmployees);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(employeeGrid.getView());
        fitToAnchor(contentArea.getChildren().get(0));
    }

    private void fitToAnchor(javafx.scene.Node node) {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

    private void setActiveTab(Label activeBox) {
        if (menuDashboard != null)
            menuDashboard.getStyleClass().remove("active-label");
        if (menuEmployees != null)
            menuEmployees.getStyleClass().remove("active-label");
        if (menuAddEmployees != null)
            menuAddEmployees.getStyleClass().remove("active-label");
        if (menuUpdateUserInfo != null)
            menuUpdateUserInfo.getStyleClass().remove("active-label");

        activeBox.getStyleClass().add("active-label");
    }

    private void showUpdateUserInfo() {
        try {
            Parent updateUserUI = FXMLLoader.load(
                    getClass().getResource("/com/hypermarket/view/UpdateUserInfo.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(updateUserUI);
            fitToAnchor(updateUserUI);

            if (pageTitle != null)
                pageTitle.setText("Update User Info");

            setActiveTab(menuUpdateUserInfo);

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

            if (pageTitle != null)
                pageTitle.setText("Add Employee");

            setActiveTab(menuAddEmployees);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
