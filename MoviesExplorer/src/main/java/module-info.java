module com.example.moviesexplorer {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires com.fasterxml.jackson.databind;

    opens com.example.moviesexplorer to javafx.fxml;
    exports com.example.moviesexplorer;
}