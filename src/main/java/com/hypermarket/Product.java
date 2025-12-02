package com.hypermarket;

public class Product {
    private String name;
    private String category;
    private double price;
    private int quantity;
    private String size;
    private String duration;
    private String imagePath;

    Product(String name, String category, double price, int quantity, String size, String duration, String imagePath) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.duration = duration;
        this.imagePath = imagePath;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getSize() { return size; }
    public String getDuration() { return duration; }
    public String getImagePath() { return imagePath; }
}
