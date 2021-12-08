module com.example.myapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    exports com.example.myapp.Controllers;
    opens com.example.myapp.Controllers to javafx.fxml;

    exports com.example.myapp;
    opens com.example.myapp to javafx.fxml;
}