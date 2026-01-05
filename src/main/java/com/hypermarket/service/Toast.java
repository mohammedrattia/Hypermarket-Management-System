package com.hypermarket.service;

import java.util.Optional;

import org.controlsfx.control.Notifications;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

public class Toast {

    private static String blankIconPath = "/com/hypermarket/images/blank-image.png";
    private static String infoMessageIconPath = "/com/hypermarket/images/info-message-icon.png";
    private static String warningMessageIconPath = "/com/hypermarket/images/warning-message-icon.png";
    private static String errorMessageIconPath = "/com/hypermarket/images/error-message-icon.png";
    private static String confirmMessageIconPath = "/com/hypermarket/images/confirm-message-icon.png";

    public static Optional<ButtonType> showToast(String message, NotificationType type) {
        // TODO
        return null;
    }

    public static void showNotification(String message) {
        ImageView messageIcon = new ImageView(new Image(Toast.class.getResource(infoMessageIconPath).toExternalForm()));
        StackPane messageIconContainer = new StackPane(messageIcon);
        messageIconContainer.setPadding(new Insets(0, 10, 0, 5));
        Notifications notification = Notifications.create()
                .text(message)
                .position(Pos.BOTTOM_RIGHT)
                .hideAfter(Duration.seconds(3))
                .graphic(messageIconContainer);
        notification.show();
    }

    public static Optional<ButtonType> showAlert(String message, AlertType type) {
        Alert notification = new Alert(type);
        notification.setHeaderText(null);
        notification.setContentText(message);
        notification.setTitle(null);
        notification.setGraphic(null);
        ImageView messageIcon;
        switch (type) {
            case ERROR:
                messageIcon = new ImageView(
                        new Image(Toast.class.getResource(errorMessageIconPath).toExternalForm()));
                break;
            case WARNING:
                messageIcon = new ImageView(
                        new Image(Toast.class.getResource(warningMessageIconPath).toExternalForm()));
                break;
            case CONFIRMATION:
                messageIcon = new ImageView(
                        new Image(Toast.class.getResource(confirmMessageIconPath).toExternalForm()));
                notification.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                // notification.setContentText(null);
                break;
            default:
                messageIcon = null;
                break;
        }
        Window alertScene = notification.getDialogPane().getScene().getWindow();
        // alertScene.setOnCloseRequest(event -> {
        // event.consume(); // Prevents closing via the X button
        // });

        Stage alertStage = (Stage) alertScene;
        alertStage.initStyle(StageStyle.UNIFIED);
        alertStage.getIcons().addAll(new Image(Toast.class.getResource(blankIconPath).toExternalForm()));
        notification.setGraphic(messageIcon);
        return notification.showAndWait();
    }

    public enum NotificationType {
        INFORMATION,
        ERROR,
        CONFIRMATION,
        WARNING
    }
}
