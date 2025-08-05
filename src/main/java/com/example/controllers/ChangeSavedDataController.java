package com.example.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangeSavedDataController implements Initializable {

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

    private Stage stage;
    private UserController.PasswordEntry passwordEntry;
    private Runnable onSaveCallback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize any components if needed
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPasswordEntry(UserController.PasswordEntry entry) {
        this.passwordEntry = entry;
        // Pre-fill the form with existing data
        if (entry != null) {
            websiteField.setText(entry.getWebsite());
            usernameField.setText(entry.getUsername());
            passwordField.setText(entry.getPassword());
        }
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    private void handleCloseButton() {
        closeWindow();
    }

    @FXML
    private void handleSaveButton() {
        String website = websiteField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validation
        if (website.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        if (password.length() < 3) {
            showAlert("Error", "Password must be at least 3 characters long!");
            return;
        }

        // Update the password entry
        if (passwordEntry != null) {
            passwordEntry.setWebsite(website);
            passwordEntry.setUsername(username);
            passwordEntry.setPassword(password);
        }

        // Call the callback to notify the parent controller
        if (onSaveCallback != null) {
            onSaveCallback.run();
        }

        showAlert("Success", "Password entry updated successfully!");
        closeWindow();
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

    private void showAlert(String title, String message) {
        Alert.AlertType alertType = title.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
