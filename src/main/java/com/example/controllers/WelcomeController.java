package com.example.controllers;

import com.example.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class WelcomeController {

    @FXML
    private Button getStartedButton;

    @FXML
    private Label adminLoginLabel;

    @FXML
    private ImageView closeButton;

    @FXML
    private void handleCloseButton() {
        Platform.exit();
    }

    @FXML
    private void handleGetStarted() {
        try {
            App.setRoot("fxml/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdminLogin() {
        try {
            App.setRoot("fxml/admin-login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
