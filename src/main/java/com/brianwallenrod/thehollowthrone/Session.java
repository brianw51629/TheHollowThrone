package com.brianwallenrod.thehollowthrone;

import com.brianwallenrod.thehollowthrone.game.GameCharacter;
import com.brianwallenrod.thehollowthrone.world.WorldMap;

public class Session {
    private static String currentUsername;
    private static WorldMap worldMap;
    private static GameCharacter character;

    public static void setCurrentUser(String username) { currentUsername = username; }
    public static String getCurrentUser() { return currentUsername; }

    public static void setWorldMap(WorldMap map) { worldMap = map; }
    public static WorldMap getWorldMap() { return worldMap; }

    public static void setCharacter(GameCharacter c) { character = c; }
    public static GameCharacter getCharacter() { return character; }

    public static void clear() {
        currentUsername = null;
        worldMap = null;
        character = null;
    }
}