package com.example.controllers;

import com.example.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class AdminController {

    @FXML
    private ImageView closeButton;

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
    private void handleRegisterClick() {
        try {
            App.setRoot("fxml/register");
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
}
