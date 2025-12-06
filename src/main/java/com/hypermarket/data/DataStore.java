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

    public void loadAllDataBad() {
        for (String listKey : lists.keySet()) {
            lists.get(listKey).clear();
            ArrayList<String> records = FileManager.readFile(listKey);

            switch (listKey) {
                case "users":
                    for (String record : records) {
                        users.add(new User(record.trim()));
                        // if (record.toLowerCase().contains(",admin,"))
                        // users.add(new AdministeredObject(record.trim()));
                        // else if (record.toLowerCase().contains(",sales,"))
                        // users.add(new Sales(record.trim()));
                        // else if (record.toLowerCase().contains(",inventory,"))
                        // users.add(new Inventory(record.trim()));
                        // else if (record.toLowerCase().contains(",marketing,"))
                        // users.add(new Marketing(record.trim()));
                    }
                    break;
                case "products":
                    for (String record : records)
                        products.add(new Product(record.trim()));
                    break;
                case "orders":
                    for (String record : records)
                        // orders.add(new Order(record.trim()));
                        break;
                case "notifications":
                    for (String record : records)
                        // notifications.add(new Notification(record.trim()));
                        break;
                case "offers":
                    for (String record : records)
                        // offers.add(new Offer(record.trim()));
                        break;
                case "damageLogs":
                    for (String record : records)
                        // damageLogs.add(new DamageLog(record.trim()));
                        break;
                default:
                    System.out.println("Unknown List Type!!");
                    break;
            }
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
