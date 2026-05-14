package com.brianwallenrod.thehollowthrone;

import com.brianwallenrod.thehollowthrone.combat.Enemy;

public class CombatSession {
    private static Enemy currentEnemy;

    public static void start(Enemy enemy) {
        currentEnemy = enemy;
    }

    public static Enemy getEnemy() { return currentEnemy; }

    public static void clear() {
        currentEnemy = null;
    }
}