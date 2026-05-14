package com.brianwallenrod.thehollowthrone.controllers;

import com.brianwallenrod.thehollowthrone.Main;
import com.brianwallenrod.thehollowthrone.Session;
import com.brianwallenrod.thehollowthrone.game.CharacterClass;
import com.brianwallenrod.thehollowthrone.game.GameCharacter;
import com.brianwallenrod.thehollowthrone.save.SaveManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CharacterCreateController {

    @FXML private TextField nameField;
    @FXML private ComboBox<CharacterClass> classComboBox;
    @FXML private Label statsLabel;

    @FXML
    public void initialize() {
        classComboBox.setItems(FXCollections.observableArrayList(CharacterClass.values()));
        classComboBox.setOnAction(e -> updateStatsPreview());
    }

    private void updateStatsPreview() {
        CharacterClass selected = classComboBox.getValue();
        if (selected == null) return;

        GameCharacter preview = new GameCharacter("preview", selected);
        statsLabel.setText(
                "HP:  " + preview.getMaxHp() + "\n" +
                        "STR: " + preview.getStrength() + "\n" +
                        "DEX: " + preview.getDexterity() + "\n" +
                        "INT: " + preview.getIntelligence()
        );
    }

    @FXML
    private void handleCreate() {
        String name = nameField.getText().trim();
        CharacterClass selectedClass = classComboBox.getValue();

        if (name.isEmpty()) {
            showAlert("Please enter a character name.");
            return;
        }
        if (selectedClass == null) {
            showAlert("Please select a class.");
            return;
        }

        GameCharacter character = new GameCharacter(name, selectedClass);
        Session.setCharacter(character);
        SaveManager.saveCharacter(Session.getCurrentUser(), character);

        try {
            Main.switchScene("main-game");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}