module com.demo2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.programming3.assignment to javafx.fxml;
    exports com.programming3.assignment;
}