package com.example.controllers;

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
}
