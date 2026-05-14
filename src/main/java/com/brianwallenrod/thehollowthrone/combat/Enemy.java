package com.brianwallenrod.thehollowthrone.combat;

public class Enemy {

    private String name;
    private int hp;
    private int maxHp;
    private int strength;
    private int dexterity;
    private int xpReward;
    private int goldReward;
    private boolean isBoss;

    public Enemy(String name, int hp, int strength, int dexterity, int xpReward, int goldReward, boolean isBoss) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.strength = strength;
        this.dexterity = dexterity;
        this.xpReward = xpReward;
        this.goldReward = goldReward;
        this.isBoss = isBoss;
    }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public boolean isAlive() { return hp > 0; }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getStrength() { return strength; }
    public int getDexterity() { return dexterity; }
    public int getXpReward() { return xpReward; }
    public int getGoldReward() { return goldReward; }
    public boolean isBoss() { return isBoss; }
}