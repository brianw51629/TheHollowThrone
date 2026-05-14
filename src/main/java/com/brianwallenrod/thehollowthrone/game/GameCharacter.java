package com.brianwallenrod.thehollowthrone.game;

public class GameCharacter {
    private String name;
    private CharacterClass characterClass;
    private int level;
    private int hp;
    private int maxHp;
    private int strength;
    private int dexterity;
    private int intelligence;
    private int xp;
    private int gold;
    private int lives;
    private Inventory inventory;
    private int tempStrBonus;
    private int tempDexBonus;

    public GameCharacter() {
        this.inventory = new Inventory();
        this.tempStrBonus = 0;
        this.tempDexBonus = 0;
    }

    public GameCharacter(String name, CharacterClass characterClass) {
        this.name = name;
        this.characterClass = characterClass;
        this.level = 1;
        this.xp = 0;
        this.gold = 0;
        this.lives = 3;
        this.tempStrBonus = 0;
        this.tempDexBonus = 0;
        this.inventory = new Inventory();
        applyClassStats();
    }
    public boolean addXp(int amount) {
        this.xp += amount;
        return checkLevelUp();
    }

    private boolean checkLevelUp() {
        int xpNeeded = level * 100;
        if (xp >= xpNeeded) {
            xp -= xpNeeded;
            level++;
            maxHp += 20;
            hp = maxHp;
            strength += 2;
            dexterity += 2;
            intelligence += 2;
            return true;
        }
        return false;
    }

    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }
    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public boolean isAlive() {
        return hp > 0;
    }

    private void applyClassStats() {
        switch (characterClass) {
            case WARRIOR -> { maxHp = 150; strength = 12; dexterity = 6; intelligence = 3; }
            case MAGE ->    { maxHp = 90;  strength = 4;  dexterity = 6; intelligence = 14; }
            case ROGUE ->   { maxHp = 110; strength = 7;  dexterity = 14; intelligence = 5; }
        }
        this.hp = maxHp;
    }

    // Getters
    public String getName() { return name; }
    public CharacterClass getCharacterClass() { return characterClass; }
    public int getLevel() { return level; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getIntelligence() { return intelligence; }
    public int getXp() { return xp; }
    public int getLives() { return lives; }
    public void setLives(int lives) { this.lives = lives; }

    public void applyDeathPenalty() {
        lives--;
        gold = (int)(gold * 0.75);   // lose 25% gold
        xp = (int)(xp * 0.75);       // lose 25% XP
        hp = maxHp;                   // restore HP
    }

    public void resetToFloorOne() {
        lives = 3;
        gold = 0;
        xp = 0;
        level = 1;
        tempStrBonus = 0;
        tempDexBonus = 0;
        hp = maxHp;
        applyClassStats();
    }

    public Inventory getInventory() { return inventory; }

    public int getStrength() { return strength + tempStrBonus; }
    public int getDexterity() { return dexterity + tempDexBonus; }

    public void applyStrBoost(int amount) { tempStrBonus += amount; }
    public void applyDexBoost(int amount) { tempDexBonus += amount; }

    public void clearTempBoosts() {
        tempStrBonus = 0;
        tempDexBonus = 0;
    }

    public String getDamageItemName() {
        return switch (characterClass) {
            case WARRIOR -> "Throwable Axe";
            case MAGE    -> "Mana Bomb";
            case ROGUE   -> "Poison Dart";
        };
    }

    public Item getDamageItem() {
        return switch (characterClass) {
            case WARRIOR -> Item.throwableAxe();
            case MAGE    -> Item.manaBomb();
            case ROGUE   -> Item.poisonDart();
        };
    }
}