package com.hypermarket.modules.inventory;

import java.io.IOException;
import java.util.List;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.Product; // Assuming you have this
import com.hypermarket.modules.components.KpiCardController;
// Import your PieChart controller/component if needed

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;

public class InventoryDashboard {

    private HBox kpiContainer;
    private VBox productListContainer; 

    // This method is what you call from your MainController
    public Parent getView() {
        
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        kpiContainer = new HBox(20);
        productListContainer = new VBox(10);

        refreshView(); 

        ScrollPane scrollPane = new ScrollPane(productListContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS); // Let it grow to fill space

        // 6. Assemble everything
        // Example: Top = KPIs, Center = Chart + List
        mainLayout.getChildren().add(kpiContainer);
        
        // Let's assume you want the PieChart above the List
        Parent chartNode = loadPieChartComponent();
        if (chartNode != null) {
            mainLayout.getChildren().add(chartNode);
        }
        
        mainLayout.getChildren().add(scrollPane);

        return mainLayout;
    }

    // --- REFRESH LOGIC ---

    public void refreshView() {
        refreshKpis();
        refreshProductList();
    }

    private void refreshKpis() {
        kpiContainer.getChildren().clear();

        // Calculate Stats
        DataStore ds = DataStore.getDataStore();
        int productCount = ds.getProducts().size();
        // You can add logic here to count low stock, etc.

        // Load the 3 Cards
        kpiContainer.getChildren().addAll(
            loadKpiCard("Total Products", "100", "+5", true),
            loadKpiCard("Low Stock", "5", "-2", false), // Example data
            loadKpiCard("Categories", "8", "0", true)
        );
    }

    private void refreshProductList() {
        productListContainer.getChildren().clear();

        List<Product> products = DataStore.getDataStore().getProducts();
        
        // Loop through data and create a row for each product
        for (Product p : products) {
            // Assume you have a ProductCard.fxml, or you can use EmployeeCard.fxml as a template
            // passing 'null' for onDelete for now
            productListContainer.getChildren().add(loadProductCard(p)); 
        }
    }

    // --- LOADER HELPER METHODS ---
    // These methods do the heavy lifting of reading the FXML files

    private Parent loadKpiCard(String title, String value, String trend, boolean isPositive) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hypermarket/view/components/KpiCard.fxml"));
            Parent node = loader.load();
            
            // Get controller and push data
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
            // Assuming your PieChart component is in this path
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hypermarket/view/components/PieChart.fxml"));
            return loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Assuming you reuse EmployeeCard or have a ProductCard.fxml
    private Parent loadProductCard(Product product) {
        // You can reuse a card FXML here. If you don't have a ProductCard.fxml,
        // you might need to create one, or use a Label for now.
        try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hypermarket/view/components/EmployeeCard.fxml"));
             Parent node = loader.load();
             
             // Setup controller (You might need to adapt EmployeeCardController to accept Product)
             // EmployeeCardController controller = loader.getController();
             // controller.setProduct(product); 
             
             return node;
        } catch (IOException ex) {
             return null;
        }
    }
}

