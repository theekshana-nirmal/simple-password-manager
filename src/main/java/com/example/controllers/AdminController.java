package com.example.controllers;

import com.example.App;
import com.example.models.User;
import com.example.utils.UserManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private ImageView closeButton;

    @FXML
    private Button changePasswordButton;

    @FXML
    private Button logoutButton;
    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, Void> actionsColumn;

    private ObservableList<User> userData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadUserData();
    }

    private void setupTableColumns() {
        // Set up data columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Set up actions column with delete button
        actionsColumn.setCellFactory(param -> new javafx.scene.control.TableCell<User, Void>() {
            private final Button deleteButton = new Button();
            private final HBox actionBox = new HBox(5);

            {
                // Add style class to this cell for alignment
                getStyleClass().add("actions-cell");

                // Try to load delete icon, fallback to text if not available
                try {
                    java.io.InputStream deleteIconStream = getClass()
                            .getResourceAsStream("/com/example/images/action-icons/delete-icon.png");
                    if (deleteIconStream != null) {
                        javafx.scene.image.ImageView deleteIcon = new javafx.scene.image.ImageView(
                                new javafx.scene.image.Image(deleteIconStream));
                        deleteIcon.setFitHeight(16);
                        deleteIcon.setFitWidth(16);
                        deleteButton.setGraphic(deleteIcon);
                        deleteButton.setText("");
                    } else {
                        deleteButton.setText("🗑");
                    }
                } catch (Exception e) {
                    deleteButton.setText("🗑");
                }
                deleteButton.getStyleClass().addAll("btn", "delete", "action-btn");
                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    if (user != null) {
                        handleDeleteUser(user);
                    }
                });
                actionBox.getChildren().add(deleteButton);
                actionBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionBox);
                    setAlignment(javafx.geometry.Pos.CENTER_LEFT); // Align the cell content to left
                }
            }
        });

        usersTable.setItems(userData);
    }

    private void loadUserData() {
        userData.clear();
        List<User> users = UserManager.getAllUsers();
        userData.addAll(users);
    }

    private void handleDeleteUser(User user) {
        // Show confirmation dialog
        javafx.scene.control.Alert confirmAlert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete User Account");
        confirmAlert.setContentText("Are you sure you want to delete user '" + user.getUsername() + "'?\n\n" +
                "This will permanently delete:\n" +
                "• User account information\n" +
                "• All saved passwords for this user\n" +
                "• This action cannot be undone!");

        java.util.Optional<javafx.scene.control.ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            if (UserManager.deleteUser(user.getEmail())) {
                userData.remove(user);

                // Show success message
                javafx.scene.control.Alert successAlert = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("User Deleted");
                successAlert.setContentText(
                        "User '" + user.getUsername() + "' and all associated data have been successfully deleted.");
                successAlert.showAndWait();

                System.out.println("User and all data deleted: " + user.getEmail());
            } else {
                // Show error message
                javafx.scene.control.Alert errorAlert = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Deletion Failed");
                errorAlert.setContentText("Failed to delete user '" + user.getUsername() + "'. Please try again.");
                errorAlert.showAndWait();

                System.out.println("Failed to delete user: " + user.getEmail());
            }
        }
    }

    @FXML
    private void handleCloseButton() {
        Platform.exit();
    }

    @FXML
    private void handleChangePassword() {
        try {
            // Load the change-password FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxml/change-password.fxml"));
            Parent root = loader.load();

            // Create a new stage for the popup
            Stage changePasswordStage = new Stage();
            changePasswordStage.initModality(Modality.APPLICATION_MODAL);
            changePasswordStage.initStyle(StageStyle.TRANSPARENT);
            changePasswordStage.setTitle("Change Password");

            // Create scene and set it transparent
            Scene scene = new Scene(root);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

            // Apply CSS stylesheets
            scene.getStylesheets().add(getClass().getResource("/com/example/css/reset.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/com/example/css/styles.css").toExternalForm());

            changePasswordStage.setScene(scene);

            // Make the window draggable
            makeDraggable(root, changePasswordStage);
            // Get the controller and set up callback
            ChangePasswordController controller = loader.getController();
            controller.setStage(changePasswordStage);

            // Show the popup
            changePasswordStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            App.setRoot("fxml/admin-login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to make the popup draggable
    private void makeDraggable(Parent root, Stage stage) {
        final double[] xOffset = { 0 };
        final double[] yOffset = { 0 };

        root.setOnMousePressed(event -> {
            xOffset[0] = event.getSceneX();
            yOffset[0] = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset[0]);
            stage.setY(event.getScreenY() - yOffset[0]);
        });
    }
}
