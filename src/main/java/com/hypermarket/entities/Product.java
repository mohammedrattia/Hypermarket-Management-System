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
        //jawn jawn
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



    public void parseString(String line) {
        String[] parts = line.split(";");

        try {
            this.productID = Integer.parseInt(parts[0]); // int
            this.name = parts[1];                        // String
            this.category = parts[2];                    // String
            this.price = Double.parseDouble(parts[3]);   // double
            this.offer = Double.parseDouble(parts[4]);   // double
            this.quantity = Integer.parseInt(parts[5]);  // int
            this.size = parts[6];                        // String
            this.duration = parts[7];                    // String
            this.imagePath = parts[8];                   // String
        } catch (Exception e) {
            System.err.println("Error parsing product: " + line);
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return productID + ";" + 
            name + ";" + 
            category + ";" + 
            price + ";" + 
            offer + ";" + 
            quantity + ";" + 
            size + ";" + 
            duration + ";" + 
            imagePath;
    }
}
