package com.hypermarket.modules.sales;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hypermarket.data.FileManager;
import com.hypermarket.entities.Sales;
import com.hypermarket.entities.User;
import com.hypermarket.modules.components.ViewController;
import com.hypermarket.modules.user.UpdateInfoController;
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

public class SalesViewController extends ViewController implements Initializable {

    @FXML
    private AnchorPane mainContainer;

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

    private SalesDashboard salesDashboard;

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

    protected void setUpNavigation() {
        menuDashboardItem.setOnMouseClicked(event -> showDashboard());

        menuOrdersItem.setOnMouseClicked(event -> showListOrders());

        menuMakeOrderItem.setOnMouseClicked(event -> showMakeOrder());
        menuReturnedOrdersItem.setOnMouseClicked(event -> showListReturns());

        menuUpdateUserInfo.setOnMouseClicked(event -> {
            showUpdateUserInfo();
        });

        menuLogout.setOnMouseClicked(event -> {
            onLogout.run();
        });
    }

    protected void showDashboard() {
        updateTitleAndActiveTab(menuDashboard);
        if (salesDashboard == null)
            salesDashboard = new SalesDashboard((Sales) Session.getInstance().getUser());
        contentArea.getChildren().clear();
        contentArea.getChildren().add(salesDashboard.getView());
        fitToAnchor(contentArea.getChildren().get(0));
    }

    private void showListOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hypermarket/view/sales/ListOrders.fxml"));
            Parent listOrdersUI = loader.load();
            ListOrders listOrdersController = loader.getController();

            mainContainer.setOnMouseClicked(event -> {
                if (contentArea.getChildren().contains(listOrdersUI))
                    listOrdersController.clearTableSelection();
            });

            listOrdersController.setOnNewOrderAction(() -> {
                showMakeOrder();
            });

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hypermarket/view/sales/MakeOrder.fxml"));
            Parent makeOrderUI = loader.load();
            MakeOrder makeOrderController = loader.getController();

            mainContainer.setOnMouseClicked(event -> {
                if (contentArea.getChildren().contains(makeOrderUI))
                    makeOrderController.clearSelection();
            });

            contentArea.getChildren().clear();
            contentArea.getChildren().add(makeOrderUI);
            fitToAnchor(makeOrderUI);
            updateTitleAndActiveTab(menuMakeOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showListReturns() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hypermarket/view/sales/ListReturns.fxml"));
            Parent listReturnsUI = loader.load();
            ListReturns listReturnsController = loader.getController();

            mainContainer.setOnMouseClicked(event -> {
                if (contentArea.getChildren().contains(listReturnsUI))
                    listReturnsController.clearTableSelection();
            });

            contentArea.getChildren().clear();
            contentArea.getChildren().add(listReturnsUI);
            fitToAnchor(listReturnsUI);
            updateTitleAndActiveTab(menuReturnedOrders);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
