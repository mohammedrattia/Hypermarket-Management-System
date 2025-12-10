package com.hypermarket.modules.admin;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.User;
import com.hypermarket.modules.components.EmployeeCardController;
import com.hypermarket.service.ListManipulation;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class EmployeeGrid {

    private ScrollPane scrollPane;
    private VBox allScene;
    private GridPane grid;

    private HBox toolBar;
    private TextField searchField;
    private ChoiceBox<String> filterButton;
    private ChoiceBox<String> filterChoice;
    private ChoiceBox<String> sortButton;

    private ObservableList<User> employeesData;
    private FilteredList<User> filteredData;
    private SortedList<User> sortedData;

    public Parent getView() {
        // init the employee list
        setEmployeeList(DataStore.getDataStore().getUsers());

        // set columns constraints
        ColumnConstraints colConst = new ColumnConstraints();
        colConst.setPercentWidth(33.33);

        // make the grid of Employees
        grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(30));
        grid.getColumnConstraints().addAll(colConst, colConst, colConst);

        // put the grid into scroll pane
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setContent(grid);

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/FilterAndSearch.fxml"));
            toolBar = loader.load();
            configToolBar();
        } catch (IOException e) {
            System.err.println("Failed to load FilterAndSearch.fxml");
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.err.println("FilterAndSearch.fxml not found at specified path.");
            e.printStackTrace();
        }

        allScene = new VBox();
        if (toolBar != null) {
            allScene.getChildren().add(toolBar);
        }
        allScene.getChildren().add(scrollPane);

        refreshGrid();

        return allScene;
    }

    @SuppressWarnings("unchecked")
    private void configToolBar() {

        filterButton = (ChoiceBox<String>) toolBar.getChildren().get(1);
        filterButton.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateFilterChoices(filterButton.getValue());
        });

        filterChoice = (ChoiceBox<String>) toolBar.getChildren().get(3);
        filterChoice.valueProperty().addListener((observable, oldValue, newValue) -> {
            ListManipulation.updateFilter(filteredData, newValue, filterButton.getValue(),
                    User.class);
        });

        searchField = (TextField) toolBar.getChildren().get(5);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            ListManipulation.updateFilter(filteredData, newValue, "fullName", User.class);
        });

        // TODO: handle the ascending only sort (maybe add checkbox to change from
        // ascending to descending)
        sortButton = (ChoiceBox<String>) toolBar.getChildren().get(8);
        sortButton.valueProperty().addListener((observable, oldValue, newValue) -> {
            ListManipulation.updateSort(sortedData, true, sortButton.getValue(), User.class);
        });

        addSortAndFilterOtions();
    }

    private void setEmployeeList(ObservableList<User> users) {
        employeesData = FXCollections.observableList(users);
        filteredData = new FilteredList<>(employeesData, p -> true);
        sortedData = new SortedList<>(filteredData);
        sortedData.addListener((ListChangeListener<User>) change -> {
            refreshGrid();
        });
    }

    private void refreshGrid() {
        grid.getChildren().clear();

        int column = 0;
        int row = 0;

        for (User user : sortedData) {
            Parent card = loadEmployeeCard(user, () -> {
                employeesData.remove(user);
            });

            if (card != null) {
                grid.add(card, column++, row);

                if (column == 3) {
                    column = 0;
                    row++;
                }
            }
        }
    }

    private Parent loadEmployeeCard(User user, Runnable onDelete) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/EmployeeCard.fxml"));
            Parent node = loader.load();

            if (node instanceof Region) {
                ((Region) node).setMaxWidth(Double.MAX_VALUE);
            }
            GridPane.setHgrow(node, Priority.ALWAYS);

            EmployeeCardController controller = loader.getController();
            controller.setData(user);

            controller.setOnDeleteAction(onDelete);

            return node;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void updateFilterChoices(String property) {

        filterChoice.getItems().clear();
        Field field;
        try {
            field = User.class.getDeclaredField(filterButton.getValue());
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }

        for (User obj : employeesData) {
            Object choice;
            try {
                choice = field.get(obj);
                boolean alreadyExist = filterChoice.getItems().contains(String.valueOf(choice));
                if (!alreadyExist)
                    filterChoice.getItems().add(String.valueOf(choice));
            } catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    private void addSortAndFilterOtions() {

        Field[] fields = User.class.getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();

            sortButton.getItems().add(fieldName);
            filterButton.getItems().add(fieldName);
        }
    }
}