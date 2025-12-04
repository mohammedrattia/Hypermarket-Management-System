package com.hypermarket.modules.components;

import com.hypermarket.service.*;

import java.net.URL;
import java.util.*;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
	private ChoiceBox<String> filterButton;

	@FXML
	private TextField filterValueField;

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
		initializeTableView();
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			ListManipulation.addFilter(filteredData, newValue, filterOnProperty);
		});
		filterValueField.textProperty().addListener((observable, oldValue, newValue) -> {
			ListManipulation.addFilter(filteredData, newValue, filterButton.getValue());
		});
	}

	private void initializeTableView() {
		tableView.setItems(filteredData);

		Field[] fields = type.getDeclaredFields();

		// handling select all button
		CheckMenuItem allItem = new CheckMenuItem("Select All");
		allItem.setSelected(true);
		columnsCheckMenu.getItems().add(allItem);

		for (Field field : fields) {
			String fieldName = field.getName();

			// generating columns
			TableColumn<T, String> col = new TableColumn<>(fieldName);
			col.setCellValueFactory(new PropertyValueFactory<>(fieldName));
			tableView.getColumns().add(col);

			// generate menu item for each column
			CheckMenuItem item = new CheckMenuItem(fieldName);
			item.setSelected(true);

			columnsCheckMenu.getItems().add(item);
			item.setOnAction(e -> {
				col.setVisible(item.isSelected());
				if (!item.isSelected())
					allItem.setSelected(false);
			});

			filterButton.getItems().add(fieldName);
		}

		// handling select all button action
		allItem.setOnAction(e -> {
			for (TableColumn<T, ?> col : tableView.getColumns()) {
				col.setVisible(allItem.isSelected());
			}
			for (MenuItem colItem : columnsCheckMenu.getItems()) {
				((CheckMenuItem) colItem).setSelected(allItem.isSelected());
			}
		});
	}
}
