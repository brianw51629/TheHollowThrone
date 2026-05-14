package com.brianwallenrod.thehollowthrone.combat;

import com.brianwallenrod.thehollowthrone.game.CharacterClass;
import java.util.Random;

public class EnemyPool {

    private static final Random rand = new Random();

    public static Enemy getEnemy(CharacterClass characterClass, int floor, boolean isBoss) {
        if (isBoss) return getBoss(characterClass, floor);
        return getRegularEnemy(characterClass, floor);
    }

    private static Enemy getRegularEnemy(CharacterClass characterClass, int floor) {
        return switch (characterClass) {
            case WARRIOR -> getWarriorEnemy(floor);
            case MAGE -> getMageEnemy(floor);
            case ROGUE -> getRogueEnemy(floor);
        };
    }

    // ══════════════════════════ WARRIOR ENEMIES ══════════════════════════
    private static Enemy getWarriorEnemy(int floor) {
        String[][] enemies = {
                {"Deserter", "Militia Grunt"},           // floor 1
                {"Corrupted Soldier", "Shield Bearer"},   // floor 2
                {"Royal Knight", "War Hound"},            // floor 3
                {"Siege Veteran", "Cursed Paladin"},      // floor 4
                {"Blackguard", "Fallen Champion"},        // floor 5
                {"Death Knight", "War Golem"},            // floor 6
                {"Cursed Crusader", "Blood Knight"},      // floor 7
                {"Warlord's Guard", "Siege Beast"},       // floor 8
                {"Elite Royal Guard", "Fallen Hero"},     // floor 9
                {"Undead Warlord", "Soul Reaver"},        // floor 10
        };
        int index = Math.min(floor - 1, enemies.length - 1);
        String name = enemies[index][rand.nextInt(2)];
        return scaleEnemy(name, floor, false);
    }

    // ══════════════════════════ MAGE ENEMIES ══════════════════════════
    private static Enemy getMageEnemy(int floor) {
        String[][] enemies = {
                {"Apprentice", "Mana Wisp"},
                {"Rogue Elementalist", "Arcane Golem"},
                {"Storm Caller", "Void Touched"},
                {"Chaos Mage", "Crystal Golem"},
                {"Void Stalker", "Rune Wraith"},
                {"Arcane Lich", "Elemental Titan"},
                {"Entropy Mage", "Soul Shard"},
                {"Void Colossus", "Spell Eater"},
                {"Reality Breaker", "Chaos Titan"},
                {"Arcane Destroyer", "Void God"},
        };
        int index = Math.min(floor - 1, enemies.length - 1);
        String name = enemies[index][rand.nextInt(2)];
        return scaleEnemy(name, floor, false);
    }

    // ══════════════════════════ ROGUE ENEMIES ══════════════════════════
    private static Enemy getRogueEnemy(int floor) {
        String[][] enemies = {
                {"Street Thug", "Pickpocket"},
                {"Syndicate Scout", "Knife Juggler"},
                {"Shadow Stalker", "Poison Dealer"},
                {"Syndicate Enforcer", "Hex Blade"},
                {"Phantom Blade", "Night Crawler"},
                {"Shadow Assassin", "Death Dealer"},
                {"Soul Thief", "Shade Walker"},
                {"Ghost Blade", "Eclipse Hunter"},
                {"Void Stalker", "Shadow Titan"},
                {"Syndicate Elite", "Darkness Avatar"},
        };
        int index = Math.min(floor - 1, enemies.length - 1);
        String name = enemies[index][rand.nextInt(2)];
        return scaleEnemy(name, floor, false);
    }

    // ══════════════════════════ BOSSES ══════════════════════════
    private static Enemy getBoss(CharacterClass characterClass, int floor) {
        String name = switch (characterClass) {
            case WARRIOR -> switch (floor) {
                case 2 -> "General Malgrath";
                case 4 -> "The Iron Warden";
                case 6 -> "Commander Valdris";
                case 8 -> "The Betrayer King";
                case 10 -> "Lord Malachar";
                case 11 -> "The Hollow King";
                default -> "Unknown Boss";
            };
            case MAGE -> switch (floor) {
                case 2 -> "Archon Vex";
                case 4 -> "The Shattered Mind";
                case 6 -> "Magister Zorah";
                case 8 -> "The Unbound";
                case 10 -> "Nexus Prime";
                case 11 -> "Shadow Mage";
                default -> "Unknown Boss";
            };
            case ROGUE -> switch (floor) {
                case 2 -> "The Broker";
                case 4 -> "Lady Venom";
                case 6 -> "The Phantom";
                case 8 -> "The Unseen Hand";
                case 10 -> "Master Shade";
                case 11 -> "Lord Assassin";
                default -> "Unknown Boss";
            };
        };
        return scaleEnemy(name, floor, true);
    }

    // ══════════════════════════ STAT SCALING ══════════════════════════
    private static Enemy scaleEnemy(String name, int floor, boolean isBoss) {
        int baseHp = 30 + (floor * 15);
        int baseStr = 5 + (floor * 3);
        int baseDex = 3 + (floor * 2);
        int baseXp = 20 + (floor * 10);
        int baseGold = 5 + (floor * 5);

        if (isBoss) {
            baseHp *= 2;
            baseStr = (int)(baseStr * 1.5);
            baseDex = (int)(baseDex * 1.5);
            baseXp *= 3;
            baseGold *= 3;
        }

        return new Enemy(name, baseHp, baseStr, baseDex, baseXp, baseGold, isBoss);
    }
}