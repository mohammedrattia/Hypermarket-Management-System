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
        batchIDColumn.setCellValueFactory(new PropertyValueFactory<>("batchID"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        expiryDateColumn.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        User currentUser = Session.getInstance().getUser();

        if (currentUser instanceof Inventory) {
            Inventory inv = (Inventory) currentUser;

            nearExpiryBatches.setItems(FXCollections.observableArrayList(inv.checkExpiryDates()));
        } else {
            System.err.println("Access Denied: Current user is not an Inventory Manager.");
        }
    }
}