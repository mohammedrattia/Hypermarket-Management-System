package com.hypermarket.modules.sales;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.Order;
import com.hypermarket.entities.Sales;
import com.hypermarket.modules.components.BarChartController;
import com.hypermarket.modules.components.KpiCardController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SalesDashboard {

    private HBox kpiContainer;
    private TableView<Order> miniTable;

    private BarChartController chartController;

    private Sales currentUser;

    public SalesDashboard(Sales currentUser) {
        this.currentUser = currentUser;
    }

    public Parent getView() {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(25));
        mainLayout.setStyle("-fx-background-color: #f4f4f4;");

        kpiContainer = new HBox(30);

        HBox contentContainer = new HBox(5);
        VBox.setVgrow(contentContainer, Priority.ALWAYS);

        Parent chartNode = loadSalesChart();
        if (chartNode != null) {
            HBox.setHgrow(chartNode, Priority.ALWAYS);
            contentContainer.getChildren().add(chartNode);
        } else {
            contentContainer.getChildren().add(new Label("Error loading chart"));
        }

        VBox tableContainer = new VBox(10);
        tableContainer.setPrefWidth(350);
        tableContainer.setMinWidth(350);

        Label lblTable = new Label("Recent Orders (Today)");
        lblTable.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2a2a2a;");

        miniTable = createMiniTable();
        VBox.setVgrow(miniTable, Priority.ALWAYS);

        tableContainer.getChildren().addAll(lblTable, miniTable);

        contentContainer.getChildren().add(tableContainer);

        refreshView();
        mainLayout.getChildren().addAll(kpiContainer, contentContainer);

        return mainLayout;
    }

    private void refreshView() {
        ObservableList<Order> allOrders = DataStore.getDataStore().getOrders();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        // ObservableList<Order> myOrders = allOrders;

        ObservableList<Order> myOrders = FXCollections.observableArrayList();
        for (Order o : allOrders)
            if (o.getSeller() != null && o.getSeller().getID() == currentUser.getID())
                myOrders.add(o);

        ObservableList<Order> myOrdersToday = FXCollections.observableArrayList();
        ObservableList<Order> myOrdersYesterday = FXCollections.observableArrayList();

        for (Order o : myOrders) {
            LocalDate oDate = o.getDateTime().toLocalDate();
            if (oDate.equals(today)) {
                myOrdersToday.add(o);
            } else if (oDate.equals(yesterday)) {
                myOrdersYesterday.add(o);
            }
        }

        refreshKpis(myOrdersToday, myOrdersYesterday);
        refreshBarChart(myOrders);
        refreshMiniTable(myOrdersToday);
    }

    private void refreshBarChart(List<Order> myOrders) {
        if (chartController != null) {
            chartController.setData(myOrders);
        }
    }

    private Parent loadSalesChart() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/BarChart.fxml"));
            Parent node = loader.load();

            chartController = loader.getController();

            return node;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void refreshKpis(List<Order> todayOrders, List<Order> yesterdayOrders) {
        kpiContainer.getChildren().clear();

        double revToday = 0;
        for (Order o : todayOrders)
            revToday += o.getTotalPrice();

        int countToday = todayOrders.size();
        double avgToday = (countToday == 0) ? 0 : revToday / countToday;

        double revYesterday = 0;
        for (Order o : yesterdayOrders)
            revYesterday += o.getTotalPrice();

        int countYesterday = yesterdayOrders.size();
        double avgYesterday = (countYesterday == 0) ? 0 : revYesterday / countYesterday;

        kpiContainer.getChildren().addAll(
                createKpiWithTrend("Today's Revenue", revToday, revYesterday, true),
                createKpiWithTrend("Today's Orders", countToday, countYesterday, false),
                createKpiWithTrend("Avg Order Value", avgToday, avgYesterday, true));
    }

    private Parent createKpiWithTrend(String title, double current, double previous, boolean isCurrency) {
        String valueStr;
        if (isCurrency) {
            valueStr = String.format("$%.2f", current);
        } else {
            valueStr = String.valueOf((int) current);
        }

        double percentage = 0;
        if (previous > 0) {
            percentage = ((current - previous) / previous) * 100;
        } else if (current > 0) {
        }

        boolean isPositive = percentage >= 0;

        String trendStr = String.format("%+.1f%% vs yesterday", percentage);

        return loadKpiCard(title, valueStr, trendStr, isPositive);
    }

    private TableView<Order> createMiniTable() {
        TableView<Order> table = new TableView<>();

        configureTableFeatures(table);

        TableColumn<Order, Integer> colId = createIdColumn();
        TableColumn<Order, Order> colTime = createTimeColumn();
        TableColumn<Order, Integer> colQty = createQuantityColumn();
        TableColumn<Order, Double> colTotal = createTotalColumn();

        table.getColumns().add(colId);
        table.getColumns().add(colTime);
        table.getColumns().add(colQty);
        table.getColumns().add(colTotal);

        return table;
    }

    private void configureTableFeatures(TableView<Order> table) {
        table.getStylesheets().add(getClass().getResource("/com/hypermarket/css/TableView.css").toExternalForm());
        table.getStyleClass().add("table-view");

        table.setStyle(
                "-fx-border-width: 0px; -fx-background-insets: 0; -fx-padding: 0; -fx-background-color: transparent;");

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        table.setSelectionModel(null);
    }

    private TableColumn<Order, Integer> createIdColumn() {
        TableColumn<Order, Integer> col = new TableColumn<>("ID");
        col.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        return col;
    }

    private TableColumn<Order, Order> createTimeColumn() {
        TableColumn<Order, Order> col = new TableColumn<>("Time");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        col.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue()));

        col.setCellFactory(column -> new TableCell<Order, Order>() {
            @Override
            protected void updateItem(Order item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDateTime().format(timeFmt));
                }
            }
        });
        return col;
    }

    private TableColumn<Order, Integer> createQuantityColumn() {
        TableColumn<Order, Integer> col = new TableColumn<>("Qty");
        col.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        return col;
    }

    private TableColumn<Order, Double> createTotalColumn() {
        TableColumn<Order, Double> col = new TableColumn<>("Total");
        col.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        col.setCellFactory(column -> new TableCell<Order, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
        return col;
    }

    private void refreshMiniTable(List<Order> todayOrders) {
        List<Order> sortedList = new ArrayList<>(todayOrders);
        Collections.sort(sortedList, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return o2.getDateTime().compareTo(o1.getDateTime());
            }
        });

        List<Order> top5 = new ArrayList<>();
        int limit = Math.min(sortedList.size(), 5);
        for (int i = 0; i < limit; i++) {
            top5.add(sortedList.get(i));
        }

        miniTable.setItems(FXCollections.observableArrayList(top5));
    }

    private Parent loadKpiCard(String title, String value, String trend, boolean isPositive) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hypermarket/view/components/KpiCard.fxml"));
            Parent node = loader.load();

            KpiCardController controller = loader.getController();
            controller.setData(title, value, trend, isPositive);

            HBox.setHgrow(node, Priority.ALWAYS);
            ((VBox) node).setMaxWidth(Double.MAX_VALUE);
            return node;
        } catch (IOException ex) {
            ex.printStackTrace();
            return new Label("Error loading KPI");
        }
    }
}