package com.hypermarket.entities;

import java.util.Date;
import java.util.List;
import com.hypermarket.data.DataStore;

public class Marketing extends User {

    private final DataStore dataStore;

    public Marketing(String recordLine) {
        super(recordLine);
        this.dataStore = DataStore.getDataStore();
    }

    public Marketing(String role, int id, String fName, String lName, String image,
                     String phone, String email, String password, double salary) {
        super(role, id, fName, lName, image, phone, email, password, salary);
        this.dataStore = DataStore.getDataStore();
    }

    public DataStore getDataStore() { return dataStore; }

    public Offer createOffer(String offerName, double discount, Date startDate, Date endDate,String targetType,String targetValue) {
        
        int newID = dataStore.getOffers().size() + 1;

        Offer offer = new Offer(newID, offerName, discount, startDate, endDate, targetType , targetValue);

        dataStore.getOffers().add(offer);
        dataStore.saveAllData();
        return offer;
    }
    public Offer createOffer(String offerName, double discount, Date startDate, Date endDate, Product product) {
        int newID = dataStore.getOffers().size() + 1;

        Offer offer = new Offer(newID, offerName, discount, startDate, endDate,"product",String.valueOf(product.getProductID()));
        offer.setProduct(product);

        dataStore.getOffers().add(offer);
        dataStore.saveAllData();
        return offer;
    }

    
    public boolean editOffer(int offerID, String offerName, double discount, Date startDate, Date endDate,
                             String targetType, String targetValue) {
        for (Offer offer : dataStore.getOffers()) {
            if (offer.getOfferID() == offerID) {
                offer.setOfferName(offerName);
                offer.setDiscount(discount);
                offer.setStartDate(startDate);
                offer.setEndDate(endDate);
                offer.setTargetType(targetType);
                offer.setTargetValue(targetValue);
                dataStore.saveAllData();
                return true;
            }
        }
        return false;
    }
    public boolean deleteOffer(int offerID) {
    for (Offer offer : dataStore.getOffers()) {
        if (offer.getOfferID() == offerID) {
            dataStore.getOffers().remove(offer);
            dataStore.saveAllData();
            return true;
        }
    }
    return false;
}

//report section 


    private int countTotalOffers() {
        return dataStore.getOffers().size();
    }
    private int countActiveOffers() {
        int active = 0;
        for (Offer o : dataStore.getOffers()) {
            if (o.isActive()) {
                active++;
            }
        }
        return active;
    }
    private int countExpiredOffers() {
        int expired = 0;
        for (Offer o : dataStore.getOffers()) {
            if (!o.isActive()) {
                expired++;
            }
        }
        return expired;
    }
    private double findMaxDiscount() {
        double max = 0;
        for (Offer o : dataStore.getOffers()) {
            if (o.getDiscount() > max) {
                max = o.getDiscount();
            }
        }
        return max;
    }

    public Report generateOffersReport(String reportTitle) {
        List<Offer> offers = dataStore.getOffers();
        int total = offers.size();
        int active = 0;
        int expired = 0;
        double maxDiscount = 0;

        for (Offer offer : offers) {
            if (offer.isActive()) active++;
            else expired++;
            if (offer.getDiscount() > maxDiscount) maxDiscount = offer.getDiscount();
        }

        Report report = new Report(0, reportTitle, total, active, expired, maxDiscount, new Date(), "");
        dataStore.getReports().add(report);
        dataStore.saveAllData();
        return report;
    }

   
    public Report saveCustomReport(int id, String title, String content) {
    
    int totalOffers = countTotalOffers();
    int activeOffers = countActiveOffers();
    int expiredOffers = countExpiredOffers();
    double maxDiscountValue = findMaxDiscount();

    
    Report r = new Report(id, title, totalOffers, activeOffers, expiredOffers, maxDiscountValue, new Date(), content);

    
    dataStore.getReports().add(r);
    dataStore.saveAllData();
    return r;
}


}
