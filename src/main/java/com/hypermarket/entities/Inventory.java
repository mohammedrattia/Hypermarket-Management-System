package com.hypermarket.entities;

import com.hypermarket.data.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

// If there's anything you want to ask about the code "Better Call Mada" , 01065145114

public class Inventory extends User {
    private ObservableList<Product> products;
    private ObservableList<Batch> batches;

    public Inventory(String recordLine) {
        super(recordLine);
        DataStore ds = DataStore.getDataStore();
        this.products = ds.getProducts();
        this.batches = ds.getBatches();
    }

    public Inventory(String role, int id, String fName, String lName, String image, String phone,
            String email, String password, double salary) {

        super(role, id, fName, lName, image, phone, email, password, salary);
        DataStore ds = DataStore.getDataStore();
        this.products = ds.getProducts();
        this.batches = ds.getBatches();
    }

    public void addProduct(Product newProduct) {
        if (newProduct != null) {
            this.products.add(newProduct);
            DataStore.getDataStore().saveAllData();
            System.out.println("Product added and saved to file.");
        }
    }

    public int generateNextProductId() {
        int maxId = 0;
        if (products != null) {
            for (Product p : products) {
                if (p.getProductID() > maxId) {
                    maxId = p.getProductID();
                }
            }
        }
        return maxId + 1;
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
}
