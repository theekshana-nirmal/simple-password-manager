package com.example.controllers;

import com.example.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

import java.io.IOException;

public class RegisterController {

    @FXML
    private ImageView closeButton;

    @FXML
    private Label loginLabel;

    @FXML
    private void handleCloseButton() {
        // Close the application
        Platform.exit();
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
}
