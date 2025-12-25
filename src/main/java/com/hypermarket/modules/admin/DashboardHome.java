package com.hypermarket.modules.admin;

import java.io.IOException;
import java.util.List;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.Role;
import com.hypermarket.entities.User;
import com.hypermarket.modules.components.EmployeeCardController;
import com.hypermarket.modules.components.KpiCardController;
import com.hypermarket.modules.components.PieChartController;
import com.hypermarket.service.Session;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DashboardHome {
    private VBox employeeListContainer;
    private HBox kpiContainer;
    private PieChart pieChartNode;

    public Parent getView() {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(25));

        kpiContainer = new HBox(30);

        employeeListContainer = new VBox(15);
        employeeListContainer.setPadding(new Insets(10));

        refreshView();

        HBox bottomContainer = new HBox(5);
        VBox.setVgrow(bottomContainer, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(employeeListContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefWidth(370);
        scrollPane.setStyle("-fx-background-color: transparent;");

        HBox.setHgrow(pieChartNode, Priority.ALWAYS);

        bottomContainer.getChildren().addAll(pieChartNode, scrollPane);
        mainLayout.getChildren().addAll(kpiContainer, bottomContainer);

        return mainLayout;
    }

    private void refreshView() {
        refreshKpis();
        refreshList();
        refreshPie();
    }

    private void refreshPie() {
        pieChartNode = loadPieChartComponent();
    }

    private void refreshKpis() {
        kpiContainer.getChildren().clear();

        DataStore db = DataStore.getDataStore();
        List<User> users = db.getUsers();

        long salesCount = users.stream().filter(user -> user.getRole() == Role.SALES).count();
        long inventoryCount = users.stream().filter(user -> user.getRole() == Role.INVENTORY).count();
        long marketingCount = users.stream().filter(user -> user.getRole() == Role.MARKETING).count();

        kpiContainer.getChildren().addAll(
                loadKpiCard("Sales Agents", String.valueOf(salesCount), "Active Staff", true),
                loadKpiCard("Inventory Managers", String.valueOf(inventoryCount), "Warehouse Team", true),
                loadKpiCard("Marketing Team", String.valueOf(marketingCount), "Campaign Team", true));
    }

    private void refreshList() {
        employeeListContainer.getChildren().clear();

        DataStore db = DataStore.getDataStore();
        ObservableList<User> users = db.getUsers();
        for (User user : users) {
            if (user.getID() == Session.getInstance().getUser().getID())
                continue;

            employeeListContainer.getChildren().add(loadEmployeeCard(user, this::refreshView));
        }
    }

    private Parent loadKpiCard(String title, String value, String trend, boolean isPositive) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/KpiCard.fxml"));
            Parent node = loader.load();

            KpiCardController controller = loader.getController();
            controller.setData(title, value, trend, isPositive);
            HBox.setHgrow(node, Priority.ALWAYS);
            ((VBox) node).setMaxWidth(Double.MAX_VALUE);

            return node;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private PieChart loadPieChartComponent() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/PieChart.fxml"));
            PieChart root = loader.load();
            PieChartController pieController = loader.getController();
            pieController.setData(DataStore.getDataStore().getUsers(), "role");
            return root;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Parent loadEmployeeCard(User user, Runnable onDelete) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/EmployeeCard.fxml"));
            Parent node = loader.load();

            EmployeeCardController controller = loader.getController();
            controller.setData(user);
            controller.setOnDeleteAction(onDelete);

            return node;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
