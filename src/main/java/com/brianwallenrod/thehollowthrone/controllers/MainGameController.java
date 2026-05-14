package com.brianwallenrod.thehollowthrone.controllers;

import com.brianwallenrod.thehollowthrone.CombatSession;
import com.brianwallenrod.thehollowthrone.Main;
import com.brianwallenrod.thehollowthrone.Session;
import com.brianwallenrod.thehollowthrone.combat.Enemy;
import com.brianwallenrod.thehollowthrone.combat.EnemyPool;
import com.brianwallenrod.thehollowthrone.game.GameCharacter;
import com.brianwallenrod.thehollowthrone.save.SaveManager;
import com.brianwallenrod.thehollowthrone.world.WorldMap;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import com.brianwallenrod.thehollowthrone.world.Floor;
import com.brianwallenrod.thehollowthrone.world.Room;
import com.brianwallenrod.thehollowthrone.CombatSession;
import com.brianwallenrod.thehollowthrone.combat.Enemy;
import com.brianwallenrod.thehollowthrone.combat.EnemyPool;

import java.io.IOException;
import java.util.Objects;

public class MainGameController {

    @FXML private ImageView characterView;
    @FXML private GridPane mapGrid;
    @FXML private Label nameLabel;
    @FXML private Label classLabel;
    @FXML private Label levelLabel;
    @FXML private Label hpLabel;
    @FXML private Label strLabel;
    @FXML private Label dexLabel;
    @FXML private Label intLabel;
    @FXML private Label xpLabel;

    private GameCharacter character;
    private WorldMap worldMap;


    @FXML
    public void initialize() {
        character = SaveManager.loadCharacter(Session.getCurrentUser());

        // Only create new world if one doesn't exist yet
        if (Session.getWorldMap() == null) {
            Session.setWorldMap(new WorldMap());
        }
        worldMap = Session.getWorldMap();

        if (character != null) {
            updateStats();
        }
        loadCharacterImage();
        renderMap();
    }

    private void updateStats() {
        nameLabel.setText("Name: " + character.getName());
        classLabel.setText("Class: " + character.getCharacterClass());
        levelLabel.setText("Level: " + character.getLevel() + " | Floor: " + worldMap.getCurrentFloorNumber());
        hpLabel.setText("HP: " + character.getHp() + "/" + character.getMaxHp());
        strLabel.setText("STR: " + character.getStrength());
        dexLabel.setText("DEX: " + character.getDexterity());
        intLabel.setText("INT: " + character.getIntelligence());
        xpLabel.setText("XP: " + character.getXp() + " | Gold: " + character.getGold());
        hpLabel.setText("HP: " + character.getHp() + "/" + character.getMaxHp() + " | Lives: " + character.getLives());
    }

    private void loadCharacterImage() {
        try {
            var url = getClass().getResource("/com/brianwallenrod/thehollowthrone/assets/placeHolder.gif");
            System.out.println("GIF URL: " + url);

            if (url == null) {
                System.out.println("Resource not found - checking target folder...");
                return;
            }

            Image gif = new Image(url.toExternalForm());
            characterView.setImage(gif);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void renderMap() {
        mapGrid.getChildren().clear();

        Floor floor = worldMap.getCurrentFloor();
        Room[][] grid = floor.getGrid();
        int size = floor.getSize();
        int playerRow = floor.getPlayerRow();
        int playerCol = floor.getPlayerCol();

        int displaySize = 5;
        int half = displaySize / 2;

        for (int dr = -half; dr <= half; dr++) {
            for (int dc = -half; dc <= half; dc++) {
                int worldRow = playerRow + dr;
                int worldCol = playerCol + dc;
                int gridRow = dr + half;
                int gridCol = dc + half;

                Rectangle cell = new Rectangle();
                cell.widthProperty().bind(mapGrid.widthProperty().divide(displaySize).subtract(6));
                cell.heightProperty().bind(mapGrid.heightProperty().divide(displaySize).subtract(6));
                cell.setStroke(Color.DARKGRAY);
                cell.setStrokeWidth(1.5);

                if (dr == 0 && dc == 0) {
                    cell.setFill(Color.CORNFLOWERBLUE);
                } else if (worldRow < 0 || worldRow >= size || worldCol < 0 || worldCol >= size) {
                    cell.setFill(Color.BLACK);
                } else {
                    Room room = grid[worldRow][worldCol];
                    if (!room.isRevealed()) {
                        cell.setFill(Color.DIMGRAY);
                    } else if (room.isConsumed()) {
                        cell.setFill(Color.LIGHTGRAY);
                    } else {
                        cell.setFill(getRoomColor(room.getType()));
                    }

                    final int finalDr = dr;
                    final int finalDc = dc;
                    cell.setOnMouseClicked(e -> {
                        try {
                            handleMove(finalDr, finalDc);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    cell.setStyle("-fx-cursor: hand;");
                }

                mapGrid.add(cell, gridCol, gridRow);
            }
        }
    }

    private Color getRoomColor(Room.RoomType type) {
        return switch (type) {
            case EMPTY    -> Color.LIGHTGRAY;
            case ENEMY    -> Color.SALMON;
            case ELITE    -> Color.ORANGERED;
            case TREASURE -> Color.GOLD;
            case SHOP     -> Color.MEDIUMPURPLE;
            case TRAP     -> Color.DARKRED;
            case HEAL     -> Color.LIGHTGREEN;
            case EXIT     -> Color.CYAN;
            case BOSS     -> Color.CRIMSON;
            case BOSS_DOOR -> Color.BLACK;
        };
    }

    private void handleMove(int dRow, int dCol) throws IOException {
        Floor floor = worldMap.getCurrentFloor();
        boolean moved = floor.movePlayer(dRow, dCol);
        if (moved) {
            renderMap();
            handleRoomEvent();
        }
    }

    private void handleRoomEvent() throws IOException {
        Room room = worldMap.getCurrentFloor().getCurrentRoom();

        if (room.isConsumed()) return; // already triggered, do nothing

        switch (room.getType()) {
            case HEAL -> {
                int healAmount = character.getMaxHp() / 4;
                character.heal(healAmount);
                updateStats();
                room.setConsumed(true);
                showInfo("You found a healing spring! Restored " + healAmount + " HP.");
            }
            case TRAP -> {
                int damage = 10 + (worldMap.getCurrentFloorNumber() * 5);
                character.takeDamage(damage);
                updateStats();
                room.setConsumed(true);
                if (!character.isAlive()) {
                    showInfo("You died! Game over.");
                } else {
                    showInfo("You triggered a trap! Took " + damage + " damage.");
                }
            }
            case TREASURE -> {
                int goldFound = 10 + (int)(Math.random() * 20) + (worldMap.getCurrentFloorNumber() * 5);
                character.setGold(character.getGold() + goldFound);
                updateStats();
                room.setConsumed(true);
                showInfo("You found " + goldFound + " gold!");
            }
            case EXIT -> {
                worldMap.descend();
                renderMap();
                showInfo("You descend to floor " + worldMap.getCurrentFloorNumber() + "!");
            }
            case ENEMY -> {
                Enemy enemy = EnemyPool.getEnemy(character.getCharacterClass(),
                        worldMap.getCurrentFloorNumber(), false);
                CombatSession.start(enemy);
                room.setConsumed(true);
                try { Main.switchScene("combat"); } catch (Exception e) { e.printStackTrace(); }
            }
            case ELITE -> {
                Enemy enemy = EnemyPool.getEnemy(character.getCharacterClass(),
                        worldMap.getCurrentFloorNumber(), false);
                Enemy elite = new Enemy("Elite: " + enemy.getName(),
                        (int)(enemy.getMaxHp() * 1.5),
                        (int)(enemy.getStrength() * 1.3),
                        (int)(enemy.getDexterity() * 1.3),
                        (int)(enemy.getXpReward() * 1.5),
                        (int)(enemy.getGoldReward() * 1.5),
                        false);
                CombatSession.start(elite);
                room.setConsumed(true);
                try { Main.switchScene("combat"); } catch (Exception e) { e.printStackTrace(); }
            }
            case BOSS -> {
                Enemy boss = EnemyPool.getEnemy(character.getCharacterClass(),
                        worldMap.getCurrentFloorNumber(), true);
                CombatSession.start(boss);
                room.setConsumed(true);
                try { Main.switchScene("combat"); } catch (Exception e) { e.printStackTrace(); }
            }
            default -> {}

        }
    }
    @FXML
    private void handleSave() {
        if (character != null) {
            SaveManager.saveCharacter(Session.getCurrentUser(), character);
            showInfo("Game saved!");
        }
    }

    @FXML
    private void handleLoad() {
        character = SaveManager.loadCharacter(Session.getCurrentUser());
        if (character != null) {
            updateStats();
            showInfo("Game loaded!");
        }
    }

    @FXML
    private void handleInventory() {
        // Coming soon
        showInfo("Inventory coming soon!");
    }

    @FXML
    private void handleQuit() {
        if (character != null) {
            SaveManager.saveCharacter(Session.getCurrentUser(), character);
        }
        try {
            Session.clear();
            Main.switchScene("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(Main.getPrimaryStage());
        alert.setTitle("The Hollow Throne");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initModality(javafx.stage.Modality.APPLICATION_MODAL);

        // Make it small and centered
        alert.getDialogPane().setPrefWidth(400);
        alert.getDialogPane().setPrefHeight(150);
        alert.showAndWait();
    }
}