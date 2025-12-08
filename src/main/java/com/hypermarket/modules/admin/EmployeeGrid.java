package com.hypermarket.modules.admin;

import java.io.IOException;
import java.util.List;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.User;
import com.hypermarket.modules.components.EmployeeCardController;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class EmployeeGrid {
    public Parent getView() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(30));

        ColumnConstraints colConst = new ColumnConstraints();
        colConst.setPercentWidth(33.33);
        grid.getColumnConstraints().addAll(colConst, colConst, colConst);

        List<User> allUsers = DataStore.getDataStore().getUsers();

        int column = 0;
        int row = 0;

        for (User user : allUsers) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/hypermarket/view/components/EmployeeCard.fxml"));
                Parent card = loader.load();

                EmployeeCardController cardController = loader.getController();
                cardController.setData(
                        user.getFullName(),
                        user.getRole().toString(),
                        user.getSalary(),
                        user.getPhone(),
                        user.getEmail());

                grid.add(card, column++, row);

                if (column == 3) {
                    column = 0;
                    row++;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        scrollPane.setContent(grid);
        return scrollPane;
    }
}
