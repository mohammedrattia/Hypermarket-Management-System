package com.hypermarket.entities;

import java.time.LocalDate;
import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;
import com.hypermarket.service.ListManipulation;

public class Batch implements Parsable {

    private int batchID;
    private Product product;
    private int quantity;
    private LocalDate deliveryDate;
    private LocalDate expiryDate;

    public Batch(String record) {
        parseString(record);
    }

    public int getBatchID() {
        return batchID;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public int getBatchThreshold() {
        return product.getThreshold();
    }

    @Override
    public String toString() {
        return batchID + FileManager.DELIMETER +
                product.getProductID() + FileManager.DELIMETER +
                quantity + FileManager.DELIMETER +
                deliveryDate.format(FileManager.dateFormat) +
                FileManager.DELIMETER +
                expiryDate.format(FileManager.dateFormat);
    }

    public void parseString(String line) {
        String[] values = line.split(FileManager.DELIMETER);
        try {
            batchID = Integer.valueOf(values[0]);
            product = ListManipulation.searchObjectWithID(DataStore.getDataStore().getProducts(), values[1]);
            quantity = Integer.valueOf(values[2]);
            deliveryDate = LocalDate.parse(values[3], FileManager.dateFormat);
            expiryDate = LocalDate.parse(values[4], FileManager.dateFormat);
        } catch (IllegalArgumentException e) {
            System.err.println("Error Entering Data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error Parsing Data: " + e.getMessage());
        }
    }

}
