package com.brianwallenrod.thehollowthrone.controllers;

import com.brianwallenrod.thehollowthrone.Main;
import com.brianwallenrod.thehollowthrone.auth.AuthManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirm)) {
            showAlert("Passwords do not match.");
            return;
        }

        if (password.length() < 4) {
            showAlert("Password must be at least 4 characters.");
            return;
        }

        boolean success = AuthManager.register(username, password);

        if (success) {
            showInfo("Account created! Please log in.");
            try {
                Main.switchScene("login");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Username already taken.");
        }
    }

    @FXML
    private void handleBack() {
        try {
            Main.switchScene("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Register");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Register");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}