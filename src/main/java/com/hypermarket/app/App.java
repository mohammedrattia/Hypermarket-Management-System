package com.hypermarket.app;

import com.hypermarket.modules.components.*;
import com.hypermarket.entities.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static List<User> members = List.of(
            new User("1", "Reed", 34),
            new User("2", "Michaelson", 54),
            new User("2", "Dean", 45));

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/com/hypermarket/view/components/TableView"));
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        TableViewController<User> controller = new TableViewController<User>(User.class,
                FXCollections.observableArrayList(members), "name");
        fxmlLoader.setController(controller);

        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
