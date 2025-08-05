package com.example.controllers;

import com.example.utils.AdminManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {

    @FXML
    private ImageView closeButton;

    @FXML
    private PasswordField currentPasswordField;

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
        // Initialize any components if needed
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
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "New passwords do not match!");
            return;
        }

        if (newPassword.length() < 3) {
            showAlert("Error", "Password must be at least 3 characters long!");
            return;
        }

        // Verify current password and update if correct
        if (verifyAndUpdateAdminPassword(currentPassword, newPassword)) {
            showAlert("Success", "Password changed successfully!");
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

    private boolean verifyAndUpdateAdminPassword(String currentPassword, String newPassword) {
        return AdminManager.changePassword(currentPassword, newPassword);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
