package com.hypermarket;

import java.net.URL;
import java.util.*;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.lang.reflect.Field;

public class TableViewController<viewClass> implements Initializable {

	private Class<viewClass> type;
	private ObservableList<viewClass> teamMembers;

	@FXML
	private TableView<viewClass> tableView;

	@FXML
	private ComboBox<CheckBox> comboBox;

	public TableViewController(Class<viewClass> type, ObservableList<viewClass> teamMembers) {
		this.type = type;
		this.teamMembers = teamMembers;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		tableView.setItems(teamMembers);
		createColumns(teamMembers);
	}

	public void createColumns(ObservableList<viewClass> teamMembers) {
		Field[] fields = type.getDeclaredFields();

		for (Field field : fields) {
			TableColumn<viewClass, String> col = new TableColumn<>(field.getName());
			col.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
			tableView.getColumns().add(col);
		}
	}
}
