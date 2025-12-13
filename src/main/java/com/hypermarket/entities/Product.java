package com.hypermarket.entities;

import com.hypermarket.data.FileManager;

public class Product {
    private int productID;
    private String name;
    private String category;
    private int quantity;
    private double price;
    private Offer offer;
    private String size;
    private int threshold;
    private String imageName;

    public Product(String record) {
        parseString(record);
    }

    public Product(int productID, String name, String category, int quantity, double price, String size, int threshold) {
        this.productID = productID;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.size = size;
        this.threshold = threshold;
        this.imageName = "image_" + this.productID;
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

    public int getThreshold() {
        return threshold;
    }

    public String getImageName() {
        return imageName;
    }

    @Override
    public String toString() {
        return productID + FileManager.DELIMETER + 
               name + FileManager.DELIMETER + 
               category + FileManager.DELIMETER +
               quantity + FileManager.DELIMETER +
               price + FileManager.DELIMETER +
               offer.toString() + FileManager.DELIMETER +
               size + FileManager.DELIMETER +
               threshold + FileManager.DELIMETER + 
               imageName;
    }

    private void parseString(String line) {
        String[] values = line.split(FileManager.DELIMETER);
        try {
            productID = Integer.valueOf(values[0]);
            name = values[1];
            category = values[2];
            quantity = Integer.valueOf(values[3]);
            price = Double.valueOf(values[4]);
            /**Habiba will handle this value:
            offer = Offer.valueOf(values[5]);*/
            size = values[6];
            threshold = Integer.valueOf(values[7]);
            imageName = values[8];
        } catch (IllegalArgumentException e) {
            System.err.println("Error Entering Data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error Parsing Data: " + e.getMessage());
        }
    }
}
