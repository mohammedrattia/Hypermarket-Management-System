package com.hypermarket.modules.sales;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.Order;
import com.hypermarket.entities.OrderItem;
import com.hypermarket.entities.Product;
import com.hypermarket.entities.Sales;
import com.hypermarket.modules.inventory.ProductsGrid;
import com.hypermarket.service.ListManipulation;
import com.hypermarket.service.Session;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MakeOrder implements Initializable {
    @FXML
    private Button addUpdateButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button clearButton;

    @FXML
    private TextField itemProductID;

    @FXML
    private TextField itemProductName;

    @FXML
    private TextField itemProductQuantity;

    @FXML
    private VBox itemProductsList;

    @FXML
    private ListView<OrderItem> itemsListSection;

    @FXML
    private HBox mainContainer;

    @FXML
    private VBox orderActionsSection;

    @FXML
    private Label orderDetailAmount;

    @FXML
    private DatePicker orderDetailDate;

    @FXML
    private Label orderDetailID;

    @FXML
    private Label orderDetailQuantity;

    @FXML
    private Button purchaseButton;

    @FXML
    private Button removeButton;

    @FXML
    private VBox sideBarPanel;

    @FXML
    private HBox topBarButtons;

    @FXML
    private HBox topBarItemInfo;

    @FXML
    private VBox totalsSection;

    @FXML
    private Button viewReceiptButton;

    ProductsGrid productsGrid;

    Product selectedProduct;
    OrderItem selectedOrderItem;
    Order currentOrder;
    // Order currentOrder = DataStore.getDataStore().getOrders().get(4);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadOrder();
        loadProductsGrid();
        initListeners();
    }

    private void initListeners() {
        productsGrid.getSelectedProductProperty().addListener((obs, oldVal, newVal) -> {
            selectedProduct = newVal;
            fillProductInfo(selectedProduct);
            // System.out.println("Selected Product: " + selectedProduct.getName());
        });
        getSelectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedOrderItem = newVal;
            fillProductInfo(selectedOrderItem);
        });
        purchaseButton.setOnMouseClicked(event -> handlePurchase());
        cancelButton.setOnMouseClicked(event -> handleCancelOrder());
        viewReceiptButton.setOnMouseClicked(event -> currentOrder.printReceipt());
        addUpdateButton.setOnMouseClicked(event -> addUpdateItem());
        clearButton.setOnMouseClicked(event -> clearItems());
        removeButton.setOnMouseClicked(event -> removeItem());
    }

    private void handlePurchase() {
        try {
            System.out.println("Purchased");
            currentOrder.purchase();
            startNewOrder();
            System.out.println("Everything Cleared");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleCancelOrder() {
        startNewOrder();
    }

    private void startNewOrder() {
        currentOrder = ((Sales) Session.getInstance().getUser()).MakeOrder();
        clearItems();
        clearInputFields();
        bindOrderDetails();
        bindOrderItems();
    }

    private void loadOrder() {
        if (currentOrder == null) {
            currentOrder = ((Sales) Session.getInstance().getUser()).MakeOrder();
        }
        // System.out.println("Order Total items: " + currentOrder.getItems().size());
        bindOrderDetails();
        bindOrderItems();
    }

    private void addUpdateItem() {
        int productQuantity = Integer.parseInt(itemProductQuantity.getText());
        int productID = Integer.parseInt(itemProductID.getText());

        Product chosenProduct = null;
        try {
            chosenProduct = ListManipulation.searchObjectWithID(DataStore.getDataStore().getProducts(),
                    String.valueOf(productID));
        } catch (Exception e) {
            System.out.println("Product not found");
            return;
        }
        // if (chosenProduct == null) {
        // System.out.println("Product not found");
        // }

        if (productQuantity <= 0) {
            System.out.println("Quantity must be > 0");
            return;
        }
        if (productQuantity > chosenProduct.getQuantity()) {
            System.out.println("Not enough stock (Max: " + chosenProduct.getQuantity() + ")");
            return;
        }
        OrderItem existingItem = findItemByProduct(chosenProduct);

        if (existingItem != null) {
            existingItem.setQuantity(productQuantity);
        } else {
            currentOrder.addItem(chosenProduct, productQuantity);
        }
        clearInputFields();
    }

    private void clearItems() {
        currentOrder.getItems().clear();
        currentOrder.calculateTotalPrice();
        currentOrder.calculateTotalQuantity();
    }

    private void removeItem() {
        currentOrder.getItems().remove(selectedOrderItem);
    }

    private void clearInputFields() {
        itemProductID.clear();
        itemProductName.clear();
        itemProductQuantity.clear();
    }

    private OrderItem findItemByProduct(Product product) {
        for (OrderItem item : currentOrder.getItems()) {
            if (item.getProduct().equals(product))
                return item;
        }
        return null;
    }

    private void bindOrderDetails() {

        orderDetailID.setText(String.valueOf(currentOrder.getOrderID()));

        if (currentOrder.getDateTime() != null) {
            orderDetailDate.setValue(currentOrder.getDateTime().toLocalDate());
            orderDetailDate.setMouseTransparent(true);
            orderDetailDate.setFocusTraversable(false);
        }

        orderDetailAmount.textProperty().bind(Bindings.createStringBinding(
                () -> String.format("$ %,.2f", currentOrder.calculateTotalPrice()),
                currentOrder.getItems()));
        orderDetailQuantity.textProperty().bind(Bindings.createStringBinding(
                () -> String.valueOf(currentOrder.calculateTotalQuantity()),
                currentOrder.getItems()));
    }

    private void bindOrderItems() {
        // itemsListSection.setItems(currentOrder.getItems());
        // itemsListSection.setCellFactory(listView -> createOrderItemCell());
        clearSelection();
        itemsListSection.setItems(currentOrder.getItems());
        itemsListSection.setCellFactory(listView -> new OrderItemCell());
    }

    private void loadProductsGrid() {
        if (productsGrid == null)
            productsGrid = new ProductsGrid();
        itemProductsList.getChildren().clear();
        try {
            itemProductsList.getChildren().add(productsGrid.getView());
        } catch (Exception e) {
            System.out.println("couldn't load products!!");
            System.err.println(e.toString());
        }
    }

    private void fillProductInfo(Product product) {
        itemProductID.setText(String.valueOf(product.getProductID()));
        itemProductName.setText(product.getName());
        itemProductQuantity.setText(String.valueOf(setItemProductQuantity(product)));
    }

    private void fillProductInfo(OrderItem orderItem) {
        itemProductID.setText(String.valueOf(orderItem.getProduct().getProductID()));
        itemProductName.setText(orderItem.getProduct().getName());
        itemProductQuantity.setText(String.valueOf(setItemProductQuantity(orderItem.getProduct())));
    }

    private int setItemProductQuantity(Product product) {
        for (OrderItem item : currentOrder.getItems()) {
            if (item.getProduct().equals(product)) {
                return item.getQuantity();
            }
        }
        return 1;
    }

    private class OrderItemCell extends ListCell<OrderItem> {

        private Node graphic;
        private OrderItemCard controller;

        public OrderItemCell() {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/hypermarket/view/sales/OrderItemCard.fxml"));
                graphic = loader.load();
                controller = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
                setText("Error loading card");
                System.out.println("Couldn't Load the cards");
            }
        }

        @Override
        protected void updateItem(OrderItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                controller.setOrderItem(item);
                setText(null);
                setGraphic(graphic);
            }
        }
    }

    public ReadOnlyObjectProperty<OrderItem> getSelectedItemProperty() {
        return itemsListSection.getSelectionModel().selectedItemProperty();
    }

    public OrderItem getSelectedItem() {
        return itemsListSection.getSelectionModel().getSelectedItem();
    }

    public void clearSelection() {
        itemsListSection.getSelectionModel().clearSelection();
    }

}
