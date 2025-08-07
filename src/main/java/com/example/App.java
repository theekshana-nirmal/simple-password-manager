package com.example;

import com.example.utils.AdminManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage; // Store reference to the primary stage @Override

    public void start(Stage stage) throws IOException {
        // Migrate existing admin passwords to encrypted format
        AdminManager.migrateToEncryptedPasswords();
        primaryStage = stage; // Store the stage reference
        // Set the stage style to TRANSPARENT
        stage.initStyle(StageStyle.TRANSPARENT);
        Parent root = loadFXML("fxml/welcome");
        scene = new Scene(root);

        scene.setFill(javafx.scene.paint.Color.TRANSPARENT); // Load the reset stylesheet first
        scene.getStylesheets().add(getClass().getResource("/com/example/css/reset.css").toExternalForm());

        // Apply CSS stylesheet directly to the scene
        scene.getStylesheets().add(getClass().getResource("/com/example/css/styles.css").toExternalForm());

        // Make the undecorated window draggable
        makeDraggable(root);

        stage.setScene(scene);
        stage.show();
    }

    // Method to make any Parent node draggable
    private static void makeDraggable(Parent root) {
        final double[] xOffset = { 0 };
        final double[] yOffset = { 0 };

        root.setOnMousePressed(event -> {
            xOffset[0] = event.getSceneX();
            yOffset[0] = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            if (primaryStage != null) {
                primaryStage.setX(event.getScreenX() - xOffset[0]);
                primaryStage.setY(event.getScreenY() - yOffset[0]);
            }
        });
    }

    public static void setRoot(String fxml) throws IOException {
        Parent newRoot = loadFXML(fxml);
        scene.setRoot(newRoot);

        // Apply drag functionality to the new root
        makeDraggable(newRoot);

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