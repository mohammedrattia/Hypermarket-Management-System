package com.hypermarket.modules.inventory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Date;
import com.hypermarket.entities.Batch;
import com.hypermarket.entities.Inventory;
import com.hypermarket.entities.User;
import com.hypermarket.service.Session;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class NearExpiryController implements Initializable {

    @FXML
    private TableView<Batch> nearExpiryBatches;

    @FXML
    private TableColumn<Batch, Integer> batchIDColumn;

    @FXML
    private TableColumn<Batch, Integer> quantityColumn;

    @FXML
    private TableColumn<Batch, Date> expiryDateColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Setup Columns
        batchIDColumn.setCellValueFactory(new PropertyValueFactory<>("batchID"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        expiryDateColumn.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        // 2. Get the current user from the Session
        User currentUser = Session.getInstance().getUser();

        // 3. Check if the user is actually an Inventory Manager
        if (currentUser instanceof Inventory) {
            Inventory inv = (Inventory) currentUser;
            
            // 4. Get the expiring batches and put them in the table
            nearExpiryBatches.setItems(FXCollections.observableArrayList(inv.checkExpiryDates()));
        } else {
            System.err.println("Access Denied: Current user is not an Inventory Manager.");
        }
    }
}