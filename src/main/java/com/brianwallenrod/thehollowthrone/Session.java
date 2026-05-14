package com.brianwallenrod.thehollowthrone;

import com.brianwallenrod.thehollowthrone.world.WorldMap;

public class Session {
    private static String currentUsername;
    private static WorldMap worldMap;

    public static void setCurrentUser(String username) {
        currentUsername = username;
    }

    public static String getCurrentUser() {
        return currentUsername;
    }

    public static void setWorldMap(WorldMap map) {
        worldMap = map;
    }

    public static WorldMap getWorldMap() {
        return worldMap;
    }

    public static void clear() {
        currentUsername = null;
        worldMap = null;
    }
}