module org.compi.csc311group3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.jfr;
    requires java.sql;


    opens org.compi.csc311group3 to javafx.fxml;
    exports org.compi.csc311group3;
    exports org.compi.csc311group3.view.controllers;
    opens org.compi.csc311group3.view.controllers to javafx.fxml;
}