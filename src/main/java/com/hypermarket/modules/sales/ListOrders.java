package com.hypermarket.modules.sales;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hypermarket.modules.components.EmployeeCardController;
import com.hypermarket.modules.components.KpiCardController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ListOrders implements Initializable {
    @FXML
    private AnchorPane contentArea;
    // @FXML
    // private AnchorPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // rootPane.getStylesheets().add(getClass().getResource("/css/global.css").toExternalForm());
        // rootPane.getStylesheets().add(getClass().getResource("/css/specific.css").toExternalForm());
        // loadDashboardHome();
    }

    private void loadDashboardHome() {
        // contentArea.getChildren().clear();

    }
}
