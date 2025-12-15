package com.hypermarket.modules.sales;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hypermarket.app.App;
import com.hypermarket.data.DataStore;
import com.hypermarket.entities.Order;
import com.hypermarket.modules.admin.AdminViewController;
import com.hypermarket.modules.components.EmployeeCardController;
import com.hypermarket.modules.components.KpiCardController;
import com.hypermarket.modules.components.TableViewController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ListOrders implements Initializable {
    @FXML
    private AnchorPane listArea;

    @FXML
    private VBox mainContainer;

    @FXML
    private Button newOrderButton;

    @FXML
    private HBox optionsBar;

    @FXML
    private Button removeButton;

    @FXML
    private Button viewReceiptButton;

    TableViewController ordersTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/com/hypermarket/view/components/TableView.fxml"));
        if (ordersTable == null) {
            ordersTable = new TableViewController<>(Order.class, DataStore.getDataStore().getOrders(), "orderID");
        }
        fxmlLoader.setController(ordersTable);
        try {
            Parent table = fxmlLoader.load();
            listArea.getChildren().clear();
            listArea.getChildren().add(table);
            if (listArea instanceof AnchorPane) {
                fitToAnchor(table);
            }
        } catch (Exception e) {
            System.out.println("couldn't load products!!");
            System.err.println(e.toString());
        }
    }
    // rootPane.getStylesheets().add(getClass().getResource("/css/global.css").toExternalForm());
    // rootPane.getStylesheets().add(getClass().getResource("/css/specific.css").toExternalForm());
    // loadDashboardHome();

    private void fitToAnchor(Node node) {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }
}
