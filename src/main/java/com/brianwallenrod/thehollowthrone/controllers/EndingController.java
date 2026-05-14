package com.brianwallenrod.thehollowthrone.controllers;

import com.brianwallenrod.thehollowthrone.Main;
import com.brianwallenrod.thehollowthrone.Session;
import com.brianwallenrod.thehollowthrone.game.GameCharacter;
import com.brianwallenrod.thehollowthrone.save.SaveManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EndingController {

    @FXML private Label characterLabel;
    @FXML private Label epilogueLabel;
    @FXML private Label statLevelLabel;
    @FXML private Label statGoldLabel;
    @FXML private Label statHpLabel;
    @FXML private Label statStrLabel;
    @FXML private Label statDexLabel;
    @FXML private Label statIntLabel;

    @FXML
    public void initialize() {
        GameCharacter character = Session.getCharacter();
        if (character == null) return;

        characterLabel.setText(character.getName() + " — " + character.getCharacterClass());
        epilogueLabel.setText(getEpilogue(character));

        statLevelLabel.setText("Level: " + character.getLevel());
        statGoldLabel.setText("Gold: " + character.getGold());
        statHpLabel.setText("HP: " + character.getHp() + "/" + character.getMaxHp());
        statStrLabel.setText("STR: " + character.getStrength());
        statDexLabel.setText("DEX: " + character.getDexterity());
        statIntLabel.setText("INT: " + character.getIntelligence());
    }

    private String getEpilogue(GameCharacter character) {
        return switch (character.getCharacterClass()) {
            case WARRIOR ->
                    "The Hollow King falls. His crown shatters against the stone floor, and for the " +
                            "first time in centuries, silence fills the throne room. You stand among the ruins " +
                            "of a kingdom that was never meant to survive — and yet here you are. The soldiers " +
                            "who once hunted you will speak your name in whispers. The kingdom will not be " +
                            "rebuilt. But it will be remembered. And so will you.";
            case MAGE ->
                    "Shadow Mage dissolves into nothing, his stolen power returning to the void from " +
                            "which it came. The arcane collapse is over. You close your eyes and feel the magic " +
                            "of the world settle back into balance — quieter now, but stable. Other mages will " +
                            "come after you. They will find a world that still has rules, still has order. They " +
                            "will never know how close it came to having neither. That is your gift to them.";
            case ROGUE ->
                    "Lord Assassin is dead. The Syndicate has no master now, no shadow to hide beneath. " +
                            "You could rebuild it. You could rule it. Instead you walk away — because the most " +
                            "dangerous thing you can do to a criminal empire is leave it leaderless. By morning " +
                            "the infighting will begin. By the end of the week it will tear itself apart. " +
                            "You planned it that way from the start.";
        };
    }

    @FXML
    private void handlePlayAgain() {
        SaveManager.deleteSave(Session.getCurrentUser());
        Session.setWorldMap(null);
        try {
            Main.switchScene("create-character");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMainMenu() {
        SaveManager.deleteSave(Session.getCurrentUser());
        Session.clear();
        try {
            Main.switchScene("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}