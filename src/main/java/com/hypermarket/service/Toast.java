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
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

public class Toast {

    public enum NotificationType {
        INFORMATION,
        ERROR,
        WARNING,
        CONFIRMATION
    }

    public static String blankIconPath = "/com/hypermarket/images/blank-image.png";
    private static String infoMessageIconPath = "/com/hypermarket/images/info-message-icon.png";
    private static String warningMessageIconPath = "/com/hypermarket/images/warning-message-icon.png";
    private static String errorMessageIconPath = "/com/hypermarket/images/error-message-icon.png";

    public static Optional<ButtonType> showToast(String message, NotificationType type) {
        if (type == NotificationType.INFORMATION) {
            showNotification(message);
        } else {
            AlertType alertType = AlertType.valueOf(type.toString());
            return showAlert(message, alertType);
        }
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
        String prefix = "\n";
        notification.setHeaderText(null);
        notification.setContentText(prefix + message);
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
                        new Image(Toast.class.getResource(warningMessageIconPath).toExternalForm()));
                notification.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                break;
            default:
                messageIcon = null;
                break;
        }
        Window alertScene = notification.getDialogPane().getScene().getWindow();
        Stage alertStage = (Stage) alertScene;
        alertStage.initStyle(StageStyle.UNIFIED);
        alertStage.getIcons().addAll(new Image(Toast.class.getResource(blankIconPath).toExternalForm()));
        Region messageIconSpacing = new Region();
        messageIconSpacing.setMinHeight(10);
        VBox messageIconContainer = new VBox(messageIconSpacing, messageIcon);
        notification.setGraphic(messageIconContainer);
        return notification.showAndWait();
    }
}
