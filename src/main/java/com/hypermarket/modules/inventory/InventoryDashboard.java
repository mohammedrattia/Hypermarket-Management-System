package com.hypermarket.modules.inventory;

import java.io.IOException;
import java.util.List;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.Product;
import com.hypermarket.modules.inventory.*;
import com.hypermarket.modules.components.KpiCardController;
import com.hypermarket.modules.components.PieChartController;
import com.hypermarket.modules.components.ProductCardController;
import com.hypermarket.service.Session;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InventoryDashboard {

    private VBox productGridContainer;
    private HBox kpiContainer;
    private PieChart pieChartNode;

    private ObjectProperty<Product> selectedProduct = new SimpleObjectProperty<>();

    public ReadOnlyObjectProperty<Product> getSelectedProductProperty() {
        return selectedProduct;
    }

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
        refreshPie();
    }

    private void refreshKpis() {
        kpiContainer.getChildren().clear();

        DataStore db = DataStore.getDataStore();
        List<Product> products = db.getProducts();

        int productCount = products.size();

        long categoryCount = products.stream().map(Product::getCategory).distinct().count();

        long lowStockCount = products.stream().filter(p -> p.getQuantity() <= p.getThreshold()).count();

        kpiContainer.getChildren().addAll(
                loadKpiCard("Porducts", String.valueOf(productCount), "-", true),
                loadKpiCard("Categories", String.valueOf(categoryCount),"-", true),
                loadKpiCard("Low Stock", lowStockCount + " Items", "-", false));
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
            ((VBox) node).setAlignment(Pos.TOP_CENTER);
            HBox.setHgrow(node, Priority.ALWAYS);
            ((VBox) node).setMaxWidth(Double.MAX_VALUE);

            return node;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private PieChart loadPieChartComponent() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/PieChart.fxml"));
            PieChart root = loader.load();
            PieChartController pieController = loader.getController();
            pieController.setData(DataStore.getDataStore().getProducts(), "category");
            return root;
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
            node.setOnMouseClicked(event -> {
                if (Session.getInstance().getUser().getRole().toString() == "INVENTORY") {
                    openProductDetailsModal(product);
                } else if (Session.getInstance().getUser().getRole().toString() == "SALES") {
                    selectedProduct.set(product);
                }
            });
            return node;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void refreshPie() {
        pieChartNode = loadPieChartComponent();
    }


    private void openProductDetailsModal(Product product) {
        try {
            FXMLLoader modalLoader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/inventory/ProductDetailsModal.fxml"));
            Parent modalView = modalLoader.load();
            Object modalController = modalLoader.getController();
            try {
                java.lang.reflect.Method setProductMethod = modalController.getClass().getMethod("setProduct",
                        Product.class);
                setProductMethod.invoke(modalController, product);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initStyle(StageStyle.UTILITY);
            modalStage.setTitle("Product Details");
            modalStage.setScene(new Scene(modalView));

            modalStage.showAndWait();
            refreshView(); 
        } catch (IOException e) {
            e.printStackTrace();
        
        }
    }
}