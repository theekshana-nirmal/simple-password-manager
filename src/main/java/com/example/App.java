package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage; // Store reference to the primary stage

    // For making the window draggable
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage; // Store the stage reference
        // Set the stage style to TRANSPARENT
        stage.initStyle(StageStyle.TRANSPARENT);
        Parent root = loadFXML("fxml/login");
        scene = new Scene(root);

        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

        // Load the reset stylesheet first
        scene.getStylesheets().add(getClass().getResource("/com/example/css/reset.css").toExternalForm());

        // Apply CSS stylesheet directly to the scene
        scene.getStylesheets().add(getClass().getResource("/com/example/css/styles.css").toExternalForm());

        // Make the undecorated window draggable
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        Parent newRoot = loadFXML(fxml);
        scene.setRoot(newRoot);

        // Resize the window to fit the new content
        if (primaryStage != null) {
            primaryStage.sizeToScene();
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}