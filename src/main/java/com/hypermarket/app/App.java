package com.hypermarket.app;

import com.hypermarket.modules.components.*;
import com.hypermarket.entities.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.hypermarket.data.*;
import com.hypermarket.entities.*;
import com.hypermarket.modules.login.LoginController;
import com.hypermarket.service.Session;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        DataStore.getDataStore().loadAllData();

        FXMLLoader loginLoader = loadLoginScene(stage);

        LoginController controller = loginLoader.getController();
        controller.setOnLoginSuccess(() -> {
            User currentUser = Session.getInstance().getUser();
            try {
                switch (currentUser.getRole()) {
                    case Role.ADMIN:
                        loadAdminScene(stage);
                        break;
                    case Role.SALES:
                        loadSalesScene(stage);
                        break;
                    case Role.INVENTORY:
                        loadInventoryScene(stage);
                        break;
                    case Role.MARKETING:
                        loadMarketingScene(stage);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private static FXMLLoader loadLoginScene(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                App.class.getResource("/com/hypermarket/view/login/Login.fxml"));
        scene = new Scene(fxmlLoader.load());

        stage.setScene(scene);
        stage.show();
        return fxmlLoader;
    }

    private static void loadAdminScene(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                App.class.getResource("/com/hypermarket/view/admin/AdminView.fxml"));

        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    private static void loadSalesScene(Stage stage) throws IOException {

    }

    private static void loadInventoryScene(Stage stage) throws IOException {

    }

    private static void loadMarketingScene(Stage stage) throws IOException {

    }

    public static void main(String[] args) {
        launch();
    }

}
