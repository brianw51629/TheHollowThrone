package com.brianwallenrod.thehollowthrone.controllers;

import com.brianwallenrod.thehollowthrone.CombatSession;
import com.brianwallenrod.thehollowthrone.Main;
import com.brianwallenrod.thehollowthrone.Session;
import com.brianwallenrod.thehollowthrone.combat.CombatEngine;
import com.brianwallenrod.thehollowthrone.combat.Enemy;
import com.brianwallenrod.thehollowthrone.combat.EnemyPool;
import com.brianwallenrod.thehollowthrone.game.GameCharacter;
import com.brianwallenrod.thehollowthrone.save.SaveManager;
import com.brianwallenrod.thehollowthrone.world.WorldMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import com.brianwallenrod.thehollowthrone.game.Inventory;
import com.brianwallenrod.thehollowthrone.game.Item;

public class CombatController {

    @FXML private Label floorLabel;
    @FXML private Label playerHpLabel;
    @FXML private Label enemyNameLabel;
    @FXML private Label enemyHpLabel;
    @FXML private ImageView playerView;
    @FXML private ImageView enemyView;
    @FXML private TextArea combatLog;
    @FXML private Button attackButton;
    @FXML private Button specialButton;
    @FXML private Button itemButton;
    @FXML private Button fleeButton;

    private GameCharacter player;
    private Enemy enemy;
    private boolean playerTurn;
    private boolean combatOver;
    @FXML
    public void initialize() {
        player = SaveManager.loadCharacter(Session.getCurrentUser());
        WorldMap worldMap = Session.getWorldMap();
        enemy = CombatSession.getEnemy();

        floorLabel.setText("Floor " + worldMap.getCurrentFloorNumber());
        updateHpLabels();
        loadPlayerImage();

        specialButton.setText(CombatEngine.getSpecialName(player));

        playerTurn = CombatEngine.playerGoesFirst(player, enemy);
        combatOver = false;

        if (playerTurn) {
            log("You move first!");
        } else {
            log(enemy.getName() + " moves first!");
            disableButtons();
            Platform.runLater(() -> enemyTurn());
        }
    }

    private void updateHpLabels() {
        playerHpLabel.setText("HP: " + player.getHp() + "/" + player.getMaxHp());
        enemyNameLabel.setText(enemy.getName());
        enemyHpLabel.setText("HP: " + enemy.getHp() + "/" + enemy.getMaxHp());
    }

    private void loadPlayerImage() {
        try {
            var url = getClass().getResource("/com/brianwallenrod/thehollowthrone/assets/placeHolder.gif");
            if (url != null) {
                playerView.setImage(new Image(url.toExternalForm()));
            }
        } catch (Exception e) {
            System.out.println("Could not load player image: " + e.getMessage());
        }
    }

    @FXML
    private void handleAttack() {
        if (!playerTurn || combatOver) return;

        int damage = CombatEngine.playerAttack(player);
        enemy.takeDamage(damage);
        log("You attack " + enemy.getName() + " for " + damage + " damage!");
        updateHpLabels();

        if (!enemy.isAlive()) {
            handleVictory();
            return;
        }

        playerTurn = false;
        disableButtons();
        enemyTurn();
    }

    @FXML
    private void handleSpecial() {
        if (!playerTurn || combatOver) return;

        int damage = CombatEngine.playerSpecial(player);
        enemy.takeDamage(damage);
        log("You use " + CombatEngine.getSpecialName(player) + " on " + enemy.getName() + " for " + damage + " damage!");
        updateHpLabels();

        if (!enemy.isAlive()) {
            handleVictory();
            return;
        }

        playerTurn = false;
        disableButtons();
        enemyTurn();
    }



    @FXML
    private void handleFlee() {
        if (!playerTurn || combatOver) return;

        boolean fled = CombatEngine.attemptFlee(player, enemy);
        if (fled) {
            combatOver = true;
            log("You successfully fled!");
            disableButtons();
            returnToGame();
        } else {
            log("You failed to flee!");
            playerTurn = false;
            disableButtons();
            enemyTurn();
        }
    }

    private void enemyTurn() {
        if (combatOver) return;

        int damage = CombatEngine.enemyAttack(enemy);
        player.takeDamage(damage);
        log(enemy.getName() + " attacks you for " + damage + " damage!");
        updateHpLabels();

        if (!player.isAlive()) {
            handleDefeat();
            return;
        }

        playerTurn = true;
        enableButtons();
    }

    private void handleVictory() {
        combatOver = true;
        boolean leveledUp = CombatEngine.grantRewards(player, enemy);
        SaveManager.saveCharacter(Session.getCurrentUser(), player);
        log("You defeated " + enemy.getName() + "!");
        log("Gained " + enemy.getXpReward() + " XP and " + enemy.getGoldReward() + " gold!");
        if (leveledUp) {
            log("LEVEL UP! You are now level " + player.getLevel() + "!");
            log("HP, STR, DEX, and INT have increased!");
            showInfo("Level Up! You are now level " + player.getLevel() + "!");
        }
        disableButtons();
        returnToGame();
        player.clearTempBoosts();
    }

    private void returnToGame() {
        try {
            Main.switchScene("main-game");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void log(String message) {
        combatLog.appendText(message + "\n");
    }

    private void disableButtons() {
        attackButton.setDisable(true);
        specialButton.setDisable(true);
        itemButton.setDisable(true);
        fleeButton.setDisable(true);
    }

    private void enableButtons() {
        attackButton.setDisable(false);
        specialButton.setDisable(false);
        itemButton.setDisable(false);
        fleeButton.setDisable(false);
    }
    private void handleDefeat() {
        combatOver = true;
        disableButtons();
        log("You were defeated by " + enemy.getName() + "...");

        player.applyDeathPenalty();
        SaveManager.saveCharacter(Session.getCurrentUser(), player);

        if (player.getLives() <= 0) {
            handleGameOver();
        } else {
            log("You have " + player.getLives() + " lives remaining.");
            log("You lost 25% gold and XP as a penalty.");

            // Reset to floor 1
            Session.setWorldMap(new WorldMap());

            showInfo("You died! You have " + player.getLives() + " lives remaining.\n" +
                    "You lost 25% gold and XP.\nReturning to floor 1...");
            returnToGame();
            player.clearTempBoosts();
        }
    }

    private void handleGameOver() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION
        );
        alert.initOwner(Main.getPrimaryStage());
        alert.setTitle("Game Over");
        alert.setHeaderText("You have no lives remaining!");
        alert.setContentText("What would you like to do?");

        javafx.scene.control.ButtonType restartButton =
                new javafx.scene.control.ButtonType("Restart from Floor 1");
        javafx.scene.control.ButtonType newCharButton =
                new javafx.scene.control.ButtonType("Delete & New Character");

        alert.getButtonTypes().setAll(restartButton, newCharButton);
        alert.getDialogPane().setPrefWidth(400);

        alert.showAndWait().ifPresent(choice -> {
            if (choice == restartButton) {
                player.resetToFloorOne();
                Session.setWorldMap(new WorldMap());
                SaveManager.saveCharacter(Session.getCurrentUser(), player);
                try { Main.switchScene("main-game"); } catch (Exception e) { e.printStackTrace(); }
            } else {
                SaveManager.deleteSave(Session.getCurrentUser());
                Session.clear();
                try { Main.switchScene("create-character"); } catch (Exception e) { e.printStackTrace(); }
            }
        });
    }
    private void showInfo(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION
        );
        alert.initOwner(Main.getPrimaryStage());
        alert.setTitle("The Hollow Throne");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setPrefWidth(400);
        alert.showAndWait();
    }

    @FXML
    private void handleItem() {
        if (!playerTurn || combatOver) return;

        Inventory inventory = player.getInventory();
        if (inventory.isEmpty()) {
            log("You have no items!");
            return;
        }

        // Show item selection dialog
        javafx.scene.control.ChoiceDialog<Item> dialog = new javafx.scene.control.ChoiceDialog<>(
                inventory.getItem(0), inventory.getItems()
        );
        dialog.initOwner(Main.getPrimaryStage());
        dialog.setTitle("Use Item");
        dialog.setHeaderText("Select an item to use:");
        dialog.setContentText("Item:");

        // Display item names properly
        javafx.util.StringConverter<Item> converter = new javafx.util.StringConverter<>() {
            public String toString(Item item) {
                return item == null ? "" : item.getName() + " — " + item.getDescription();
            }
            public Item fromString(String s) { return null; }
        };

        dialog.getDialogPane().setPrefWidth(500);
        dialog.showAndWait().ifPresent(selectedItem -> {
            int damage = CombatEngine.useItem(player, enemy, selectedItem);
            inventory.removeItem(selectedItem);
            updateHpLabels();

            switch (selectedItem.getType()) {
                case HEALTH_POTION ->
                        log("You use a Health Potion! Restored " + selectedItem.getValue() + " HP.");
                case STRENGTH_ELIXIR ->
                        log("You drink a Strength Elixir! STR +" + selectedItem.getValue() + " for this combat.");
                case DEXTERITY_ELIXIR ->
                        log("You drink a Dexterity Elixir! DEX +" + selectedItem.getValue() + " for this combat.");
                case ANTIDOTE ->
                        log("You use an Antidote!");
                case DAMAGE_ITEM ->
                        log("You throw a " + selectedItem.getName() + " at " + enemy.getName() + " for " + damage + " damage!");
            }

            if (!enemy.isAlive()) {
                handleVictory();
                return;
            }

            playerTurn = false;
            disableButtons();
            enemyTurn();
        });
    }
}