package com.hypermarket.entities;

import java.time.LocalDateTime;
import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;
import com.hypermarket.service.ListManipulation;

import javafx.collections.ObservableList;

public class Return implements Parsable {

    private int returnID;
    private OrderItem orderItem;
    private Product product;
    private LocalDateTime returnDate;
    private int quantityReturned;
    private double refundAmount;

    public Return(OrderItem orderItem, int quantityReturned) {
        ObservableList<Return> returnsList = DataStore.getDataStore().getReturns();
        if (returnsList.isEmpty()) {
            this.returnID = 1;
        } else {
            this.returnID = returnsList.get(returnsList.size() - 1).getReturnID() + 1;
        }

        this.orderItem = orderItem;
        this.product = orderItem.getProduct();
        this.quantityReturned = quantityReturned;
        this.returnDate = LocalDateTime.now();
        this.refundAmount = quantityReturned * orderItem.getPriceThatDate();
        returnsList.add(this);
    }

    public Return(String recordLine) {
        parseString(recordLine);
    }

    @Override
    public String toString() {
        return returnID + FileManager.DELIMETER +
                orderItem.getOrderItemID() + FileManager.DELIMETER +
                returnDate.format(FileManager.dateTimeFormat) + FileManager.DELIMETER +
                quantityReturned + FileManager.DELIMETER +
                refundAmount;
    }

    public void parseString(String line) {
        String[] values = line.split(FileManager.DELIMETER);

        try {
            this.returnID = Integer.parseInt(values[0]);
            this.orderItem = (OrderItem) ListManipulation.searchObjectWithID(
                    DataStore.getDataStore().getOrderItems(), values[1]);
            this.product = orderItem.getProduct();
            this.returnDate = LocalDateTime.parse(values[2], FileManager.dateTimeFormat);
            this.quantityReturned = Integer.parseInt(values[3]);
            this.refundAmount = Double.parseDouble(values[4]);

        } catch (Exception e) {
            System.err.println("Error parsing data: " + e.getMessage());
        }
    }

    public int getReturnID() {
        return returnID;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public Product getProduct() {
        return product;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public int getQuantityReturned() {
        return quantityReturned;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setQuantityReturned(int quantityReturned) {
        this.quantityReturned = quantityReturned;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

}