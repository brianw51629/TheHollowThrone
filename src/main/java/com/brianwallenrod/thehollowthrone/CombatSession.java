package com.brianwallenrod.thehollowthrone;

import com.brianwallenrod.thehollowthrone.combat.Enemy;

public class CombatSession {
    private static Enemy currentEnemy;
    private static boolean playerTurn = true;
    private static boolean returningFromInventory = false;

    public static void start(Enemy enemy) {
        currentEnemy = enemy;
        playerTurn = true;
        returningFromInventory = false;
    }

    public static void setPlayerTurn(boolean turn) { playerTurn = turn; }
    public static boolean isPlayerTurn() { return playerTurn; }

    public static void setReturningFromInventory(boolean returning) {
        returningFromInventory = returning;
    }
    public static boolean isReturningFromInventory() { return returningFromInventory; }

    public static Enemy getEnemy() { return currentEnemy; }

    public static void clear() {
        currentEnemy = null;
        playerTurn = true;
        returningFromInventory = false;
    }
}