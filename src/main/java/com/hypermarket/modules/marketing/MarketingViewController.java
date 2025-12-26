package com.hypermarket.modules.marketing;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;
import com.hypermarket.entities.Offer;
import com.hypermarket.entities.Product;
import com.hypermarket.entities.User;
import com.hypermarket.modules.components.KpiCardController;
import com.hypermarket.modules.components.TableViewController;
import com.hypermarket.modules.components.ViewController;
import com.hypermarket.modules.user.UpdateInfoController;
import com.hypermarket.service.Session;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MarketingViewController extends ViewController implements Initializable {

    @FXML
    private AnchorPane contentArea;

    @FXML
    private Label pageTitle;

    @FXML
    private ImageView userImage;

    @FXML
    private VBox dashboardContainer;

    @FXML
    private HBox kpiContainer;

    @FXML
    private VBox dashboardContent;

    @FXML
    private AnchorPane tableContainer;

    @FXML
    private Label menuDashboard;

    @FXML
    private Label menuReports;

    @FXML
    private Label menuOffers;

    @FXML
    private Label menuUpdateUserInfo;

    @FXML
    private HBox menuDashboardItem;

    @FXML
    private HBox menuReportsItem;

    @FXML
    private HBox menuOffersItem;

    @FXML
    private HBox menuLogoutItem;

    @FXML
    private HBox userImageContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpNavigation();
        showDashboard();
        refereshImage();
    }

    protected void setUpNavigation() {
        menuDashboardItem.setOnMouseClicked(e -> showDashboard());
        menuReportsItem.setOnMouseClicked(e -> showReports());
        menuOffersItem.setOnMouseClicked(e -> showOffers());
        menuUpdateUserInfo.setOnMouseClicked(e -> showUpdateUserInfo());
        userImageContainer.setOnMouseClicked(e -> showUpdateUserInfo());
        menuLogoutItem.setOnMouseClicked(e -> onLogout.run());
    }

    protected void showDashboard() {
        pageTitle.setText("Marketing Dashboard");
        pageTitle.setFont(Font.font("System", FontWeight.BOLD, 28));
        dashboardContainer.setVisible(true);
        contentArea.getChildren().clear();
        refreshDashboard();
        contentArea.getChildren().add(dashboardContainer);
        fitToAnchor(dashboardContainer);
        updateTitleAndActiveTab(menuDashboard);
    }

    private void refreshDashboard() {
        kpiContainer.getChildren().clear();
        dashboardContent.getChildren().clear();

        DataStore db = DataStore.getDataStore();

        kpiContainer.getChildren().addAll(
                loadKpiCard("Total Offers", String.valueOf(db.getOffers().size()), "", true),
                loadKpiCard("Active Offers",
                        String.valueOf(
                                db.getOffers().stream().filter(o -> o.getManualStatus() == Offer.Status.ACTIVE)
                                        .count()),
                        "", true),
                loadKpiCard("Expired Offers",
                        String.valueOf(
                                db.getOffers().stream().filter(o -> o.getManualStatus() == Offer.Status.EXPIRED)
                                        .count()),
                        "", false));

        // TableView of Offers
        ObservableList<Offer> offers = FXCollections.observableArrayList(db.getOffers());
        TableViewController<Offer> tableController = new TableViewController<>(Offer.class, offers, "offerName");
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/TableView.fxml"));
            loader.setController(tableController);
            tableController.setColumnFormatter("product", obj -> {
                return (obj != null) ? ((Product) obj).getName() : "Unknown";
            });
            tableController.setColumnFormatter("discount", obj -> {
                return String.format("%3.0f%%", obj);
            });
            Parent tableNode = loader.load();
            tableContainer.getChildren().clear();
            tableContainer.getChildren().add(tableNode);
            fitToAnchor(tableNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Parent loadKpiCard(String title, String value, String trend, boolean isPositive) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hypermarket/view/components/KpiCard.fxml"));
            Parent node = loader.load();
            KpiCardController controller = loader.getController();
            controller.setData(title, value, trend, isPositive);
            HBox.setHgrow(node, Priority.ALWAYS);
            ((VBox) node).setMaxWidth(Double.MAX_VALUE);
            return node;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void showReports() {
        pageTitle.setText("Reports");
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/marketing/ReportsPage.fxml"));
            Parent view = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
            fitToAnchor(view);
            updateTitleAndActiveTab(menuReports);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showOffers() {
        pageTitle.setText("Offers");
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/marketing/OffersPage.fxml"));
            Parent view = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
            fitToAnchor(view);
            updateTitleAndActiveTab(menuOffers);
        } catch (IOException e) {
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
            File imageFile = new File(FileManager.USER_IMAGE_PATH + currentUser.getImage());
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

    private void fitToAnchor(Parent node) {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

    private void updateTitleAndActiveTab(Label activeBox) {
        menuDashboard.getStyleClass().remove("active-label");
        menuOffers.getStyleClass().remove("active-label");
        menuReports.getStyleClass().remove("active-label");
        menuUpdateUserInfo.getStyleClass().remove("active-label");

        activeBox.getStyleClass().add("active-label");
        pageTitle.setText(activeBox.getText());
    }
}
