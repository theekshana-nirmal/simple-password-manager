package com.example.controllers;

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

    private ObservableList<PasswordEntry> passwordData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadSampleData();
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

    private void addFallbackData() {
        passwordData.addAll(
                new PasswordEntry("Facebook", "john.doe@email.com", "••••••••"),
                new PasswordEntry("Gmail", "johndoe123", "••••••••"),
                new PasswordEntry("GitHub", "john_developer", "••••••••"));
    }

    private void handleViewAction(PasswordEntry entry) {
        System.out.println("View action for: " + entry.getWebsite());
        // In guest mode, just show a message or demo functionality
    }

    private void handleEditAction(PasswordEntry entry) {
        System.out.println("Edit action for: " + entry.getWebsite());
        // In guest mode, just show a message or demo functionality
    }

    private void handleDeleteAction(PasswordEntry entry) {
        System.out.println("Delete action for: " + entry.getWebsite());
        // In guest mode, just show a message or demo functionality
    }

    @FXML
    private void handleCloseButton() {
        Platform.exit();
    }

    @FXML
    private void handleBackToLogin() {
        try {
            App.setRoot("fxml/login");
        } catch (IOException e) {
            e.printStackTrace();
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
