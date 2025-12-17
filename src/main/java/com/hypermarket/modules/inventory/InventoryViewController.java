package com.hypermarket.modules.inventory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hypermarket.data.FileManager;
import com.hypermarket.entities.Notification;
import com.hypermarket.entities.User;
import com.hypermarket.modules.inventory.*;
import com.hypermarket.modules.user.UpdateInfoController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import com.hypermarket.service.Session;
import java.util.List;
import javafx.geometry.Side;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.scene.input.MouseEvent;

public class InventoryViewController implements Initializable {

    @FXML
    private AnchorPane contentArea;

    @FXML
    private Label pageTitle;

    @FXML
    private Label menuDashboard;

    @FXML
    private Label menuProducts;

    @FXML
    private Label menuAddProducts;

    @FXML
    private Label menuUpdateUserInfo;

    @FXML
    private HBox menuDashboardItem;

    @FXML
    private HBox menuProductsItem;

    @FXML
    private HBox menuAddProductItem;

    @FXML
    private HBox menuLogoutItem;

    @FXML
    private HBox menuUpdateUserInfoItem;

    @FXML
    private ImageView userProfileImage;

    @FXML
    private FontIcon notificationBtn;

    private InventoryDashboard inventorydashboard;
    private ProductsGrid productsGrid;
    Runnable onLogout;

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
                userProfileImage.setImage(image);

                userProfileImage.setPreserveRatio(false);
                Circle clip = new Circle();
                clip.setCenterX(25);
                clip.setCenterY(25);
                clip.setRadius(25);

                userProfileImage.setClip(clip);
            }
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
        }

        if (notificationBtn != null) {
            notificationBtn.setOnMouseClicked(event -> {
                showNotifications();
            });
        }
    }

    public void setOnLogout(Runnable onLogout) {
        this.onLogout = onLogout;
    }

    private void setUpNavigation() {
        menuDashboardItem.setOnMouseClicked(event -> {
            showDashboard();
        });

        menuProductsItem.setOnMouseClicked(event -> {
            showProducts();
        });

        menuAddProductItem.setOnMouseClicked(event -> {
            showAddProduct();
        });

        menuUpdateUserInfo.setOnMouseClicked(event -> {
            showUpdateUserInfo();
        });

        menuUpdateUserInfoItem.setOnMouseClicked(event -> {
            showUpdateUserInfo();
        });

        menuLogoutItem.setOnMouseClicked(event -> {
            onLogout.run();
        });
    }

    private void showDashboard() {
        updateTitleAndActiveTab(menuDashboard);
        if (inventorydashboard == null)
            inventorydashboard = new InventoryDashboard();
        contentArea.getChildren().clear();
        contentArea.getChildren().add(inventorydashboard.getView());
        fitToAnchor(contentArea.getChildren().get(0));
    }

    private void showProducts() {
        updateTitleAndActiveTab(menuProducts);

        if (productsGrid == null)
            productsGrid = new ProductsGrid();

        contentArea.getChildren().clear();
        contentArea.getChildren().add(productsGrid.getView());
        fitToAnchor(contentArea.getChildren().get(0));
    }

    private void showUpdateUserInfo() {
        try {
            FXMLLoader updateUserUI = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/user/UpdateUserInfo.fxml"));

            Parent root = updateUserUI.load();
            UpdateInfoController controller = updateUserUI.getController();
            controller.setOnUpdateImage(() -> {
                refereshImage();
            });
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
            fitToAnchor(root);
            updateTitleAndActiveTab(menuUpdateUserInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refereshImage() {
        User currentUser = Session.getInstance().getUser();
        try {
            File imageFile = new File(FileManager.IMAGE_PATH + currentUser.getImage());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toURL().toString());
                userProfileImage.setImage(image);

                userProfileImage.setPreserveRatio(false);
                Circle clip = new Circle();
                clip.setCenterX(25);
                clip.setCenterY(25);
                clip.setRadius(25);

                userProfileImage.setClip(clip);
            }
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void showAddProduct() {
        try {
            Parent addProductUI = FXMLLoader.load(
                    getClass().getResource("/com/hypermarket/view/inventory/AddProduct.fxml"));

            contentArea.getChildren().clear();
            contentArea.getChildren().add(addProductUI);
            contentArea.setPadding(Insets.EMPTY);
            fitToAnchor(addProductUI);
            updateTitleAndActiveTab(menuAddProducts);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showNotifications() {
        List<String> alerts = com.hypermarket.entities.Notification.getSystemAlerts();
        ContextMenu menu = new ContextMenu();

        menu.setStyle("-fx-selection-bar: white; -fx-selection-bar-non-focused: white; -fx-background-color: white;");

        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(15));
        contentBox.setStyle("-fx-background-color: white;");

        for (String message : alerts) {
            Label itemLabel = new Label(message);
            itemLabel.setWrapText(true);
            itemLabel.setMaxWidth(300);

            if (message.contains("LOW") || message.contains("EXPIRED")) {
                itemLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold; -fx-font-size: 14px;");
            } else {
                itemLabel.setStyle("-fx-text-fill: #2e7d32; -fx-font-size: 13px;");
            }
            contentBox.getChildren().add(itemLabel);
        }

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(450);
        scrollPane.setMaxHeight(450);
        scrollPane.setPrefWidth(350);

        scrollPane.setFocusTraversable(false);
        scrollPane.setStyle("-fx-background-color: white; -fx-background-insets: 0;");

        CustomMenuItem customItem = new CustomMenuItem(scrollPane);
        customItem.setHideOnClick(false);
        
        customItem.setStyle("-fx-background-color: white;");
        
        menu.getItems().add(customItem);
        menu.show(notificationBtn, Side.LEFT, -20, -150);
    }

    private void fitToAnchor(javafx.scene.Node node) {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

    private void updateTitleAndActiveTab(Label activeBox) {
        menuDashboard.getStyleClass().remove("active-label");
        menuProducts.getStyleClass().remove("active-label");
        menuAddProducts.getStyleClass().remove("active-label");
        menuUpdateUserInfo.getStyleClass().remove("active-label");

        activeBox.getStyleClass().add("active-label");
        pageTitle.setText(activeBox.getText());
    }
}
