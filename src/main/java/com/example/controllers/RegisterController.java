package com.example.controllers;

import com.example.App;
import com.example.utils.UserManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.regex.Pattern;

public class RegisterController {

    @FXML
    private ImageView closeButton;

    @FXML
    private Label loginLabel;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button createAccountButton;

    @FXML
    private void handleCloseButton() {
        // Close the application
        Platform.exit();
    }

    @FXML
    private void handleCreateAccount() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Validate input
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            showAlert("Error", "Please enter a valid email address.");
            return;
        }

        // Validate password strength
        if (password.length() < 6) {
            showAlert("Error", "Password must be at least 6 characters long.");
            return;
        }

        // Attempt registration
        if (UserManager.registerUser(name, email, password)) {
            showAlert("Success", "Account created successfully! You can now log in.");
            try {
                App.setRoot("fxml/login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Registration Failed", "Username or email already exists. Please choose different credentials.");
        }
    }

    @FXML
    private void handleLoginClick() {
        try {
            App.setRoot("fxml/login");
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

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
