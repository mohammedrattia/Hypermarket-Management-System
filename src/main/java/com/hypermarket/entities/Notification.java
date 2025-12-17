package com.hypermarket.entities;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Notification {

    private String message;
    private LocalDate date;


    public String getMessage() {
        return message;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return message + FileManager.DELIMETER + date.format(FileManager.dateFormat);
    }

    public static List<String> getSystemAlerts() {
        List<String> alerts = new ArrayList<>();
        alerts.addAll(checkLowStock());
        alerts.addAll(checkExpiry());
        
        if (alerts.isEmpty()) {
            alerts.add("✅ System is Healthy. No alerts.");
        }
        return alerts;
    }

    private static List<String> checkLowStock() {
        List<String> lowStock = new ArrayList<>();
        for (Product p : DataStore.getDataStore().getProducts()) {
            if (p.getQuantity() <= p.getThreshold()) {
                lowStock.add("📉 LOW STOCK: " + p.getName() + " (Qty: " + p.getQuantity() + ")");
            }
        }
        return lowStock;
    }

    private static List<String> checkExpiry() {
        List<String> expiring = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Batch b : DataStore.getDataStore().getBatches()) {
            if (b.getExpiryDate() != null) {
                long days = ChronoUnit.DAYS.between(today, b.getExpiryDate());
                
                if (days < 0) {
                    expiring.add("❌ EXPIRED: " + b.getProduct().getName() + " (Batch " + b.getBatchID() + ")");
                } else if (days <= 7) {
                    expiring.add("⚠️ EXPIRING SOON: " + b.getProduct().getName() + " (" + days + " days left)");
                }
            }
        }
        return expiring;
    }
}