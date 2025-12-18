module com.hypermarket {
    requires transitive javafx.fxml;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires javafx.base;
    // Receipt Printer
    requires java.desktop;
    requires kernel;
    requires layout;
    requires io;
    requires html2pdf;
    requires org.apache.pdfbox;
    requires org.apache.fontbox;
    requires javafx.swing;

    opens com.hypermarket.modules.components to javafx.fxml;
    opens com.hypermarket.modules.sales to javafx.fxml;
    opens com.hypermarket.modules.admin to javafx.fxml;
    opens com.hypermarket.modules.user to javafx.fxml;
    opens com.hypermarket.entities to javafx.fxml, javafx.base;
    opens com.hypermarket.data to javafx.fxml;
    opens com.hypermarket.service to javafx.fxml;
    opens com.hypermarket.app to javafx.fxml;
    opens com.hypermarket.modules.inventory to javafx.fxml;

    exports com.hypermarket.app;

    // making a testing package
    // Open to EVERYONE (Reflection/FXML)

    // Export to EVERYONE (Public access)
}
