package com.hypermarket.modules.sales;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import com.hypermarket.modules.inventory.ProductsGrid;

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
    private ListView<?> itemsListSection;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // rootPane.getStylesheets().add(getClass().getResource("/css/global.css").toExternalForm());
        // rootPane.getStylesheets().add(getClass().getResource("/css/specific.css").toExternalForm());
        if (productsGrid == null)
            productsGrid = new ProductsGrid();
        // Parent view = productsGrid.getView();
        itemProductsList.getChildren().clear();
        try {
            itemProductsList.getChildren().add(productsGrid.getView());
        } catch (Exception e) {
            System.out.println("couldn't load products!!");
            System.err.println(e.toString());
        }
    }

    private void loadDashboardHome() {

    }
}
