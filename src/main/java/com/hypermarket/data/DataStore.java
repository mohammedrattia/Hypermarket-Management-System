package com.hypermarket.data;

import java.util.HashMap;

import com.hypermarket.entities.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataStore {
    private static DataStore dataStore;

    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private ObservableList<Batch> batches = FXCollections.observableArrayList();
    private ObservableList<Order> orders = FXCollections.observableArrayList();
    private ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();
    private ObservableList<Return> returns = FXCollections.observableArrayList();
    private ObservableList<Offer> offers = FXCollections.observableArrayList();
    private ObservableList<Report> reports = FXCollections.observableArrayList();
    private ObservableList<DamageLog> damageLogs = FXCollections.observableArrayList();
    private HashMap<String, ObservableList<?>> lists = new HashMap<>();

    private DataStore() {
        lists.put("users", users);
        lists.put("products", products);
        lists.put("batches", batches);
        lists.put("orders", orders);
        lists.put("orderItems", orderItems);
        lists.put("returns", returns);
        lists.put("offers", offers);
        lists.put("reports", reports);
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
        loadData("users", users, (record) -> {
            if (record.toLowerCase().contains(FileManager.DELIMETER + "admin" + FileManager.DELIMETER))
                return new Admin(record.trim());
            else if (record.toLowerCase().contains(FileManager.DELIMETER + "sales" + FileManager.DELIMETER))
                return new Sales(record.trim());
            else if (record.toLowerCase().contains(FileManager.DELIMETER + "inventory" + FileManager.DELIMETER))
                return new Inventory(record.trim());
            else if (record.toLowerCase().contains(FileManager.DELIMETER + "marketing" + FileManager.DELIMETER))
                return new Marketing(record.trim());
            else
                return null;
        });
        loadData("products", products, (record) -> new Product(record));
        loadData("batches", batches, (record) -> new Batch(record));
        loadData("orders", orders, (record) -> new Order(record));
        loadData("orderItems", orderItems, (record) -> new OrderItem(record));
        loadData("returns", returns, (record) -> new Return(record));
        loadData("offers", offers, (record) -> new Offer(record));
        loadData("reports", reports, (record) -> new Report(record));
        loadData("damageLogs", damageLogs, (record) -> new DamageLog(record));

    }

    private <T> void loadData(String filename, ObservableList<T> list, Parser<T> parser) {
        ObservableList<String> records = FileManager.readFile(filename);
        if (records == null)
            return;
        list.clear();
        for (String record : records) {
            T object = parser.parse(record);
            list.add(object);
        }

    }

    // Get Reference Variable to the Original Lists
    public ObservableList<User> getUsers() {
        return users;
    }

    public ObservableList<Product> getProducts() {
        return products;
    }

    public ObservableList<Batch> getBatches() {
        return batches;
    }

    public ObservableList<Order> getOrders() {
        return orders;
    }

    public ObservableList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public ObservableList<Return> getReturns() {
        return returns;
    }

    public ObservableList<Offer> getOffers() {
        return offers;
    }

    public ObservableList<Report> getReports() {
        return reports;
    }

    public ObservableList<DamageLog> getDamageLogs() {
        return damageLogs;
    }
}
