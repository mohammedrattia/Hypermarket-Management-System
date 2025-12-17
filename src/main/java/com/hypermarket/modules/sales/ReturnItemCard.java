package com.hypermarket.modules.sales;

import javafx.scene.control.Button;

import com.hypermarket.entities.OrderItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class ReturnItemCard {

    @FXML
    private Label itemName;
    @FXML
    private Label originalQuantity;
    @FXML
    private Label unitPrice;
    @FXML
    private Label originalTotalPrice;
    @FXML
    private Label prevReturnedQuantity;
    @FXML
    private Label refundSubtotal;
    @FXML
    private Spinner<Integer> toReturnField;
    @FXML
    private Button maxButton;

    private OrderItem item;
    Runnable onQuantityChange;

    public void setOrderItem(OrderItem item, Runnable onQuantityChange) {
        this.item = item;
        this.onQuantityChange = onQuantityChange;

        itemName.setText(item.getProduct().getName());
        originalQuantity.setText(String.valueOf(item.getQuantity()));
        unitPrice.setText(String.format("$ %,.2f", item.getPriceThatDate()));

        double originalTotal = item.getQuantity() * item.getPriceThatDate();
        originalTotalPrice.setText(String.format("$ %,.2f", originalTotal));
        updateRefundLabel(0);

        prevReturnedQuantity.setText(String.valueOf(item.getReturnedItems()));

        int maxReturnable = item.getQuantity() - item.getReturnedItems();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxReturnable,
                0);
        toReturnField.setValueFactory(valueFactory);

        initListeners();
    }

    private void initListeners() {
        toReturnField.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateRefundLabel(newVal);
            if (onQuantityChange != null)
                onQuantityChange.run();
        });
        maxButton.setOnAction(event -> setMaxQuantity());

    }

    private void updateRefundLabel(int quantity) {
        double refund = quantity * item.getPriceThatDate();
        refundSubtotal.setText(String.format("$ %,.2f", refund));
    }

    public int getReturnAmount() {
        return toReturnField.getValue();
    }

    public OrderItem getOrderItem() {
        return item;
    }

    public void setMaxQuantity() {
        int max = item.getQuantity() - item.getReturnedItems();
        toReturnField.getValueFactory().setValue(max);
    }

    public void resetQuantity() {
        toReturnField.getValueFactory().setValue(0);
    }
}