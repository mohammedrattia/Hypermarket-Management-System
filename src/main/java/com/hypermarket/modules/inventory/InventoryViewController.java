package com.hypermarket.modules.inventory;

import java.net.URL;
import java.util.ResourceBundle;

import com.hypermarket.modules.inventory.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

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
    private HBox menuUpdateUserInfoItem;

    private InventoryDashboard inventorydashboard;
    private ProductsGrid productsGrid;
    Runnable onLogout;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showDashboard();
        setUpNavigation();
        updateTitleAndActiveTab(menuDashboard);
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
                    getClass().getResource("/com/hypermarket/view/UpdateUserInfo.fxml"));
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
