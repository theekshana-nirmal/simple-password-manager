package com.example.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize any components if needed
    }

    @FXML
    private void handleCloseButton() {
        Platform.exit();
    }

    @FXML
    private void handleSaveButton() {
        // TODO: Implement save functionality
        String website = websiteField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (website.isEmpty() || username.isEmpty() || password.isEmpty()) {
            // TODO: Show validation error
            return;
        }

        // TODO: Save password entry
        // For now, just close the dialog
        Platform.exit();
    }

    @FXML
    private void handleCancelButton() {
        Platform.exit();
    }
}
