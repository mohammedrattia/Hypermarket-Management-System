package com.hypermarket.modules.sales;

import com.hypermarket.entities.OrderItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class OrderItemCard {

    @FXML
    private HBox orderItemCard; // Root element

    @FXML
    private Label itemName;

    @FXML
    private Label itemQuantity;

    @FXML
    private Label itemUnitPrice;

    @FXML
    private Label itemTotalPrice;

    public void setOrderItem(OrderItem item) {
        if (item == null)
            return;

        itemName.setText(item.getProduct().getName());
        itemQuantity.setText(String.valueOf(item.getQuantity()));
        itemUnitPrice.setText(String.format("%.2f", item.getPriceThatDate()));
        itemTotalPrice.setText(String.format("$ %,.2f", item.getSubTotal()));
    }
}