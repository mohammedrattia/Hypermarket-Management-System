package com.hypermarket.modules.inventory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hypermarket.data.FileManager;
import com.hypermarket.entities.Notification;
import com.hypermarket.entities.User;
import com.hypermarket.modules.inventory.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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

        menu.setStyle("-fx-background-color: white;");

        for (String message : alerts) {
            MenuItem item = new MenuItem(message);
            if (message.contains("LOW") || message.contains("EXPIRED")) {
                item.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            } else {
                item.setStyle("-fx-text-fill: green;");
            }
            menu.getItems().add(item);
        }
        menu.show(notificationBtn, Side.BOTTOM, 0, 0);
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
