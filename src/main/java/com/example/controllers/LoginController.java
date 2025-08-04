package com.example.controllers;

import java.io.IOException;

import com.example.App;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class LoginController {

    @FXML
    private ImageView closeButton;

    @FXML
    private void handleCloseButton() {
        // Close the application
        Platform.exit();
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
}
