package com.hypermarket.app;

// import com.hypermarket.modules.components.*;
// import com.hypermarket.entities.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import com.hypermarket.data.*;
import com.hypermarket.entities.*;
import com.hypermarket.modules.components.*;

/**
 * JavaFX App
 */
public class App extends Application {

        private static Scene scene;

        @Override
        public void start(Stage stage) throws IOException {
                DataStore.getDataStore().loadAllData();
                scene = new Scene(loadFXML("/com/hypermarket/view/components/TableView"));
                stage.setScene(scene);
                stage.show();
        }

        static void setRoot(String fxml) throws IOException {
                scene.setRoot(loadFXML(fxml));
        }

        private static Parent loadFXML(String fxml) throws IOException {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
                ArrayList<User> members = DataStore.getDataStore().getUsers();
                TableViewController<User> controller = new TableViewController<User>(User.class,
                                FXCollections.observableArrayList(members), "fullName");
                fxmlLoader.setController(controller);

                return fxmlLoader.load();
        }

        public static void main(String[] args) {
                launch();
        }
}
