package com.brianwallenrod.thehollowthrone.combat;

import com.brianwallenrod.thehollowthrone.game.GameCharacter;
import java.util.Random;
import com.brianwallenrod.thehollowthrone.game.Item;

public class CombatEngine {

    private static final Random rand = new Random();

    public enum CombatResult {
        PLAYER_WINS, ENEMY_WINS, FLED
    }

    // Determines who goes first based on DEX
    public static boolean playerGoesFirst(GameCharacter player, Enemy enemy) {
        int playerRoll = player.getDexterity() + rand.nextInt(10);
        int enemyRoll = enemy.getDexterity() + rand.nextInt(10);
        return playerRoll >= enemyRoll;
    }

    // Player basic attack
    public static int playerAttack(GameCharacter player) {
        int base = player.getStrength();
        int roll = rand.nextInt(6) + 1;
        return base + roll;
    }

    // Player special ability (class based)
    public static int playerSpecial(GameCharacter player) {
        int base = player.getStrength();
        return switch (player.getCharacterClass()) {
            case WARRIOR -> (int)(base * 1.8) + rand.nextInt(8); // Heavy Strike
            case MAGE    -> (int)(player.getIntelligence() * 2.0) + rand.nextInt(12); // Fireball
            case ROGUE   -> (int)(base * 1.5) + player.getDexterity() + rand.nextInt(6); // Backstab
        };
    }

    public static String getSpecialName(GameCharacter player) {
        return switch (player.getCharacterClass()) {
            case WARRIOR -> "Heavy Strike";
            case MAGE    -> "Fireball";
            case ROGUE   -> "Backstab";
        };
    }

    // Enemy attack
    public static int enemyAttack(Enemy enemy) {
        int base = enemy.getStrength();
        int roll = rand.nextInt(6) + 1;
        return base + roll;
    }

    // Flee attempt based on DEX
    public static boolean attemptFlee(GameCharacter player, Enemy enemy) {
        int playerRoll = player.getDexterity() + rand.nextInt(10);
        int enemyRoll = enemy.getDexterity() + rand.nextInt(8);
        return playerRoll > enemyRoll;
    }

    // Grant rewards after winning
    public static boolean grantRewards(GameCharacter player, Enemy enemy) {
        boolean leveledUp = player.addXp(enemy.getXpReward());
        player.setGold(player.getGold() + enemy.getGoldReward());
        return leveledUp;
    }

    public static int useItem(GameCharacter player, Enemy enemy, Item item) {
        return switch (item.getType()) {
            case HEALTH_POTION -> {
                player.heal(item.getValue());
                yield 0; // no damage dealt
            }
            case STRENGTH_ELIXIR -> {
                player.applyStrBoost(item.getValue());
                yield 0;
            }
            case DEXTERITY_ELIXIR -> {
                player.applyDexBoost(item.getValue());
                yield 0;
            }
            case ANTIDOTE -> {
                // poison coming later
                yield 0;
            }
            case DAMAGE_ITEM -> {
                int damage = switch (player.getCharacterClass()) {
                    case WARRIOR -> player.getStrength() + new java.util.Random().nextInt(10);
                    case MAGE    -> player.getIntelligence() * 2 + new java.util.Random().nextInt(8);
                    case ROGUE   -> player.getDexterity() + new java.util.Random().nextInt(12);
                };
                enemy.takeDamage(damage);
                yield damage;
            }
        };
    }
}