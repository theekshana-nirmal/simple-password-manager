package com.example.controllers;

import java.io.IOException;

import com.example.App;
import com.example.utils.UserManager;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class LoginController {

    @FXML
    private ImageView closeButton;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInButton;

    @FXML
    private void handleCloseButton() {
        // Close the application
        Platform.exit();
    }

    @FXML
    private void handleSignIn() {
        String emailOrUsername = emailField.getText().trim();
        String password = passwordField.getText();

        // Validate input
        if (emailOrUsername.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both email/username and password.");
            return;
        }

        // Attempt authentication
        if (UserManager.authenticateUser(emailOrUsername, password)) {
            try {
                // Successful login, navigate to user dashboard
                App.setRoot("fxml/user-view");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load user dashboard.");
            }
        } else {
            showAlert("Login Failed", "Invalid email/username or password.");
        }
    }

    @FXML
    private void handleRegisterClick() {
        try {
            App.setRoot("fxml/register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdminClick() {
        try {
            App.setRoot("fxml/admin-login");
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
