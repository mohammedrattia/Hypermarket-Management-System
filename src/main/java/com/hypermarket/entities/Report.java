package com.hypermarket.entities;

import java.util.Date;
import com.hypermarket.data.FileManager;

public class Report {

    private int reportID;
    private String reportTitle;
    private int totalOffers;
    private int activeOffers;
    private int expiredOffers;
    private double maxDiscount;
    private Date creationDate;
    private String content;

    
    public Report(int reportID, String reportTitle, int totalOffers, int activeOffers,
                  int expiredOffers, double maxDiscount, Date creationDate, String content) {
        this.reportID = reportID;
        this.reportTitle = reportTitle;
        this.totalOffers = totalOffers;
        this.activeOffers = activeOffers;
        this.expiredOffers = expiredOffers;
        this.maxDiscount = maxDiscount;
        this.creationDate = creationDate;
        this.content = content;
    }

    
    public Report(String recordLine) {
        try {
            String[] values = recordLine.split(FileManager.DELIMETER);
            this.reportID = Integer.parseInt(values[0]);
            this.reportTitle = values[1];
            this.totalOffers = Integer.parseInt(values[2]);
            this.activeOffers = Integer.parseInt(values[3]);
            this.expiredOffers = Integer.parseInt(values[4]);
            this.maxDiscount = Double.parseDouble(values[5]);
            this.creationDate = new Date(Long.parseLong(values[6]));
            this.content = (values.length > 7) ? values[7] : "";
        } catch (Exception e) {
            System.err.println("Error parsing Report: " + e.getMessage());
        }
    }

    
    public Report(String content, String author) {
        this.reportID = 0;
        this.reportTitle = "Custom Marketing Report - " + java.time.LocalDateTime.now();
        this.totalOffers = 0;
        this.activeOffers = 0;
        this.expiredOffers = 0;
        this.maxDiscount = 0;
        this.creationDate = new Date();
        this.content = content;
    }

    
    public int getReportID() { return reportID; }
    public String getReportTitle() { return reportTitle; }
    public int getTotalOffers() { return totalOffers; }
    public int getActiveOffers() { return activeOffers; }
    public int getExpiredOffers() { return expiredOffers; }
    public double getMaxDiscount() { return maxDiscount; }
    public Date getCreationDate() { return creationDate; }
    public String getContent() { return content; }

    @Override
    public String toString() {
        return reportID + FileManager.DELIMETER +
               reportTitle + FileManager.DELIMETER +
               totalOffers + FileManager.DELIMETER +
               activeOffers + FileManager.DELIMETER +
               expiredOffers + FileManager.DELIMETER +
               maxDiscount + FileManager.DELIMETER +
               creationDate.getTime() + FileManager.DELIMETER +
               content;
    }
}
