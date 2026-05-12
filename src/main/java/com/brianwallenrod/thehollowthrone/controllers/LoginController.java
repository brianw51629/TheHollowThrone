package com.brianwallenrod.thehollowthrone.controllers;

import com.brianwallenrod.thehollowthrone.Main;
import com.brianwallenrod.thehollowthrone.Session;
import com.brianwallenrod.thehollowthrone.auth.AuthManager;
import com.brianwallenrod.thehollowthrone.save.SaveManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Please enter both username and password.");
            return;
        }

        boolean success = AuthManager.login(username, password);

        if (success) {
            Session.setCurrentUser(username);
            if (SaveManager.hasSave(username)) {
                Main.switchScene("main-game");
            } else {
                Main.switchScene("create-character");
            }
        } else {
            showAlert("Invalid username or password.");
        }
    }

    @FXML
    private void handleRegister() {
        try {
            Main.switchScene("register");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}