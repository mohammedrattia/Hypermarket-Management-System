module com.hypermarket {
    requires transitive javafx.fxml;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;

    opens com.hypermarket to javafx.fxml;

    exports com.hypermarket;

    // making a testing package
    // Open to EVERYONE (Reflection/FXML)
    // opens com.hypermarket.ragab to javafx.fxml;

    // Export to EVERYONE (Public access)
    // exports com.hypermarket.ragab;

        // making a testing package
    // Open to EVERYONE (Reflection/FXML)
    opens com.hypermarket.Test_Mada to javafx.fxml;

    // Export to EVERYONE (Public access)
    exports com.hypermarket.Test_Mada;
}
