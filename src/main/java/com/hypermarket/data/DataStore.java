package com.hypermarket.data;

import java.util.ArrayList;
import java.util.HashMap;

import com.hypermarket.entities.*;

public class DataStore {
    private static DataStore dataStore;

    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<Product> products = new ArrayList<Product>();
    private ArrayList<Order> orders = new ArrayList<Order>();
    private ArrayList<Notification> notifications = new ArrayList<Notification>();
    private ArrayList<Offer> offers = new ArrayList<Offer>();
    private ArrayList<DamageLog> damageLogs = new ArrayList<DamageLog>();
    private HashMap<String, ArrayList<?>> lists = new HashMap<>();

    private DataStore() {
        lists.put("users", users);
        lists.put("products", products);
        lists.put("orders", orders);
        lists.put("notifications", notifications);
        lists.put("offers", offers);
        lists.put("damageLogs", damageLogs);
    }

    public static DataStore getDataStore() {
        if (dataStore == null) {
            dataStore = new DataStore();
            dataStore.loadAllData();
        }
        return dataStore;
    }

    public void saveAllData() {
        for (String listKey : lists.keySet()) {
            FileManager.writeFile(listKey, lists.get(listKey));
        }
    }

    public void loadAllData() {
        loadData("products", products, (record) -> new Product(record));
        // loadData("orders", orders, (record) -> new Order(record));
        loadData("users", users, (record) -> {
            return new User(record.trim());
            // if (record.toLowerCase().contains(",admin,"))
            // return new Admin(record.trim());
            // else if (record.toLowerCase().contains(",sales,"))
            // return new Sales(record.trim());
            // else if (record.toLowerCase().contains(",inventory,"))
            // return new Inventory(record.trim());
            // else if (record.toLowerCase().contains(",marketing,"))
            // return new Marketing(record.trim());
        });
        // loadData("offers", offers, (record) -> new Offer(record));
        // loadData("notification", notifications, (record) -> new
        // Notification(record));
        // loadData("damageLogs", damageLogs, (record) -> new DamageLog(record));

    }

    private <T> void loadData(String filename, ArrayList<T> list, Parser<T> parser) {
        ArrayList<String> records = FileManager.readFile(filename);
        if (records == null)
            return;
        list.clear();
        for (String record : records) {
            T object = parser.parse(record);
            list.add(object);
        }

    }

    // Get Reference Variable to the Original Lists
    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public ArrayList<DamageLog> getDamageLogs() {
        return damageLogs;
    }
}
