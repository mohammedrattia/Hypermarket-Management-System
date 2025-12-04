module com.hypermarket {
    requires transitive javafx.fxml;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;

    opens com.hypermarket.app to javafx.fxml;

    exports com.hypermarket.app;

    // making a testing package
    // Open to EVERYONE (Reflection/FXML)
    opens com.hypermarket.Test_Mada to javafx.fxml;

    // Export to EVERYONE (Public access)
    exports com.hypermarket.Test_Mada;
}
