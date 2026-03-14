module com.example.assignmentone {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.assignmentone to javafx.fxml;
    exports com.example.assignmentone;
}