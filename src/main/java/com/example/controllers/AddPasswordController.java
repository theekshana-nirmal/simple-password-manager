package com.example.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for adding new password entries.
 * OOP Concept: This class demonstrates the CONTROLLER pattern in MVC
 * architecture
 * and manages user input validation and processing.
 */

public class AddPasswordController implements Initializable {

    @FXML
    private ImageView closeButton;

    @FXML
    private TextField websiteField;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private UserController parentController;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize any components if needed
    }

    public void setParentController(UserController parentController) {
        this.parentController = parentController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleCloseButton() {
        closeWindow();
    }

    @FXML
    private void handleSaveButton() {
        String website = websiteField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (website.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        if (password.length() < 3) {
            showAlert("Error", "Password must be at least 3 characters long!");
            return;
        }

        // Save password entry through parent controller
        if (parentController != null) {
            parentController.addPasswordEntry(website, username, password);
        }

        showAlert("Success", "Password saved successfully!");
        closeWindow();
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert.AlertType alertType = title.equals("Error")
                ? javafx.scene.control.Alert.AlertType.ERROR
                : javafx.scene.control.Alert.AlertType.INFORMATION;
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleCancelButton() {
        closeWindow();
    }

    private void closeWindow() {
        if (stage != null) {
            stage.close();
        } else {
            Platform.exit();
        }
    }
}
