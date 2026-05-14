package com.brianwallenrod.thehollowthrone.controllers;

import com.brianwallenrod.thehollowthrone.Main;
import com.brianwallenrod.thehollowthrone.Session;
import com.brianwallenrod.thehollowthrone.game.GameCharacter;
import com.brianwallenrod.thehollowthrone.game.Item;
import com.brianwallenrod.thehollowthrone.save.SaveManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopController {

    @FXML private Label goldLabel;
    @FXML private Label item1Name, item1Desc, item1Price;
    @FXML private Label item2Name, item2Desc, item2Price;
    @FXML private Label item3Name, item3Desc, item3Price;
    @FXML private Button buyButton1, buyButton2, buyButton3;

    private GameCharacter character;
    private List<Item> shopItems;
    private List<Integer> shopPrices;
    private int floor;

    @FXML
    public void initialize() {
        character = Session.getCharacter();
        floor = Session.getWorldMap().getCurrentFloorNumber();
        generateShop();
        updateGoldLabel();
    }

    private void generateShop() {
        // All possible items
        List<Item> allItems = new ArrayList<>();
        allItems.add(Item.healthPotion());
        allItems.add(Item.strengthElixir());
        allItems.add(Item.dexterityElixir());
        allItems.add(Item.antidote());
        allItems.add(character.getDamageItem());

        // Pick 3 random items
        Collections.shuffle(allItems);
        shopItems = allItems.subList(0, 3);
        shopPrices = new ArrayList<>();

        for (Item item : shopItems) {
            shopPrices.add(calculatePrice(item, floor));
        }

        // Populate labels
        setItemLabels(item1Name, item1Desc, item1Price, buyButton1, 0);
        setItemLabels(item2Name, item2Desc, item2Price, buyButton2, 1);
        setItemLabels(item3Name, item3Desc, item3Price, buyButton3, 2);
    }

    private void setItemLabels(Label nameLabel, Label descLabel,
                               Label priceLabel, Button buyBtn, int index) {
        Item item = shopItems.get(index);
        int price = shopPrices.get(index);
        nameLabel.setText(item.getName());
        descLabel.setText(item.getDescription());
        priceLabel.setText(price + " gold");
        buyBtn.setDisable(character.getGold() < price);
    }

    private int calculatePrice(Item item, int floor) {
        int basePrice = switch (item.getType()) {
            case HEALTH_POTION    -> 30;
            case STRENGTH_ELIXIR  -> 40;
            case DEXTERITY_ELIXIR -> 40;
            case ANTIDOTE         -> 20;
            case DAMAGE_ITEM      -> 50;
        };
        return basePrice + (floor * 10);
    }

    private void updateGoldLabel() {
        goldLabel.setText("Gold: " + character.getGold());
        // Update buy button states
        for (int i = 0; i < 3; i++) {
            Button btn = switch (i) {
                case 0 -> buyButton1;
                case 1 -> buyButton2;
                default -> buyButton3;
            };
            btn.setDisable(character.getGold() < shopPrices.get(i));
        }
    }

    private void handleBuy(int index) {
        int price = shopPrices.get(index);
        if (character.getGold() < price) {
            showAlert("Not enough gold!");
            return;
        }
        if (character.getInventory().isFull()) {
            showAlert("Your inventory is full!");
            return;
        }

        Item item = shopItems.get(index);
        character.setGold(character.getGold() - price);
        character.getInventory().addItem(item);
        SaveManager.saveCharacter(Session.getCurrentUser(), character);
        updateGoldLabel();
        showInfo("You bought " + item.getName() + "!");
    }

    @FXML private void handleBuy1() { handleBuy(0); }
    @FXML private void handleBuy2() { handleBuy(1); }
    @FXML private void handleBuy3() { handleBuy(2); }

    @FXML
    private void handleBack() {
        try {
            Main.switchScene("main-game");
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
}