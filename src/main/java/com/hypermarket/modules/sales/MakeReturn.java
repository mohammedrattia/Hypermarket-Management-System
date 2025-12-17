package com.hypermarket.modules.sales;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hypermarket.entities.Order;
import com.hypermarket.entities.OrderItem;
import com.hypermarket.entities.Sales;
import com.hypermarket.service.Session;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MakeReturn implements Initializable {

    @FXML
    private Button returnAllButton;
    @FXML
    private Button clearSelectionButton;
    @FXML
    private ListView<HBox> returnItemsList; // Stores the physical card nodes

    // Order Details Section
    @FXML
    private Label orderDetailID;
    @FXML
    private Label orderDetailAmount;
    @FXML
    private Label orderDetailQuantity;
    @FXML
    private DatePicker orderDetailDate;

    // Totals & Actions
    @FXML
    private Label totalReturnQuantityLabel;
    @FXML
    private Label totalRefundLabel;
    @FXML
    private Button exitButton;
    @FXML
    private Button confirmReturnButton;

    private Order currentOrder;
    // We keep a list of the controllers to access their data (spinners) later
    private ObservableList<ReturnItemCard> cardControllers = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initListeners();
    }

    // --- 1. SETUP ---
    public void setOrder(Order order) {
        this.currentOrder = order;

        orderDetailID.setText(String.valueOf(order.getOrderID()));
        orderDetailAmount.setText(String.format("$ %,.2f", order.getTotalPrice()));
        orderDetailQuantity.setText(String.valueOf(order.getTotalQuantity()));
        if (order.getDateTime() != null) {
            orderDetailDate.setValue(order.getDateTime().toLocalDate());
        }

        System.out.println("Start Loading Cards");
        loadItemsCards();
    }

    private void loadItemsCards() {
        returnItemsList.getItems().clear();
        cardControllers.clear();
        System.out.println("clear list");

        for (OrderItem item : currentOrder.getItems()) {
            if (item.getQuantity() > item.getReturnedItems()) {
                try {
                    System.out.println("load item: #" + item.getOrderItemID());
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/com/hypermarket/view/sales/ReturnItemCard.fxml"));
                    HBox itemCard = loader.load();
                    ReturnItemCard controller = loader.getController();
                    controller.setOrderItem(item, this::calculateTotals);

                    cardControllers.add(controller);
                    returnItemsList.getItems().add(itemCard);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Done Loading Cards");
    }

    private void calculateTotals() {
        int totalQuantity = 0;
        double totalRefund = 0;

        for (ReturnItemCard card : cardControllers) {
            int quantity = card.getReturnAmount();
            if (quantity > 0) {
                totalQuantity += quantity;
                totalRefund += quantity * card.getOrderItem().getPriceThatDate();
            }
        }

        totalReturnQuantityLabel.setText(String.valueOf(totalQuantity));
        totalRefundLabel.setText(String.format("$ %,.2f", totalRefund));
    }

    private void initListeners() {
        exitButton.setOnAction(e -> closeWindow());

        clearSelectionButton.setOnAction(e -> {
            for (ReturnItemCard card : cardControllers) {
                card.resetQuantity();
            }
        });

        returnAllButton.setOnAction(e -> {
            for (ReturnItemCard card : cardControllers) {
                card.setMaxQuantity();
            }
        });

        confirmReturnButton.setOnAction(e -> handleConfirmReturn());
    }

    private void handleConfirmReturn() {
        boolean hasReturns = false;

        for (ReturnItemCard card : cardControllers) {
            int quantityToReturn = card.getReturnAmount();

            if (quantityToReturn > 0) {
                hasReturns = true;
                OrderItem item = card.getOrderItem();
                ((Sales) Session.getInstance().getUser()).makeReturn(item, quantityToReturn);
            }
        }

        if (hasReturns) {
            System.out.println("Returns processed successfully.");
            closeWindow();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}