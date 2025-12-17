package com.hypermarket.entities;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;

import javafx.collections.ObservableList;

public class Product implements Parsable {
    private int productID;
    private String name;
    private String category;
    private String description;
    private int quantity;
    private double price;
    private Offer offer;
    private String size;
    private int threshold;
    private String imageName;

    public Product(String record) {
        parseString(record);
    }

    public Product(int productID, String name, String category, String description, int quantity, double price,
            String size, int threshold) {
        this.productID = productID;
        this.name = name;
        this.category = category;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.size = size;
        this.threshold = threshold;
        this.imageName = "image_" + this.productID;
    }

    public Product(String name, String size, double price, String category, String description) {
        this.name = name;
        this.size = size;
        this.price = price;
        this.category = category;
        this.description = description;
    }

    public int getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public String getSize() {
        return size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }

    @Override
    public String toString() {
        return productID + FileManager.DELIMETER +
                name + FileManager.DELIMETER +
                category + FileManager.DELIMETER +
                description + FileManager.DELIMETER +
                quantity + FileManager.DELIMETER +
                price + FileManager.DELIMETER +
                (offer != null ? offer.toString() : "null") + FileManager.DELIMETER +
                size + FileManager.DELIMETER +
                threshold + FileManager.DELIMETER +
                imageName;
    }

    public void parseString(String line) {
        String[] values = line.split(FileManager.DELIMETER);

        try {
            productID = Integer.parseInt(values[0]);
            name = values[1];
            category = values[2];
            description = values[3];
            quantity = (int) Double.parseDouble(values[4]);
            price = Double.parseDouble(values[5]);
            size = values[7];
            threshold = (int) Double.parseDouble(values[8]);

            imageName = values.length > 9 ? values[9] : "image_" + productID;

            // safety
            if (imageName == null || imageName.equalsIgnoreCase("null")) {
                imageName = "image_" + productID;
            }

        } catch (Exception e) {
            System.err.println("Error parsing product line: " + line);
            e.printStackTrace();
        }
    }

    public boolean isLowStock() {
        return quantity <= threshold;
    }

    public boolean reduceStock(int amount) {
        if (amount > this.quantity) {
            return false;
        }

        ObservableList<Batch> allBatches = DataStore.getDataStore().getBatches();
        List<Batch> productBatches = allBatches.stream()
                .filter(b -> b.getProduct().getProductID() == this.productID)
                .sorted(Comparator.comparing(Batch::getExpiryDate))
                .collect(Collectors.toList());

        int remainingToDeduct = amount;

        for (Batch batch : productBatches) {
            if (remainingToDeduct <= 0)
                break;

            int currentBatchQty = batch.getQuantity();
            int deductFromBatch = Math.min(currentBatchQty, remainingToDeduct);

            batch.setQuantity(currentBatchQty - deductFromBatch);
            remainingToDeduct -= deductFromBatch;
        }

        this.quantity -= amount;

        allBatches.removeIf(b -> b.getQuantity() <= 0);

        return true;
    }

    public int getThreshold() {
        return threshold;
    }
}
