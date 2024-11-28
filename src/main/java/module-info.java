module com.safe.storage {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;
    requires java.desktop;
    requires java.datatransfer;

    opens com.safe.storage to javafx.fxml;

    opens com.safe.storage.models to javafx.base; // Open the models package to javafx.base
    opens com.safe.storage.views to javafx.fxml; // If you have views that need to be opened
    opens com.safe.storage.controllers to javafx.fxml; // If you have controllers that need to be opened

    exports com.safe.storage;
}
