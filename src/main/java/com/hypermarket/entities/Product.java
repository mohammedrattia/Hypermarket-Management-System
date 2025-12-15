package com.hypermarket.entities;

import com.hypermarket.data.FileManager;

public class Product {
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

    public Product(int productID, String name, String category, String description, int quantity, double price, String size, int threshold) {
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

    public Product(String name, String size, double price, String category, String description)
    {
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
               //! The offer thing needs to be fixed
               (offer != null ? offer.toString() : "null") + FileManager.DELIMETER +
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
            description = values[3];
            quantity = Integer.valueOf(values[4]);
            price = Double.valueOf(values[5]);
            /**Habiba will handle this value:
            offer = Offer.valueOf(values[6]);*/
            size = values[7];
            threshold = Integer.valueOf(values[8]);
            imageName = values[9];
        } catch (IllegalArgumentException e) {
            System.err.println("Error Entering Data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error Parsing Data: " + e.getMessage());
        }
    }

    public boolean isLowStock() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isLowStock'");
    }

    public int getTotalQuantity() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalQuantity'");
    }

    public boolean reduceStock(int quantity2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reduceStock'");
    }

    public int getThreshold() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getThreshold'");
    }
}
