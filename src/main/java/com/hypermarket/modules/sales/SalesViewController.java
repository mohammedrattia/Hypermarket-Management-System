package com.hypermarket.modules.sales;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hypermarket.data.FileManager;
import com.hypermarket.entities.User;
import com.hypermarket.modules.admin.DashboardHome;
import com.hypermarket.modules.admin.EmployeeGrid;
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

public class SalesViewController implements Initializable {

    @FXML
    private AnchorPane contentArea;

    @FXML
    private Label pageTitle;

    @FXML
    private Label menuDashboard;

    @FXML
    private Label menuOrders;

    @FXML
    private Label menuMakeOrder;

    @FXML
    private Label menuReturnedOrders;

    @FXML
    private Label menuReturnOrder;

    @FXML
    private Label menuUpdateUserInfo;

    @FXML
    private Label menuLogout;

    @FXML
    private HBox menuDashboardItem;

    @FXML
    private HBox menuOrdersItem;

    @FXML
    private HBox menuMakeOrderItem;

    @FXML
    private HBox menuReturnedOrdersItem;

    @FXML
    private HBox menuReturnOrderItem;

    @FXML
    private HBox menuUpdateUserInfoItem;

    @FXML
    private HBox menuLogoutItem;

    @FXML
    private ImageView userImage;

    Runnable onLogout;

    private SalesDashboard salesDashboard;
    // private EmployeeGrid employeeGrid;

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

        menuOrdersItem.setOnMouseClicked(event -> {
            showListOrders();
        });

        menuMakeOrderItem.setOnMouseClicked(event -> {
            showMakeOrder();
        });
        menuReturnedOrdersItem.setOnMouseClicked(event -> {
            // showAddEmployee();
        });
        menuReturnOrderItem.setOnMouseClicked(event -> {
            // showAddEmployee();
        });

        menuUpdateUserInfo.setOnMouseClicked(event -> {
            showUpdateUserInfo();
        });

        menuLogout.setOnMouseClicked(event -> {
            onLogout.run();
        });
    }

    private void showDashboard() {
        updateTitleAndActiveTab(menuDashboard);
        if (salesDashboard == null)
            salesDashboard = new SalesDashboard();
        contentArea.getChildren().clear();
        contentArea.getChildren().add(salesDashboard.getView());
        fitToAnchor(contentArea.getChildren().get(0));
    }

    private void showListOrders() {
        try {
            Parent listOrdersUI = FXMLLoader.load(
                    getClass().getResource("/com/hypermarket/view/sales/ListOrders.fxml"));

            contentArea.getChildren().clear();
            contentArea.getChildren().add(listOrdersUI);
            fitToAnchor(listOrdersUI);
            updateTitleAndActiveTab(menuOrders);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMakeOrder() {
        try {
            Parent makeOrderUI = FXMLLoader.load(
                    getClass().getResource("/com/hypermarket/view/sales/MakeOrder.fxml"));

            contentArea.getChildren().clear();
            contentArea.getChildren().add(makeOrderUI);
            fitToAnchor(makeOrderUI);
            updateTitleAndActiveTab(menuMakeOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void fitToAnchor(javafx.scene.Node node) {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

    private void updateTitleAndActiveTab(Label activeBox) {
        menuDashboard.getStyleClass().remove("active-label");
        menuOrders.getStyleClass().remove("active-label");
        menuMakeOrder.getStyleClass().remove("active-label");
        menuReturnedOrders.getStyleClass().remove("active-label");
        menuUpdateUserInfo.getStyleClass().remove("active-label");

        activeBox.getStyleClass().add("active-label");
        pageTitle.setText(activeBox.getText());
    }
}
