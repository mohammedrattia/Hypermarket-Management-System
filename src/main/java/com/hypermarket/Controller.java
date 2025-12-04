package com.hypermarket;

import java.util.ResourceBundle;

import java.net.URL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

public class Controller implements Initializable {

	@FXML
	private PieChart pieChart;

    @Override
    public void initialize(URL url,ResourceBundle rb){
    	ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
    			new PieChart.Data("SilkSong",40),
    			new PieChart.Data("EX33CO",70),
    			new PieChart.Data("NPC",15),
    			new PieChart.Data("NPC",10));
    	pieChart.setData(pieChartData);
    }
    
}
