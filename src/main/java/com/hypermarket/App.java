package com.hypermarket;

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
    public static List<Person> members = List.of(
            new Person(1, "Reed"),
            new Person(2, "Michaelson"),
            new Person(2, "Dean"));

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("TableView"));
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        TableViewController<Person> controller = new TableViewController<Person>(Person.class,
                FXCollections.observableArrayList(members));
        fxmlLoader.setController(controller);

        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
