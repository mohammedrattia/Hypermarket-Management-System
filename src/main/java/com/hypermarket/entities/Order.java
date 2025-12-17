package com.hypermarket.entities;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;
import com.hypermarket.modules.components.ReceiptPrinter;
import com.hypermarket.service.ListManipulation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Order {

    private int orderID;
    private LocalDateTime dateTime;
    private int totalQuantity;
    private double totalPrice;
    private Sales seller;
    private ObservableList<OrderItem> items = FXCollections.observableArrayList();

    public Order(Sales seller) {
        List<Order> orders = DataStore.getDataStore().getOrders();
        if (orders.isEmpty())
            this.orderID = 1;
        else
            this.orderID = orders.get(orders.size() - 1).getOrderID() + 1;
        this.dateTime = LocalDateTime.now();
        this.seller = seller;
    }

    public Order(String recordLine) {
        parseString(recordLine);
    }

    @Override
    public String toString() {
        return orderID + FileManager.DELIMETER + seller.getID() + FileManager.DELIMETER
                + dateTime.format(FileManager.dateTimeFormat) + FileManager.DELIMETER + totalQuantity
                + FileManager.DELIMETER + totalPrice;
    }

    private void parseString(String line) {
        String[] values = line.split(FileManager.DELIMETER);
        // Look at the following examples and make the parseString Function
        try {
            orderID = Integer.parseInt(values[0]);
            seller = (Sales) ListManipulation.searchObjectWithID(DataStore.getDataStore().getUsers(), values[1]);
            dateTime = LocalDateTime.parse(values[2], FileManager.dateTimeFormat);
            totalQuantity = Integer.parseInt(values[3]);
            totalPrice = Double.parseDouble(values[4]);
        } catch (IllegalArgumentException e) {
            System.err.println("Error Chosing Role: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error parsing data: " + e.getMessage());
        }
    }

    public int getOrderID() {
        return orderID;
    }

    public Sales getSeller() {
        return seller;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ObservableList<OrderItem> getItems() {
        return items;
    }

    public boolean addItem(Product product, int quantity) {
        // if (product.isLowStock() || product.getTotalQuantity() - quantity <
        // product.getThreshold()) {
        if (quantity > product.getQuantity()) {
            return false;
        }
        OrderItem newItem = new OrderItem(product, quantity);
        newItem.setOrder(this);
        this.items.add(newItem);
        calculateTotalPrice();
        calculateTotalQuantity();
        return true;
    }

    public void deleteItem(OrderItem item) {
        this.items.remove(item);
        calculateTotalPrice();
        calculateTotalQuantity();
    }

    public double calculateTotalPrice() {
        this.totalPrice = 0;
        for (OrderItem item : items) {
            this.totalPrice += item.getSubTotal();
        }
        return this.totalPrice;
    }

    public int calculateTotalQuantity() {
        this.totalQuantity = 0;
        for (OrderItem item : items) {
            this.totalQuantity += item.getQuantity();
        }
        return this.totalQuantity;
    }

    public void printReceipt() {
        ReceiptPrinter.printToPDF(this);
    }

    public void purchase() throws Exception {
        for (OrderItem item : items) {
            if (item.getQuantity() > item.getProduct().getQuantity()) {
                throw new Exception("Not enough stock for: " + item.getProduct().getName());
            }
        }

        for (OrderItem item : items) {
            List<OrderItem> orderItems = DataStore.getDataStore().getOrderItems();
            if (orderItems.isEmpty())
                item.setOrderItemID(1);
            else
                item.setOrderItemID(orderItems.get(orderItems.size() - 1).getOrderItemID() + 1);
            item.getProduct().reduceStock(item.getQuantity());
            orderItems.add(item);
        }
        DataStore.getDataStore().getOrders().add(this);
    }

}
