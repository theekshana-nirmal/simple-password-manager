package com.example.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for viewing password details in a dialog.
 * OOP Concept: This class demonstrates ENCAPSULATION by hiding implementation
 * details
 * and providing a clean interface for viewing password data.
 */

public class ViewPasswordController implements Initializable {

    @FXML
    private ImageView closeButton;

    @FXML
    private TextField websiteField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize any components if needed
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPasswordEntry(UserController.PasswordEntry entry) {
        if (entry != null) {
            websiteField.setText(entry.getWebsite());
            usernameField.setText(entry.getUsername());
            passwordField.setText(entry.getDecryptedPassword()); // Use decrypted password for display
        }
    }

    @FXML
    private void handleCloseButton() {
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
