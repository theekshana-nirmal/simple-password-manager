package com.example.controllers;

import com.example.App;
import com.example.utils.AdminManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class AdminLoginController {

    @FXML
    private ImageView closeButton;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleCloseButton() {
        Platform.exit();
    }

    @FXML
    private void handleLoginClick() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both email and password.");
            return;
        }

        if (AdminManager.authenticateAdmin(email, password)) {
            try {
                App.setRoot("fxml/admin-view");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load admin panel.");
            }
        } else {
            showAlert("Error", "Invalid admin credentials.");
            emailField.clear();
            passwordField.clear();
        }
    }

    @FXML
    private void handleBackClick() {
        try {
            App.setRoot("fxml/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGuestClick() {
        try {
            App.setRoot("fxml/guest-view");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToWelcome() {
        try {
            App.setRoot("fxml/welcome");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
