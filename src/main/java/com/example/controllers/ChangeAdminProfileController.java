package com.example.controllers;

import com.example.utils.AdminManager;
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

public class ChangeAdminProfileController implements Initializable {

    @FXML
    private ImageView closeButton;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load current admin username
        loadCurrentAdminData();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void loadCurrentAdminData() {
        // Pre-fill the username field with current admin email/username
        String currentEmail = AdminManager.getAdminEmail();
        usernameField.setText(currentEmail);
    }

    @FXML
    private void handleCloseButton() {
        closeWindow();
    }

    @FXML
    private void handleSaveButton() {
        String currentPassword = currentPasswordField.getText();
        String newUsername = usernameField.getText().trim();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate all fields are filled
        if (currentPassword.isEmpty() || newUsername.isEmpty() ||
                newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        // Validate username
        if (newUsername.length() < 3) {
            showAlert("Error", "Username must be at least 3 characters long!");
            return;
        }

        // Validate new password
        if (newPassword.length() < 3) {
            showAlert("Error", "Password must be at least 3 characters long!");
            return;
        }

        // Check if new passwords match
        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "New passwords do not match!");
            return;
        }

        // Update admin credentials
        if (AdminManager.updateAdminCredentials(currentPassword, newUsername, newPassword)) {
            showAlert("Success", "Admin profile updated successfully!\nNew Username: " + newUsername);
            closeWindow();
        } else {
            showAlert("Error", "Current password is incorrect!");
        }
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
