package com.hypermarket.entities;

import java.util.List;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;
import com.hypermarket.service.ListManipulation;

import javafx.scene.chart.PieChart.Data;

public class OrderItem implements Parsable {

    private int orderItemID;
    private Order order;
    private Product product;
    private int quantity;
    private double priceThatDate;
    private int returnedItems;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.priceThatDate = product.getPrice();
    }

    public OrderItem(String recordLine) {
        parseString(recordLine);
    }

    @Override
    public String toString() {
        return orderItemID + FileManager.DELIMETER + order.getOrderID() + FileManager.DELIMETER + product.getProductID()
                + FileManager.DELIMETER + quantity
                + FileManager.DELIMETER + priceThatDate
                + FileManager.DELIMETER + returnedItems;
    }

    public void parseString(String line) {
        String[] values = line.split(FileManager.DELIMETER);
        // Look at the following examples and make the parseString Function
        try {
            orderItemID = Integer.parseInt(values[0]);
            order = ListManipulation.searchObjectWithID(DataStore.getDataStore().getOrders(), values[1]);
            order.getItems().add(this);
            product = ListManipulation.searchObjectWithID(DataStore.getDataStore().getProducts(), values[2]);
            quantity = Integer.parseInt(values[3]);
            priceThatDate = Double.parseDouble(values[4]);
            returnedItems = Integer.parseInt(values[5]);
        } catch (Exception e) {
            System.err.println("Error parsing data: " + e.getMessage());
        }
    }

    public int getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(int orderItemID) {
        this.orderItemID = orderItemID;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        priceThatDate = new Double(product.getPrice());
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReturnedItems() {
        return returnedItems;
    }

    public void setReturnedItems(int returnedItems) {
        this.returnedItems = returnedItems;
    }

    public double getPriceThatDate() {
        return priceThatDate;
    }

    public double getSubTotal() {
        return priceThatDate * quantity;
    }

}
