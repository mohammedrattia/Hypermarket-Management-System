module com.hypermarket {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.hypermarket to javafx.fxml;
    exports com.hypermarket;
}
