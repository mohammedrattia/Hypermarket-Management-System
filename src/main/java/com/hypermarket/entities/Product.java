package com.hypermarket.entities;

public class Product {
    private String name;
    private String category;
    private double price;
    private double offer;
    private int quantity;
    private String size;
    private String duration;
    private String imagePath;
    private int productID;

    public Product(String record) {
        parseString(record);
    }

    public Product(int productID, String name, double price) {
        this.productID = productID;
        this.name = name;
        this.price = price;
    }

    public Product(String name, String category, double price, int quantity, String size, String duration,
            String imagePath,
            int productID, double offer) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.offer = offer;
        this.quantity = quantity;
        this.size = size;
        this.duration = duration;
        this.imagePath = imagePath;
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public double getOffer() {
        return offer;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }

    public String getDuration() {
        return duration;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getProductID() {
        return productID;
    }

    @Override
    public String toString() {
        return productID + "," + name + "," + price;
    }

    private void parseString(String line) {
        String[] values = line.split(",");
        this.productID = Integer.valueOf(values[0]);
        this.name = values[1];
        this.price = Double.valueOf(values[2]);
    }
}
