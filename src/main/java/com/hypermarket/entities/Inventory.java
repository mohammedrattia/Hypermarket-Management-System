package com.hypermarket.entities;

import com.hypermarket.data.*; 
import com.hypermarket.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

        // If there's anything you want to ask about the code "Better Call Mada" , 01065145114

public class Inventory extends User {
    private ObservableList<Product> products;
    private ObservableList<DamageLog> damageLogs;
    private ObservableList<DamageLog> returnLogs;
    private ObservableList<Notification> notifications;

    public Inventory(String recordLine) {
        super(recordLine);
    }

    public Inventory(String role, int id, String fName, String lName, String image, String phone,
                     String email, String password, double salary) {

        super(role, id, fName, lName, image, phone, email, password, salary);
        DataStore ds = DataStore.getDataStore();
        this.products = ds.getProducts();
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

    public void addReturnLog(DamageLog returnings) {
        if (returnings != null) {
            this.returnLogs.add(returnings);
        }
    }

    public List<DamageLog> listReturnLogs() {
        return this.returnLogs;
    }

    public List<Product> checkLowStock() {
        List<Product> results = new ArrayList<>();
        
        for (Product product : products) {              
            if (product.getQuantity() < 5) {             
                results.add(product);                    
            }
        }
    
        return results;
    }

    // Still not finished and wont be soon i guess

    public List<Product> checkExpiryDates() {
        return new ArrayList<>();
    }

    public List<Notification> viewNotifications() {
        return this.notifications;
    }
}
