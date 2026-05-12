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
        stage.setResizable(false);
        switchScene("login");
        stage.show();
    }

    public static void switchScene(String fxmlName) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/com/brianwallenrod/thehollowthrone/fxml/" + fxmlName + ".fxml")
        );
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}