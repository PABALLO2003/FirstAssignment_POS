module com.example.assignmentone {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.assignmentone to javafx.fxml;
    exports com.example.assignmentone;
}