package com.hypermarket.app;

import com.hypermarket.entities.*;
import com.hypermarket.modules.admin.AdminViewController;
import com.hypermarket.modules.user.LoginController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.ModuleLayer.Controller;

import com.hypermarket.data.*;
import com.hypermarket.service.Authenticator;
import com.hypermarket.service.Session;

/**
 * JavaFX App
 */
public class App extends Application {

        private static Scene scene;
        private FXMLLoader loginLoader;

        @Override
        public void start(Stage stage) throws IOException {
                DataStore.getDataStore().loadAllData();
                loadLoginScene(stage);
        }

        @Override
        public void stop() throws Exception {
                DataStore.getDataStore().saveAllData();
                super.stop();
        }

        private static void loadLoginScene(Stage stage) throws IOException {
                FXMLLoader fxmlLoader = new FXMLLoader(
                                App.class.getResource("/com/hypermarket/view/user/Login.fxml"));

                scene = new Scene(fxmlLoader.load());

                LoginController controller = fxmlLoader.getController();
                controller.setOnLoginSuccess(() -> {
                        User currentUser = Session.getInstance().getUser();
                        try {
                                if (currentUser != null) {
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
                                }

                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                });

                stage.setScene(scene);
                stage.show();
        }

        private static void loadAdminScene(Stage stage) throws IOException {
                FXMLLoader fxmlLoader = new FXMLLoader(
                                App.class.getResource("/com/hypermarket/view/admin/AdminView.fxml"));

                scene = new Scene(fxmlLoader.load());
                AdminViewController controller = fxmlLoader.getController();
                controller.setOnLogout(() -> {
                        Authenticator.logout();
                        try {
                                loadLoginScene(stage);
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                });

                stage.setScene(scene);
                stage.show();
        }

        private static void loadSalesScene(Stage stage) throws IOException {

        }

        private static void loadInventoryScene(Stage stage) throws IOException {
                FXMLLoader fxmlLoader = new FXMLLoader(
                                App.class.getResource("/com/hypermarket/view/admin/InventoryView.fxml"));

                scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.show();    
        }

        private static void loadMarketingScene(Stage stage) throws IOException {

        }

        public static void main(String[] args) {
                launch();
        }

}
