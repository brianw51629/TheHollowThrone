package com.brianwallenrod.thehollowthrone;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        stage.setTitle("The Hollow Throne");
        switchScene("login");
        stage.show();
    }

    public static void switchScene(String fxmlName) throws IOException {
        var url = Main.class.getResource("/com/brianwallenrod/thehollowthrone/fxml/" + fxmlName + ".fxml");
        System.out.println("Loading: " + url);
        if (url == null) {
            System.out.println("ERROR: FXML file not found: " + fxmlName);
            return;
        }

        FXMLLoader loader = new FXMLLoader(url);
        javafx.scene.Parent root = loader.load();

        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

        primaryStage.setScene(scene);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}