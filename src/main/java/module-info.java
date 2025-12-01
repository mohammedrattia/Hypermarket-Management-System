module com.hypermarket {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.hypermarket to javafx.fxml;

    exports com.hypermarket;

    // making a testing package
    // Open to EVERYONE (Reflection/FXML)
    opens com.hypermarket.ragab;

    // Export to EVERYONE (Public access)
    exports com.hypermarket.ragab;
}
