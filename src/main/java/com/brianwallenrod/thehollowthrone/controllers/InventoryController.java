package com.brianwallenrod.thehollowthrone.controllers;

import com.brianwallenrod.thehollowthrone.CombatSession;
import com.brianwallenrod.thehollowthrone.Main;
import com.brianwallenrod.thehollowthrone.Session;
import com.brianwallenrod.thehollowthrone.game.GameCharacter;
import com.brianwallenrod.thehollowthrone.game.Item;
import com.brianwallenrod.thehollowthrone.save.SaveManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import com.brianwallenrod.thehollowthrone.combat.CombatEngine;
import com.brianwallenrod.thehollowthrone.CombatSession;

public class InventoryController {

    @FXML private ListView<String> itemListView;
    @FXML private Label slotsLabel;
    @FXML private Label descriptionLabel;

    private GameCharacter character;

    @FXML
    public void initialize() {
        character = Session.getCharacter();
        refreshInventory();

        // Show description when item is selected
        itemListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int index = newVal.intValue();
            if (index >= 0 && index < character.getInventory().getSize()) {
                Item item = character.getInventory().getItem(index);
                descriptionLabel.setText(item.getDescription());
            }
        });
    }

    private void refreshInventory() {
        ObservableList<String> itemNames = FXCollections.observableArrayList();
        for (Item item : character.getInventory().getItems()) {
            itemNames.add(item.getName() + " — " + item.getDescription());
        }
        itemListView.setItems(itemNames);
        slotsLabel.setText("Items: " + character.getInventory().getSize() + "/10");

        if (character.getInventory().isEmpty()) {
            descriptionLabel.setText("Your inventory is empty.");
        }
    }

    @FXML
    private void handleUseItem() {
        int index = itemListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showAlert("Please select an item to use.");
            return;
        }

        Item item = character.getInventory().getItem(index);
        boolean inCombat = "combat".equals(returnScene);

        switch (item.getType()) {
            case HEALTH_POTION -> {
                character.heal(item.getValue());
                character.getInventory().removeItem(item);
                if (inCombat) CombatSession.setPlayerTurn(false);
                showInfo("You used a Health Potion! Restored " + item.getValue() + " HP.");
            }
            case STRENGTH_ELIXIR -> {
                if (!inCombat) {
                    showAlert("You can only use this during combat!");
                    return;
                }
                character.applyStrBoost(item.getValue());
                character.getInventory().removeItem(item);
                CombatSession.setPlayerTurn(false);
                showInfo("You used a Strength Elixir! STR +" + item.getValue() + " for this combat.");
            }
            case DEXTERITY_ELIXIR -> {
                if (!inCombat) {
                    showAlert("You can only use this during combat!");
                    return;
                }
                character.applyDexBoost(item.getValue());
                character.getInventory().removeItem(item);
                CombatSession.setPlayerTurn(false);
                showInfo("You used a Dexterity Elixir! DEX +" + item.getValue() + " for this combat.");
            }
            case DAMAGE_ITEM -> {
                if (!inCombat) {
                    showAlert("You can only use this during combat!");
                    return;
                }
                int damage = CombatEngine.useItem(character, CombatSession.getEnemy(), item);
                character.getInventory().removeItem(item);
                CombatSession.setPlayerTurn(false);
                if (!CombatSession.getEnemy().isAlive()) {
                    showInfo("You used " + item.getName() + " and defeated the enemy!");
                } else {
                    showInfo("You used " + item.getName() + " for " + damage + " damage!");
                }
            }
            case ANTIDOTE -> {
                character.getInventory().removeItem(item);
                if (inCombat) CombatSession.setPlayerTurn(false);
                showInfo("You used an Antidote!");
            }
        }

        SaveManager.saveCharacter(Session.getCurrentUser(), character);
        refreshInventory();
    }

    @FXML
    private void handleDropItem() {
        int index = itemListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showAlert("Please select an item to drop.");
            return;
        }

        Item item = character.getInventory().getItem(index);
        character.getInventory().removeItem(item);
        SaveManager.saveCharacter(Session.getCurrentUser(), character);
        refreshInventory();
        showInfo("You dropped " + item.getName() + ".");
    }

    @FXML
    private void handleBack() {
        try {
            Main.switchScene(returnScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(Main.getPrimaryStage());
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setPrefWidth(400);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(Main.getPrimaryStage());
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setPrefWidth(400);
        alert.showAndWait();
    }
    private static String returnScene = "main-game";
    public static String getReturnScene() { return returnScene; }

    public static void setReturnScene(String scene) {
        returnScene = scene;
    }
}