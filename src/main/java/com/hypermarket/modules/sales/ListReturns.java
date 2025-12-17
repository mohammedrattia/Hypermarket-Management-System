package com.hypermarket.modules.sales;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;
import com.hypermarket.entities.OrderItem;
import com.hypermarket.entities.Product;
import com.hypermarket.entities.Return; // Import the Return entity
import com.hypermarket.modules.components.TableViewController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ListReturns implements Initializable {
    @FXML
    private AnchorPane listArea;

    @FXML
    private VBox mainContainer;

    // 1. Updated Generic Type to <Return>
    private TableViewController<Return> returnsTable;

    // 2. Updated Selection Object
    private Return selectedReturn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadReturnsTable();
        initListeners();
    }

    private void initListeners() {
        // 3. Updated Listener to cast to Return
        returnsTable.getSelectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedReturn = newVal;
            if (selectedReturn != null) {
                System.out.println("Selected Return ID: " + selectedReturn.getReturnID());
            }
        });
    }

    private void loadReturnsTable() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/com/hypermarket/view/components/TableView.fxml"));

        if (returnsTable == null) {
            // 4. Initialize with Return.class and getReturns() list
            returnsTable = new TableViewController<>(Return.class, DataStore.getDataStore().getReturns(), "returnID");
        }
        fxmlLoader.setController(returnsTable);

        formatTable(); // Apply the new formatters

        try {
            Parent table = fxmlLoader.load();
            listArea.getChildren().clear();
            listArea.getChildren().add(table);
            if (listArea instanceof AnchorPane) {
                fitToAnchor(table);
            }
        } catch (Exception e) {
            System.out.println("couldn't load returns table!!");
            e.printStackTrace();
        }
    }

    private void formatTable() {
        returnsTable.setColumnFormatter("orderItem", obj -> {
            OrderItem item = (OrderItem) obj;
            return item != null ? String.valueOf(item.getOrderItemID()) : "";
        });

        returnsTable.setColumnFormatter("product", obj -> {
            Product p = (Product) obj;
            // If you want "ID - Name", change to: p.getProductID() + " - " + p.getName()
            return p != null ? String.valueOf(p.getProductID()) : "";
        });

        returnsTable.setColumnFormatter("returnDate", obj -> {
            if (obj != null) {
                return ((LocalDateTime) obj).format(FileManager.dateTimeFormat);
            }
            return "";
        });

        returnsTable.setColumnFormatter("isDamaged", obj -> {
            // Note: Check if your field is named "isDamaged" or just "damaged"
            return (boolean) obj ? "Damaged" : "Good Condition";
        });

        returnsTable.setColumnFormatter("refundAmount", obj -> {
            return String.format("$ %,.2f", (Double) obj);
        });
    }

    private void fitToAnchor(Node node) {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

    public void clearTableSelection() {
        returnsTable.clearSelection();
    }
}