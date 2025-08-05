package com.example.controllers;

/**
 * This file contains the GuestController class that manages the guest view of the password manager.
 * OOP Concept: This class demonstrates ENCAPSULATION by handling all guest view functionality.
 */

import com.example.App;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

public class GuestController implements Initializable {

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

    private ObservableList<PasswordEntry> passwordData = FXCollections.observableArrayList(); // Initializes the
                                                                                              // controller and sets up
                                                                                              // the UI components

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadSampleData();
    }

    // Configures the table columns and sets up the action buttons
    private void setupTableColumns() { // Set up data columns
        websiteColumn.setCellValueFactory(new PropertyValueFactory<>("website"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Set up password column to show asterisks for the password
        passwordColumn.setCellValueFactory(cellData -> {
            String maskedPassword = "••••••••"; // Fixed mask for demo mode
            return new javafx.beans.property.SimpleStringProperty(maskedPassword);
        });// Set up actions column with icons
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

    // Loads sample password data for demonstration purposes
    private void loadSampleData() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/sample-passwords.csv");
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                boolean firstLine = true;

                while ((line = reader.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false; // Skip header
                        continue;
                    }

                    String[] data = line.split(",");
                    if (data.length >= 3) {
                        passwordData.add(new PasswordEntry(
                                data[0].trim(),
                                data[1].trim(),
                                "••••••••" // Mask the password for security in guest mode
                        ));
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            System.err.println("Could not load sample data: " + e.getMessage());
            // Add fallback sample data
            addFallbackData();
        }

        passwordTable.setItems(passwordData);
    }

    // Adds default password entries if sample data couldn't be loaded
    private void addFallbackData() {
        passwordData.addAll(
                new PasswordEntry("Facebook", "john.doe@email.com", "••••••••"),
                new PasswordEntry("Gmail", "johndoe123", "••••••••"),
                new PasswordEntry("GitHub", "john_developer", "••••••••"));
    } // Handles the view button click to show password details

    private void handleViewAction(PasswordEntry entry) {
        if (entry == null)
            return;

        // Create a simple dialog to show password details
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Password Details");
        alert.setHeaderText("Viewing password for " + entry.getWebsite()); // Use the demo password from the
                                                                           // getDecryptedPassword method
        String demoPassword = entry.getDecryptedPassword();

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        // Add password info to grid
        grid.add(new javafx.scene.control.Label("Website:"), 0, 0);
        grid.add(new javafx.scene.control.Label(entry.getWebsite()), 1, 0);
        grid.add(new javafx.scene.control.Label("Username:"), 0, 1);
        grid.add(new javafx.scene.control.Label(entry.getUsername()), 1, 1);
        grid.add(new javafx.scene.control.Label("Password:"), 0, 2);
        grid.add(new javafx.scene.control.Label(demoPassword), 1, 2);

        // Add note about guest mode
        javafx.scene.control.Label guestNote = new javafx.scene.control.Label(
                "Note: This is demo mode. Create an account to manage real passwords.");
        guestNote.setWrapText(true);
        guestNote.setStyle("-fx-font-style: italic; -fx-text-fill: #555;");
        grid.add(guestNote, 0, 3, 2, 1);

        alert.getDialogPane().setContent(grid);
        alert.showAndWait();
    }

    // Handles the edit button click to demonstrate editing functionality
    private void handleEditAction(PasswordEntry entry) {
        if (entry == null)
            return;

        // Create a simple dialog to simulate editing
        javafx.scene.control.Dialog<PasswordEntry> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Edit Password");
        dialog.setHeaderText("Edit password for " + entry.getWebsite());

        // Set the button types
        javafx.scene.control.ButtonType saveButtonType = new javafx.scene.control.ButtonType("Save",
                javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, javafx.scene.control.ButtonType.CANCEL);

        // Create the form fields
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        javafx.scene.control.TextField website = new javafx.scene.control.TextField(entry.getWebsite());
        javafx.scene.control.TextField username = new javafx.scene.control.TextField(entry.getUsername());
        javafx.scene.control.TextField password = new javafx.scene.control.TextField("DemoPassword123!");

        grid.add(new javafx.scene.control.Label("Website:"), 0, 0);
        grid.add(website, 1, 0);
        grid.add(new javafx.scene.control.Label("Username:"), 0, 1);
        grid.add(username, 1, 1);
        grid.add(new javafx.scene.control.Label("Password:"), 0, 2);
        grid.add(password, 1, 2);

        // Add note about guest mode
        javafx.scene.control.Label guestNote = new javafx.scene.control.Label(
                "Note: This is demo mode. Changes won't be saved. Create an account to manage real passwords.");
        guestNote.setWrapText(true);
        guestNote.setStyle("-fx-font-style: italic; -fx-text-fill: #555;");
        grid.add(guestNote, 0, 3, 2, 1);

        dialog.getDialogPane().setContent(grid);

        // Show the dialog and handle the result
        dialog.showAndWait();

        // Show feedback that this is just a demo
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Guest Mode");
        alert.setHeaderText("Demo Only");
        alert.setContentText("In guest mode, changes aren't saved. Create an account to manage real passwords.");
        alert.showAndWait();
    }

    // Handles the delete button click to demonstrate deletion functionality
    private void handleDeleteAction(PasswordEntry entry) {
        if (entry == null)
            return;

        // Show confirmation dialog
        javafx.scene.control.Alert confirmDialog = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Password");
        confirmDialog.setHeaderText("Confirm Delete");
        confirmDialog.setContentText("Are you sure you want to delete the password for " + entry.getWebsite() + "?");

        java.util.Optional<javafx.scene.control.ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            // Show feedback that this is just a demo
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Guest Mode");
            alert.setHeaderText("Demo Only");
            alert.setContentText(
                    "In guest mode, passwords cannot be deleted. Create an account to manage real passwords.");
            alert.showAndWait();
        }
    }

    @FXML
    // Exits the application when the close button is clicked
    private void handleCloseButton() {
        Platform.exit();
    }

    @FXML
    // Returns to the login screen
    private void handleBackToLogin() {
        try {
            App.setRoot("fxml/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Simple data model class for guest mode password entries
     * Note: In guest mode, we don't actually use encryption since these are demo
     * entries only
     */
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

        /**
         * For compatibility with the main application's interface
         * In guest mode, this returns a demo password
         */
        public String getDecryptedPassword() {
            return "DemoPassword123!";
        }
    }
}
