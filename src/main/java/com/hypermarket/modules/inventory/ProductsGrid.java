package com.hypermarket.modules.inventory;

import java.io.IOException;
import java.lang.reflect.Field;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.Product;
import com.hypermarket.modules.components.ProductCardController;
import com.hypermarket.service.ListManipulation;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class ProductsGrid {

    private ScrollPane scrollPane;
    private VBox allScene;
    private TilePane grid;

    private HBox toolBar;
    private TextField searchField;
    private ChoiceBox<String> filterButton;
    private ChoiceBox<String> filterChoice;
    private ChoiceBox<String> sortButton;

    private ObservableList<Product> productsData = DataStore.getDataStore().getProducts();
    private FilteredList<Product> filteredData;
    private SortedList<Product> sortedData;

    public Parent getView() {

        setProductList();

        grid = new TilePane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(30, 10, 30, 10));
        grid.setAlignment(Pos.TOP_CENTER);

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
                    Product.class);
        });

        searchField = (TextField) toolBar.getChildren().get(5);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            ListManipulation.updateFilter(filteredData, newValue, "name", Product.class);
        });

        sortButton = (ChoiceBox<String>) toolBar.getChildren().get(8);
        sortButton.valueProperty().addListener((observable, oldValue, newValue) -> {
            ListManipulation.updateSort(sortedData, true, sortButton.getValue(), Product.class);
        });

        addSortAndFilterOptions();
    }

    private void setProductList() {
        filteredData = new FilteredList<>(productsData, p -> true);
        sortedData = new SortedList<>(filteredData);
        sortedData.addListener((ListChangeListener<Product>) change -> {
            refreshGrid();
        });
    }

    private void refreshGrid() {
        grid.getChildren().clear();

        for (Product product : sortedData) {
            Parent card = loadProductCard(product);
            if (card != null) {
                grid.getChildren().add(card);
            }
        }
    }

    private Parent loadProductCard(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hypermarket/view/components/ProductCard.fxml"));
            Parent node = loader.load();

            if (node instanceof Region) {
                ((Region) node).setMaxWidth(Double.MAX_VALUE);
            }
            GridPane.setHgrow(node, Priority.ALWAYS);

            ProductCardController controller = loader.getController();
            controller.setData(product);

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
            field = Product.class.getDeclaredField(filterButton.getValue());
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }

        for (Product obj : productsData) {
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

    private void addSortAndFilterOptions() {
        Field[] fields = Product.class.getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();
            sortButton.getItems().add(fieldName);
            filterButton.getItems().add(fieldName);
        }
    }
}