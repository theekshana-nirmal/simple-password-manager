package com.example.controllers;

import com.example.App;
import com.example.models.User;
import com.example.utils.CSVHandler;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    private ImageView closeButton;

    @FXML
    private TableView<PasswordEntry> passwordTable;

    @FXML
    private TableColumn<PasswordEntry, String> websiteColumn;

    @FXML
    private TableColumn<PasswordEntry, String> usernameColumn;

    @FXML
    private TableColumn<PasswordEntry, String> passwordColumn;

    @FXML
    private TableColumn<PasswordEntry, Void> actionsColumn;
    @FXML
    private Button backToLoginButton;

    @FXML
    private Button addPasswordButton;

    @FXML
    private Label welcomeUserLabel;

    private ObservableList<PasswordEntry> passwordData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupWelcomeMessage();
        loadUserData();
    }

    private void setupWelcomeMessage() {
        User currentUser = UserManager.getCurrentUser();
        if (currentUser != null) {
            welcomeUserLabel.setText(currentUser.getUsername());
        } else {
            welcomeUserLabel.setText("Guest User");
            // If no user is logged in, redirect to login
            try {
                App.setRoot("fxml/login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupTableColumns() {
        // Set up data columns
        websiteColumn.setCellValueFactory(new PropertyValueFactory<>("website"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password")); // Set up actions column with icons
        actionsColumn.setCellFactory(param -> new javafx.scene.control.TableCell<PasswordEntry, Void>() {
            private final Button viewButton = new Button("👁");
            private final Button editButton = new Button("✏");
            private final Button deleteButton = new Button("🗑");
            private final HBox actionBox = new HBox(5);

            {
                // Add style class to this cell
                getStyleClass().add("actions-cell");

                // Try to load action icons, fallback to text if not available
                try {
                    InputStream viewIconStream = getClass()
                            .getResourceAsStream("/com/example/images/action-icons/view-icon.png");
                    InputStream editIconStream = getClass()
                            .getResourceAsStream("/com/example/images/action-icons/edit-icon.png");
                    InputStream deleteIconStream = getClass()
                            .getResourceAsStream("/com/example/images/action-icons/delete-icon.png");
                    if (viewIconStream != null) {
                        ImageView viewIcon = new ImageView(new Image(viewIconStream));
                        viewIcon.setFitHeight(16);
                        viewIcon.setFitWidth(16);
                        viewButton.setGraphic(viewIcon);
                        viewButton.setText("");
                    } else {
                        viewButton.setText("👁"); // Keep emoji fallback if icon not found
                    }

                    if (editIconStream != null) {
                        ImageView editIcon = new ImageView(new Image(editIconStream));
                        editIcon.setFitHeight(16);
                        editIcon.setFitWidth(16);
                        editButton.setGraphic(editIcon);
                        editButton.setText("");
                    } else {
                        editButton.setText("✏"); // Keep emoji fallback if icon not found
                    }

                    if (deleteIconStream != null) {
                        ImageView deleteIcon = new ImageView(new Image(deleteIconStream));
                        deleteIcon.setFitHeight(16);
                        deleteIcon.setFitWidth(16);
                        deleteButton.setGraphic(deleteIcon);
                        deleteButton.setText("");
                    } else {
                        deleteButton.setText("🗑"); // Keep emoji fallback if icon not found
                    }
                } catch (Exception e) {
                    System.out.println("Could not load PNG action icons, using emoji fallbacks: " + e.getMessage());
                    // Set emoji fallbacks if there's any exception
                    if (viewButton.getGraphic() == null && viewButton.getText().isEmpty()) {
                        viewButton.setText("👁");
                    }
                    if (editButton.getGraphic() == null && editButton.getText().isEmpty()) {
                        editButton.setText("✏");
                    }
                    if (deleteButton.getGraphic() == null && deleteButton.getText().isEmpty()) {
                        deleteButton.setText("🗑");
                    }
                } // Style buttons
                viewButton.getStyleClass().add("action-btn");
                editButton.getStyleClass().add("action-btn");
                deleteButton.getStyleClass().add("action-btn");

                // Set button actions
                viewButton.setOnAction(e -> handleViewAction(getTableRow().getItem()));
                editButton.setOnAction(e -> handleEditAction(getTableRow().getItem()));
                deleteButton.setOnAction(e -> handleDeleteAction(getTableRow().getItem()));

                actionBox.getChildren().addAll(viewButton, editButton, deleteButton);
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
    }

    private void loadUserData() {
        User currentUser = UserManager.getCurrentUser();
        if (currentUser != null) {
            // Load passwords from user-specific CSV file
            List<PasswordEntry> loadedPasswords = CSVHandler.loadUserPasswordsFromCSV(currentUser.getUsername());

            // Clear existing data and add loaded data
            passwordData.clear();
            passwordData.addAll(loadedPasswords);

            System.out.println(
                    "Loaded " + passwordData.size() + " password entries for user: " + currentUser.getUsername());
        } else {
            // If no user is logged in, redirect to login
            try {
                App.setRoot("fxml/login");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Always set the items, even if empty
        passwordTable.setItems(passwordData);

        // Print status message
        if (passwordData.isEmpty()) {
            System.out.println("No password data loaded. Table will be empty.");
        }
    }

    private void handleViewAction(PasswordEntry entry) {
        System.out.println("View action for: " + entry.getWebsite());
        // Implement view functionality for user mode
    }

    private void handleEditAction(PasswordEntry entry) {
        if (entry == null) {
            System.out.println("No entry selected for editing");
            return;
        }

        try {
            // Load the change-saved-data FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxml/change-saved-data.fxml"));
            Parent root = loader.load();

            // Create a new stage for the popup
            Stage editStage = new Stage();
            editStage.initModality(Modality.APPLICATION_MODAL);
            editStage.initStyle(StageStyle.TRANSPARENT);
            editStage.setTitle("Edit Password Entry");

            // Create scene and set it transparent
            Scene scene = new Scene(root);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

            // Apply CSS stylesheets
            scene.getStylesheets().add(getClass().getResource("/com/example/css/reset.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/com/example/css/styles.css").toExternalForm());

            editStage.setScene(scene);

            // Make the window draggable
            makeDraggable(root, editStage);

            // Get the controller and set up the data
            ChangeSavedDataController controller = loader.getController();
            controller.setStage(editStage);
            controller.setPasswordEntry(entry);

            // Set up callback to save data when the dialog is closed
            controller.setOnSaveCallback(() -> {
                // Save updated data back to user-specific CSV
                User currentUser = UserManager.getCurrentUser();
                if (currentUser != null) {
                    CSVHandler.saveUserPasswordsToCSV(currentUser.getUsername(), passwordData);
                    System.out.println("Password entry updated and saved to CSV.");

                    // Refresh the table to show updated data
                    passwordTable.refresh();
                }
            });

            // Show the popup
            editStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading edit dialog: " + e.getMessage());
        }
    }

    private void handleDeleteAction(PasswordEntry entry) {
        System.out.println("Delete action for: " + entry.getWebsite());
        // Remove from the table
        passwordData.remove(entry);

        // Save updated data back to user-specific CSV
        User currentUser = UserManager.getCurrentUser();
        if (currentUser != null) {
            CSVHandler.saveUserPasswordsToCSV(currentUser.getUsername(), passwordData);
            System.out.println("Entry deleted and user CSV updated.");
        }
    }

    @FXML
    private void handleCloseButton() {
        Platform.exit();
    }

    @FXML
    private void handleBackToLogin() {
        try {
            // Log out the current user
            UserManager.logout();
            App.setRoot("fxml/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddPassword() {
        try {
            // Load the add-password FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fxml/add-password.fxml"));
            Parent root = loader.load();

            // Create a new stage for the popup
            Stage addPasswordStage = new Stage();
            addPasswordStage.initModality(Modality.APPLICATION_MODAL);
            addPasswordStage.initStyle(StageStyle.TRANSPARENT);
            addPasswordStage.setTitle("Add Password");

            // Create scene and set it transparent
            Scene scene = new Scene(root);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

            // Apply CSS stylesheets
            scene.getStylesheets().add(getClass().getResource("/com/example/css/reset.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/com/example/css/styles.css").toExternalForm());

            addPasswordStage.setScene(scene);

            // Make the window draggable
            makeDraggable(root, addPasswordStage);

            // Get the controller and set up callback for saving
            AddPasswordController controller = loader.getController();
            controller.setParentController(this);
            controller.setStage(addPasswordStage);

            // Show the popup
            addPasswordStage.showAndWait();

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

    /**
     * Refresh the password data from CSV file
     */
    public void refreshData() {
        loadUserData();
    }

    /**
     * Add a new password entry and save to CSV
     */
    public void addPasswordEntry(String website, String username, String password) {
        PasswordEntry newEntry = new PasswordEntry(website, username, password);
        passwordData.add(newEntry);

        User currentUser = UserManager.getCurrentUser();
        if (currentUser != null) {
            CSVHandler.saveUserPasswordsToCSV(currentUser.getUsername(), passwordData);
            System.out.println("New password entry added and saved to user CSV.");
        }
    }

    // Data model class
    public static class PasswordEntry {
        private String website;
        private String username;
        private String password;

        public PasswordEntry(String website, String username, String password) {
            this.website = website;
            this.username = username;
            this.password = password;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
