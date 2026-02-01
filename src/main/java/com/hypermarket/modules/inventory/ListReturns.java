package com.hypermarket.modules.inventory;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;
import com.hypermarket.entities.Batch;
import com.hypermarket.entities.OrderItem;
import com.hypermarket.entities.Product;
import com.hypermarket.entities.Return;
import com.hypermarket.modules.components.TableViewController;
import com.hypermarket.service.Toast;

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

    private TableViewController<Return> returnsTable;

    private Return selectedReturn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadReturnsTable();
        initListeners();
    }

    private void initListeners() {
        returnsTable.getSelectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedReturn = newVal;
        });
    }

    @FXML
    private void handleReAddToStock() {
        if (selectedReturn == null) {
            String selectReturnedOrderWarningMsg = "Please select a returned order first.";
            Toast.showToast(selectReturnedOrderWarningMsg, Toast.NotificationType.WARNING);
            return;
        }
        String reAddReturnedOrderSuccessMsg = "Item re-added to stock (Nearest Expiry Batch updated).";
        Toast.showToast(reAddReturnedOrderSuccessMsg, Toast.NotificationType.INFORMATION);
        DataStore db = DataStore.getDataStore();

        Product product = selectedReturn.getProduct();
        int qtyToReturn = selectedReturn.getQuantityReturned();

        List<Batch> productBatches = db.getBatches().stream()
                .filter(b -> b.getProduct().getProductID() == product.getProductID())
                .sorted(Comparator.comparing(Batch::getExpiryDate))
                .collect(Collectors.toList());

        if (!productBatches.isEmpty()) {
            Batch targetBatch = productBatches.get(0);
            targetBatch.setQuantity(targetBatch.getQuantity() + qtyToReturn);
        } else {
            System.err.println("Warning: No active batches found for product " + product.getName()
                    + ". Stock added to product total only.");
        }

        product.setQuantity(product.getQuantity() + qtyToReturn);

        db.getReturns().remove(selectedReturn);

        returnsTable.clearSelection();
        selectedReturn = null;
    }

    @FXML
    private void handleDeleteDamaged() {
        if (selectedReturn == null) {
            String selectReturnedOrderWarningMsg = "Please select a returned order first.";
            Toast.showToast(selectReturnedOrderWarningMsg, Toast.NotificationType.WARNING);
            return;
        }
        String deleteReturnedOrderSuccessMsg = "Returned order discarded as damaged/waste.";
        Toast.showToast(deleteReturnedOrderSuccessMsg, Toast.NotificationType.INFORMATION);

        DataStore.getDataStore().getReturns().remove(selectedReturn);

        returnsTable.clearSelection();
        selectedReturn = null;
    }

    private void loadReturnsTable() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/com/hypermarket/view/components/TableView.fxml"));

        if (returnsTable == null) {
            returnsTable = new TableViewController<>(Return.class, DataStore.getDataStore().getReturns(), "returnID");
        }
        fxmlLoader.setController(returnsTable);

        formatTable();

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
            return p != null ? String.valueOf(p.getProductID()) : "";
        });

        returnsTable.setColumnFormatter("returnDate", obj -> {
            if (obj != null) {
                return ((LocalDateTime) obj).format(FileManager.localDateTimeFormat);
            }
            return "";
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
        if (returnsTable != null)
            returnsTable.clearSelection();
    }
}