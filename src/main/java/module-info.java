module com.example.eventmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.eventmanager to javafx.fxml;
    opens com.example.eventmanager.model to javafx.base;

    exports com.example.eventmanager;
    exports com.example.eventmanager.controller;
    opens com.example.eventmanager.controller to javafx.fxml;
}
