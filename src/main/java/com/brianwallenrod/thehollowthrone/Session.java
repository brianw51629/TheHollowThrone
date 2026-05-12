package com.brianwallenrod.thehollowthrone;

public class Session {
    private static String currentUsername;

    public static void setCurrentUser(String username) {
        currentUsername = username;
    }

    public static String getCurrentUser() {
        return currentUsername;
    }

    public static void clear() {
        currentUsername = null;
    }
}