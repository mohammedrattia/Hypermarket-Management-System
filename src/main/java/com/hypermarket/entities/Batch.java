package com.hypermarket.entities;

import java.util.Date;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;
import com.hypermarket.service.ListManipulation;

public class Batch {

    private int batchID;
    private Product product;
    private int quantity;
    private Date deliveryDate;
    private Date expiryDate;

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

    public Date getExpiryDate() {
        return expiryDate;
    }

    @Override
    public String toString() {
        return batchID + FileManager.DELIMETER + 
               product.getProductID() + FileManager.DELIMETER +
               quantity + FileManager.DELIMETER + 
               FileManager.dateTimeFormat.format(deliveryDate) + 
               FileManager.DELIMETER + 
               FileManager.dateFormat.format(expiryDate);
    }

    private void parseString(String line) {
        String[] values = line.split(FileManager.DELIMETER);
        try {
            batchID = Integer.valueOf(values[0]);
            product = ListManipulation.searchObjectWithID(DataStore.getDataStore().getProducts(), values[1]); 
            quantity = Integer.valueOf(values[2]);
            deliveryDate = FileManager.dateTimeFormat.parse(values[3]);
            expiryDate = FileManager.dateFormat.parse(values[4]);
        } catch (IllegalArgumentException e) {
            System.err.println("Error Entering Data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error Parsing Data: " + e.getMessage());
        }
    }

}
