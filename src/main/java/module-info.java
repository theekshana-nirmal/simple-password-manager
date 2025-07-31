module com.example {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.example to javafx.fxml;

    exports com.example;
}
