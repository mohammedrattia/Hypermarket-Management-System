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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class EmployeeGrid {

    private ScrollPane scrollPane;
    private GridPane grid;

    public Parent getView() {
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(30));

        ColumnConstraints colConst = new ColumnConstraints();
        colConst.setPercentWidth(33.33);
        grid.getColumnConstraints().addAll(colConst, colConst, colConst);

        scrollPane.setContent(grid);

        refreshGrid();

        return scrollPane;
    }

    private void refreshGrid() {
        grid.getChildren().clear();
        List<User> allUsers = DataStore.getDataStore().getUsers();

        int column = 0;
        int row = 0;

        for (User user : allUsers) {
            Parent card = loadEmployeeCard(user, this::refreshGrid);

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
}