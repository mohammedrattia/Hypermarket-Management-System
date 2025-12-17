package com.hypermarket.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;

public class Offer {
    
    public enum Status { PENDING, ACTIVE, EXPIRED }

    private int offerID;
    private String offerName;
    private double discount;
    private Date startDate;
    private Date endDate;
    private String targetType;
    private String targetValue;
    private Product product;
    private Status manualStatus;

    public Offer(String recordLine) {
        parseString(recordLine);
    }

    public Offer(int offerID, String offerName, double discount, Date startDate,
            Date endDate, String targetType, String targetValue) {
        this.offerID = offerID;
        this.offerName = offerName;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetType = targetType;
        this.targetValue = targetValue;
    }

    public int getOfferID() {
        return offerID;
    }

    public String getOfferName() {
        return offerName;
    }

    public double getDiscount() {
        return discount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getTargetType() {
        return targetType;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public Product getProduct() {
        return product;
    }

    public Status getStatus() {
        if (manualStatus != null) return manualStatus;
        Date now = new Date();
        if (now.before(startDate)) return Status.PENDING;
        else if (now.after(endDate)) return Status.EXPIRED;
        else return Status.ACTIVE;
    }
    
    public void setManualStatus(Status status) {
        this.manualStatus = status;
    }

    public boolean isActive() {
        Date now = new Date();
        return now.after(startDate) && now.before(endDate);
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }



    @Override
    public String toString() {
        SimpleDateFormat sdf = FileManager.dateFormat1;
<<<<<<< Updated upstream
=======
        int productId = (product != null) ? product.getProductID() : -1;
        // TODO: add product id
>>>>>>> Stashed changes
        return offerID + FileManager.DELIMETER +
                offerName + FileManager.DELIMETER +
                discount + FileManager.DELIMETER +
                sdf.format(startDate) + FileManager.DELIMETER +
                sdf.format(endDate) + FileManager.DELIMETER +
                targetType + FileManager.DELIMETER +
                targetValue + FileManager.DELIMETER +
                productId;
    }

    private void parseString(String line) {
        try {
            String[] values = line.split(FileManager.DELIMETER);
            this.offerID = Integer.parseInt(values[0]);
            this.offerName = values[1];
            this.discount = Double.parseDouble(values[2]);
            this.startDate = FileManager.dateFormat1.parse(values[3]);
            this.endDate = FileManager.dateFormat1.parse(values[4]);
            this.targetType = values[5];
            this.targetValue = values[6]; 

            int productId = Integer.parseInt(values[7]);

        this.product = DataStore.getDataStore()
                .getProducts()
                .stream()
                .filter(p -> p.getProductID() == productId)
                .findFirst()
                .orElse(null);

        if (this.product != null) {
            this.product.setOffer(this);
        }
        } catch (Exception e) {
            System.err.println("Error parsing Offer: " + e.getMessage());
        }
    }
}
