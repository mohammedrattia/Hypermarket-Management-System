package com.hypermarket.entities;

import com.hypermarket.data.*;
import com.hypermarket.entities.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// If there's anything you want to ask about the code "Better Call Mada" , 01065145114

public class Inventory extends User {
    private ObservableList<Product> products;
    private ObservableList<Batch> batches;
    private ObservableList<DamageLog> damageLogs;
    private ObservableList<Notification> notifications;

    public Inventory(String recordLine) {
        super(recordLine);
        DataStore ds = DataStore.getDataStore();
        this.products = ds.getProducts();
        this.batches = ds.getBatches();
        this.damageLogs = ds.getDamageLogs();
        this.notifications = ds.getNotifications();
    }

    public Inventory(String role, int id, String fName, String lName, String image, String phone,
            String email, String password, double salary) {

        super(role, id, fName, lName, image, phone, email, password, salary);
        DataStore ds = DataStore.getDataStore();
        this.products = ds.getProducts();
        this.batches = ds.getBatches();
        this.damageLogs = ds.getDamageLogs();
        this.notifications = ds.getNotifications();
    }

    public void addProduct(Product newProduct) {
        if (newProduct != null) {
            this.products.add(newProduct);
            DataStore.getDataStore().saveAllData();
            System.out.println("Product added and saved to file.");
        }
    }

    public void updateProduct(String id, Product updateProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (String.valueOf(products.get(i).getProductID()).equals(id)) {
                products.set(i, updateProduct);
                DataStore.getDataStore().saveAllData();
                return;
            }
        }
        System.out.println("Product with ID " + id + " not found.");
    }

    public void deleteProduct(String id) {
        products.removeIf(p -> String.valueOf(p.getProductID()).equals(id));
        System.out.println("Product has been deleted");
    }

    public List<Product> listProducts() {
        return this.products;
    }

    public List<Product> searchProduct(String keyword) {
        ObservableList<Product> results = FXCollections.observableArrayList();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(product);
            }
        }
        return results;
    }

    public void addDamageLog(DamageLog damaged) {
        if (damaged != null) {
            this.damageLogs.add(damaged);
        }
    }

    public List<DamageLog> listDamageLogs() {
        return this.damageLogs;
    }

    public List<Batch> checkLowStock() {
        List<Batch> lowStockBatches = new ArrayList<>();
        for (Batch b : this.batches) {
            if (b.getQuantity() < b.getBatchThreshold()) {
                lowStockBatches.add(b);
            }
        }
        return lowStockBatches;
    }

    public List<Batch> checkExpiryDates() {
    LocalDate today = LocalDate.now();
    LocalDate sevenDaysFromNow = today.plusDays(7);
    
    List<Batch> nearExpiryBatches = new ArrayList<>();

    for (Batch b : this.batches) {
        LocalDate expiryDate = b.getExpiryDate();

        boolean isInFutureOrToday = !expiryDate.isBefore(today);
        boolean isWithinWeek = !expiryDate.isAfter(sevenDaysFromNow);

        if (isInFutureOrToday && isWithinWeek) {
            nearExpiryBatches.add(b);
        }
    }
    return nearExpiryBatches;
}

    public List<Notification> viewNotifications() {
        return this.notifications;
    }
}
