module org.compi.csc311group3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.jfr;


    opens org.compi.csc311group3 to javafx.fxml;
    exports org.compi.csc311group3;
}