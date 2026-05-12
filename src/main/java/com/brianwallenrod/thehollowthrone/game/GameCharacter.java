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

    public GameCharacter() {}

    public GameCharacter(String name, CharacterClass characterClass) {
        this.name = name;
        this.characterClass = characterClass;
        this.level = 1;
        this.xp = 0;
        applyClassStats();
    }

    private void applyClassStats() {
        switch (characterClass) {
            case WARRIOR -> { maxHp = 120; strength = 10; dexterity = 5; intelligence = 3; }
            case MAGE ->    { maxHp = 70;  strength = 3;  dexterity = 5; intelligence = 12; }
            case ROGUE ->   { maxHp = 90;  strength = 6;  dexterity = 12; intelligence = 5; }
        }
        this.hp = maxHp;
    }

    // Getters
    public String getName() { return name; }
    public CharacterClass getCharacterClass() { return characterClass; }
    public int getLevel() { return level; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getStrength() { return strength; }
    public int getDexterity() { return dexterity; }
    public int getIntelligence() { return intelligence; }
    public int getXp() { return xp; }
}