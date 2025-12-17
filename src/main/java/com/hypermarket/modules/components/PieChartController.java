package com.hypermarket.modules.components;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import java.net.URL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import java.lang.reflect.Field;

public class PieChartController implements Initializable {

	@FXML
	private PieChart pieChart;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("SilkSong", 40),
				new PieChart.Data("EX33CO", 70),
				new PieChart.Data("NPC", 15),
				new PieChart.Data("NPC", 10));
		pieChart.setData(pieChartData);
	}

	public void setData(ObservableList<?> dataList, String fieldName) {

		Map<String, Integer> counts = new HashMap<>();

		for (Object item : dataList) {
			try {
				Field field = getFieldAcrossHierarchy(item.getClass(), fieldName);
				field.setAccessible(true);

				Object value = field.get(item);

				String label = (value == null) ? "Unknown" : value.toString();

				counts.put(label, counts.getOrDefault(label, 0) + 1);

			} catch (IllegalAccessException e) {
				System.err.println("Error accessing field '" + fieldName + "': " + e.getMessage());
			}
		}

		ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();

		for (Map.Entry<String, Integer> entry : counts.entrySet()) {
			chartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
		}

		pieChart.setData(chartData);
	}

	private Field getFieldAcrossHierarchy(Class<?> currentClass, String fieldName) {
		while (currentClass != null && currentClass != Object.class) {
			try {
				return currentClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				currentClass = currentClass.getSuperclass();
			}
		}
		return null;

	}
}
