package com.hypermarket.modules.admin;

import java.io.IOException;
import java.util.List;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.User;
import com.hypermarket.modules.components.EmployeeCardController;
import com.hypermarket.modules.components.KpiCardController;
import com.hypermarket.modules.components.PieChartController;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

        int userCount = users.size();

        kpiContainer.getChildren().addAll(
                loadKpiCard("Active Users", String.valueOf(userCount), "2", true),
                loadKpiCard("Total Sales", "120,000", "2", true),
                loadKpiCard("Low Stock", "10 Items", "5%", false));
    }

    private void refreshList() {
        employeeListContainer.getChildren().clear();

        DataStore db = DataStore.getDataStore();
        List<User> users = db.getUsers();
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            employeeListContainer.getChildren().add(loadEmployeeCard(u, this::refreshView));
        }
    }

    private Parent loadKpiCard(String title, String value, String trend, boolean isPositive) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/KpiCard.fxml"));
            Parent node = loader.load();

            KpiCardController controller = loader.getController();
            controller.setData(title, value, trend, isPositive);
            ((VBox) node).setAlignment(Pos.TOP_CENTER);
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
