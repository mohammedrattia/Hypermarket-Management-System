package com.hypermarket;

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
	// private ObservableList<MenuItem> menuItems;

	@FXML
	private TableView<T> tableView;

	@FXML
	private MenuButton columnsCheckMenu;

	@FXML
	private ChoiceBox<String> filterButton;

	@FXML
	private TextField filterValueField;

	public TableViewController(Class<T> type, ObservableList<T> teamMembers) {
		this.type = type;
		this.tableRows = teamMembers;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeTableView();
		filterValueField.textProperty().addListener((observable, oldValue, newValue) -> {
			addFilter(newValue);
		});
	}

	private void initializeTableView() {
		tableView.setItems(tableRows);

		Field[] fields = type.getDeclaredFields();

		CheckMenuItem allItem = new CheckMenuItem("Select All");
		allItem.setSelected(true);
		columnsCheckMenu.getItems().add(allItem);

		for (Field field : fields) {
			TableColumn<T, String> col = new TableColumn<>(field.getName());
			col.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
			tableView.getColumns().add(col);

			CheckMenuItem item = new CheckMenuItem(field.getName());
			item.setSelected(true);

			columnsCheckMenu.getItems().add(item);
			item.setOnAction(e -> {
				col.setVisible(item.isSelected());
				if (!item.isSelected())
					allItem.setSelected(false);
			});

			filterButton.getItems().add(field.getName());
		}

		allItem.setOnAction(e -> {
			for (TableColumn<T, ?> col : tableView.getColumns()) {
				col.setVisible(allItem.isSelected());
			}
			for (MenuItem colItem : columnsCheckMenu.getItems()) {
				((CheckMenuItem) colItem).setSelected(allItem.isSelected());
			}
		});
	}

	public void addFilter() {

	}

}
