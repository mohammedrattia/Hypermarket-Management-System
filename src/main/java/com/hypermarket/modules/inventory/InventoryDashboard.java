package com.hypermarket.modules.inventory;

import java.io.IOException;
import java.util.List;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.Product;
import com.hypermarket.modules.components.KpiCardController;
import com.hypermarket.modules.components.ProductCardController;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class InventoryDashboard {

    private VBox productGridContainer;
    private HBox kpiContainer;

    public Parent getView() {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(25));

        kpiContainer = new HBox(30);

        productGridContainer = new VBox(10);
        productGridContainer.setPadding(new Insets(0));

        refreshView();

        HBox bottomContainer = new HBox(5);
        VBox.setVgrow(bottomContainer, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(productGridContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefWidth(400);
        scrollPane.setStyle("-fx-background-color: transparent;");

        Parent pieChartNode = loadPieChartComponent();
        HBox.setHgrow(pieChartNode, Priority.ALWAYS);

        bottomContainer.getChildren().addAll(pieChartNode, scrollPane);
        mainLayout.getChildren().addAll(kpiContainer, bottomContainer);

        return mainLayout;
    }

    private void refreshView() {
        refreshKpis();
        refreshList();
    }

    private void refreshKpis() {
        kpiContainer.getChildren().clear();

        DataStore db = DataStore.getDataStore();
        List<Product> products = db.getProducts();

        int productCount = products.size();

        kpiContainer.getChildren().addAll(
                loadKpiCard("Porducts", String.valueOf(productCount), "2", true),
                loadKpiCard("Categories", "10", "2", true),
                loadKpiCard("Low Stock", "10 Items", "5%", false));
    }

    private void refreshList() {
        productGridContainer.getChildren().clear();

        DataStore db = DataStore.getDataStore();
        List<Product> products = db.getProducts();
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            productGridContainer.getChildren().add(loadProductCards(p, this::refreshView));
        }
    }

    private Parent loadKpiCard(String title, String value, String trend, boolean isPositive) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/KpiCard.fxml"));
            Parent node = loader.load();

            KpiCardController controller = loader.getController();
            controller.setData(title, value, trend, isPositive);

            return node;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Parent loadPieChartComponent() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/PieChart.fxml"));
            return loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Parent loadProductCards(Product product, Runnable onDelete) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/ProductCard.fxml"));
            Parent node = loader.load();

            ProductCardController controller = loader.getController();
            controller.setData(product);
            controller.setOnDeleteAction(onDelete);
            return node;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
