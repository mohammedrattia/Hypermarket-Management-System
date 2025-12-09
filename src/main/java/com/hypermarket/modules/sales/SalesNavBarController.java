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

public class SalesNavBarController implements Initializable {
    @FXML
    private AnchorPane contentArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDashboardHome();
    }

    private void loadDashboardHome() {
        contentArea.getChildren().clear();

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(25));

        HBox kpiContainer = new HBox(30);
        kpiContainer.getChildren().addAll(
                loadKpiCard("Total Sales", "$120,000", "15%", true),
                loadKpiCard("Active Users", "45", "2", true),
                loadKpiCard("Low Stock", "10 Items", "5%", false));

        HBox bottomContainer = new HBox(5);
        VBox.setVgrow(bottomContainer, Priority.ALWAYS);

        VBox employeeList = new VBox(15);
        employeeList.setPadding(new Insets(10));

        for (int i = 0; i < 5; i++) {
            employeeList.getChildren()
                    .add(loadEmployeeCard("Hana Mohamed", "Admin", 75000, "0100000000", "hana@gmail.com"));
        }
        ScrollPane scrollPane = new ScrollPane(employeeList);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefWidth(370);
        scrollPane.setStyle("-fx-background-color: transparent;");

        Parent pieChartNode = loadPieChartComponent();
        HBox.setHgrow(pieChartNode, Priority.ALWAYS);
        bottomContainer.getChildren().addAll(pieChartNode, scrollPane);

        mainLayout.getChildren().addAll(kpiContainer, bottomContainer);

        AnchorPane.setTopAnchor(mainLayout, 0.0);
        AnchorPane.setBottomAnchor(mainLayout, 0.0);
        AnchorPane.setLeftAnchor(mainLayout, 0.0);
        AnchorPane.setRightAnchor(mainLayout, 0.0);

        contentArea.getChildren().add(mainLayout);
    }

    private Parent loadKpiCard(String title, String value, String trend, boolean isPositive) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/KpiCard.fxml"));
            Parent node = loader.load();

            KpiCardController controller = loader.getController();
            controller.setData(title, value, trend, isPositive);

            return node;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Parent loadPieChartComponent() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/PieChart.fxml"));
            return loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Parent loadEmployeeCard(String name, String title, double salary, String phone, String email) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/EmployeeCard.fxml"));
            Parent node = loader.load();

            EmployeeCardController controller = loader.getController();
            controller.setData(name, title, salary, phone, email);

            return node;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
