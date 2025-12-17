package com.hypermarket.modules.sales;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.hypermarket.app.App;
import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;
import com.hypermarket.entities.Order;
import com.hypermarket.entities.Sales;
import com.hypermarket.modules.admin.AdminViewController;
import com.hypermarket.modules.components.EmployeeCardController;
import com.hypermarket.modules.components.KpiCardController;
import com.hypermarket.modules.components.ReceiptPrinter;
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
import javafx.scene.control.Tooltip;
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
    private HBox optionsBar;

    @FXML
    private Button newOrderButton;

    @FXML
    private Button returnOrderButton;

    @FXML
    private Button viewReceiptButton;

    private Runnable onNewOrderRequest;

    TableViewController ordersTable;

    Order selectedOrder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadOrdersTable();
        initListeners();
        Tooltip t = new Tooltip("Select an order to view receipt");
        viewReceiptButton.setTooltip(t);
        viewReceiptButton.disableProperty().bind(ordersTable.getSelectedItemProperty().isNull());
        returnOrderButton.disableProperty().bind(ordersTable.getSelectedItemProperty().isNull());
    }

    private void initListeners() {
        ordersTable.getSelectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedOrder = (Order) newVal;
        });
        newOrderButton.setOnMouseClicked(event -> onNewOrderRequest.run());
        viewReceiptButton.setOnMouseClicked(event -> loadOrderReceipt());
        returnOrderButton.setOnMouseClicked(event -> loadReturnOrder());
    }

    private void loadOrderReceipt() {
        if (selectedOrder == null) {
            System.out.println("Popup: Please select an order first!");
        } else {
            System.out.println("Opening Receipt for Order #" + selectedOrder.getOrderID());
            selectedOrder.printReceipt();
        }
    }

    private void loadReturnOrder() {
        if (selectedOrder == null) {
            System.out.println("Popup: Please select an order first!");
        } else {
            System.out.println("Opening Return Page for Order #" + selectedOrder.getOrderID());
        }
    }

    public void setOnNewOrderAction(Runnable action) {
        this.onNewOrderRequest = action;
    }

    private void loadOrdersTable() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/com/hypermarket/view/components/TableView.fxml"));
        if (ordersTable == null) {
            ordersTable = new TableViewController<>(Order.class, DataStore.getDataStore().getOrders(), "orderID");
        }
        fxmlLoader.setController(ordersTable);

        formatTable();

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

    private void formatTable() {
        ordersTable.excludeColumn("items");

        ordersTable.setColumnFormatter("seller", obj -> {
            Sales s = (Sales) obj;
            return s.getFName();
        });

        ordersTable.setColumnFormatter("dateTime", obj -> {
            return ((LocalDateTime) obj).format(FileManager.dateTimeFormat);
        });
    }

    private void fitToAnchor(Node node) {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

    public void clearTableSelection() {
        ordersTable.clearSelection();
    }
}
