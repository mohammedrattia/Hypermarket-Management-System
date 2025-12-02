package com.hypermarket;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DashboardController implements Initializable{
    @FXML
    private GridPane employeeContainer;

    private List<Employee> employees;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        employees = new ArrayList<>(employees());    
        int column = 0;
        int row = 1;
        
        try{
            for(Employee employee : employees){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("EmployeeCard.fxml"));
                VBox cardBox = fxmlLoader.load();
                
                EmployeeCardController cardController = fxmlLoader.getController();
                
                cardController.setData(employee);
                
                if(column == 3){
                    column = 0;
                    ++row;
                }
                
                employeeContainer.add(cardBox, column++, row);
                
                GridPane.setMargin(cardBox, new Insets(16));
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
        
        private List<Employee> employees(){
        List<Employee> ls = new ArrayList<>();
        ls.add(new Employee("Hana AbdelHamid", "Fullstack Engineer", "90000", "+20 0102"));
        ls.add(new Employee("Hana AbdelHamid", "Fullstack Engineer", "90000", "+20 0102"));
        ls.add(new Employee("Hana AbdelHamid", "Fullstack Engineer", "90000", "+20 0102"));
        return ls;
    }
}