package com.brianwallenrod.thehollowthrone.game;

public class Item {

    public enum ItemType {
        HEALTH_POTION,
        STRENGTH_ELIXIR,
        DEXTERITY_ELIXIR,
        ANTIDOTE,
        DAMAGE_ITEM  // Throwable Axe / Mana Bomb / Poison Dart
    }

    private String name;
    private ItemType type;
    private int value;
    private String description;

    public Item() {}

    public Item(String name, ItemType type, int value, String description) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.description = description;
    }

    // Static factory methods for easy creation
    public static Item healthPotion() {
        return new Item("Health Potion", ItemType.HEALTH_POTION, 50, "Restores 50 HP");
    }

    public static Item strengthElixir() {
        return new Item("Strength Elixir", ItemType.STRENGTH_ELIXIR, 5, "Boosts STR by 5 for one combat");
    }

    public static Item dexterityElixir() {
        return new Item("Dexterity Elixir", ItemType.DEXTERITY_ELIXIR, 5, "Boosts DEX by 5 for one combat");
    }

    public static Item antidote() {
        return new Item("Antidote", ItemType.ANTIDOTE, 0, "Cures poison effects");
    }

    public static Item throwableAxe() {
        return new Item("Throwable Axe", ItemType.DAMAGE_ITEM, 0, "Deals STR-based damage to enemy");
    }

    public static Item manaBomb() {
        return new Item("Mana Bomb", ItemType.DAMAGE_ITEM, 0, "Deals INT-based damage to enemy");
    }

    public static Item poisonDart() {
        return new Item("Poison Dart", ItemType.DAMAGE_ITEM, 0, "Deals DEX-based damage to enemy");
    }

    public String getName() { return name; }
    public ItemType getType() { return type; }
    public int getValue() { return value; }
    public String getDescription() { return description; }
}