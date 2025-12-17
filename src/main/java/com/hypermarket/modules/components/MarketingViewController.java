package com.hypermarket.modules.components;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.Offer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
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
    private HBox menuDashboardItem;
    @FXML
    private HBox menuReportsItem;
    @FXML
    private HBox menuOffersItem;
    @FXML
    private HBox menuLogoutItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpNavigation();
        showDashboard();
    }

    protected void setUpNavigation() {
        menuDashboardItem.setOnMouseClicked(e -> showDashboard());
        menuReportsItem.setOnMouseClicked(e -> showReports());
        menuOffersItem.setOnMouseClicked(e -> showOffers());
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
    }

    private void setActiveMenu(HBox activeItem) {
        HBox[] items = { menuDashboardItem, menuReportsItem, menuOffersItem };
        for (HBox item : items) {
            item.setStyle("-fx-background-color: transparent;");
        }
        activeItem.setStyle("-fx-background-color: #4CAF50;");
    }

    private void refreshDashboard() {
        kpiContainer.getChildren().clear();
        dashboardContent.getChildren().clear();

        DataStore db = DataStore.getDataStore();

        // KPIs
        kpiContainer.getChildren().addAll(
                loadKpiCard("Total Offers", String.valueOf(db.getOffers().size()), "5%", true),
                loadKpiCard("Active Offers",
                        String.valueOf(
                                db.getOffers().stream().filter(o -> o.getManualStatus() == Offer.Status.ACTIVE)
                                        .count()),
                        "2%", true),
                loadKpiCard("Expired Offers",
                        String.valueOf(
                                db.getOffers().stream().filter(o -> o.getManualStatus() == Offer.Status.EXPIRED)
                                        .count()),
                        "1%", false));

        // PieChart
        // PieChart pie = loadPieChartComponent();
        // dashboardContent.getChildren().add(pie);

        // TableView of Offers
        ObservableList<Offer> offers = FXCollections.observableArrayList(db.getOffers());
        TableViewController<Offer> tableController = new TableViewController<>(Offer.class, offers, "offerName");
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/TableView.fxml"));
            loader.setController(tableController);
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
            return node;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // private PieChart loadPieChartComponent() {
    // try {
    //
    // FXMLLoader loader = new FXMLLoader(
    // getClass().getResource("/com/hypermarket/view/components/PieChart.fxml"));
    // PieChart root = loader.load();
    // root.setMinHeight(400);
    // return root;
    //
    // } catch (IOException ex) {
    // ex.printStackTrace();
    // return null;
    // }
    // }

    private void showReports() {
        pageTitle.setText("Reports");
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/ReportsPage.fxml"));
            Parent view = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
            fitToAnchor(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showOffers() {
        pageTitle.setText("Offers");
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/OffersPage.fxml"));
            Parent view = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
            fitToAnchor(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fitToAnchor(Parent node) {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

    private void showUpdateUserInfo() {
        try {
            Parent updateUserUI = FXMLLoader.load(
                    getClass().getResource("/com/hypermarket/view/user/UpdateUserInfo.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(updateUserUI);
            fitToAnchor(updateUserUI);
            fitToAnchor(updateUserUI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
