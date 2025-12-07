package com.hypermarket.modules.components;

import com.hypermarket.service.*;

import java.net.URL;
import java.util.*;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.lang.reflect.Field;

public class TableViewController<T> implements Initializable {

	private Class<T> type;
	private ObservableList<T> tableRows;
	private FilteredList<T> filteredData;
	private String filterOnProperty;

	@FXML
	private TableView<T> tableView;

	@FXML
	private MenuButton columnsCheckMenu;

	@FXML
	private CheckMenuItem allItems;

	@FXML
	private ChoiceBox<String> filterButton;

	@FXML
	private ChoiceBox<String> filterChoice;

	@FXML
	private TextField searchField;

	public TableViewController(Class<T> type, ObservableList<T> teamMembers, String filterOnProperty) {
		this.type = type;
		this.tableRows = teamMembers;
		this.filterOnProperty = filterOnProperty;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		filteredData = new FilteredList<>(tableRows, p -> true);
		SortedList<T> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedData);

		initSelectAllMenuItem();
		initTableView();
		initListeners();

	}

	private void initListeners() {
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			ListManipulation.updateFilter(filteredData, newValue, filterOnProperty, type);
		});
		filterChoice.valueProperty().addListener((observable, oldValue, newValue) -> {
			ListManipulation.updateFilter(filteredData, newValue, filterButton.getValue(),
					type);
		});
		filterButton.valueProperty().addListener((observable, oldValue, newValue) -> {
			updateFilterChoices(filterButton.getValue());
		});
	}

	private void initTableView() {

		Field[] fields = type.getDeclaredFields();

		for (Field field : fields) {
			String fieldName = field.getName();
			TableColumn<T, Object> col = addColumn(fieldName);

			addColumnsMenuItem(col, fieldName);

			filterButton.getItems().add(fieldName);
		}
	}

	private void addColumnsMenuItem(TableColumn<T, Object> col, String fieldName) {
		CheckMenuItem item = new CheckMenuItem(fieldName);
		item.setSelected(true);

		columnsCheckMenu.getItems().add(item);
		item.setOnAction(e -> {
			col.setVisible(item.isSelected());
			if (!item.isSelected())
				allItems.setSelected(false);
		});
	}

	private TableColumn<T, Object> addColumn(String fieldName) {
		TableColumn<T, Object> col = new TableColumn<>(fieldName);
		col.setCellValueFactory(new PropertyValueFactory<>(fieldName));
		tableView.getColumns().add(col);
		return col;
	}

	private void initSelectAllMenuItem() {
		allItems.setSelected(true);
		allItems.setOnAction(e -> {
			for (TableColumn<T, ?> col : tableView.getColumns()) {
				col.setVisible(allItems.isSelected());
			}
			for (MenuItem colItem : columnsCheckMenu.getItems()) {
				((CheckMenuItem) colItem).setSelected(allItems.isSelected());
			}
		});
	}

	private void updateFilterChoices(String property) {

		filterChoice.getItems().clear();
		Field field;
		try {
			field = type.getDeclaredField(filterButton.getValue());
			field.setAccessible(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return;
		}

		for (T obj : tableRows) {
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
}
