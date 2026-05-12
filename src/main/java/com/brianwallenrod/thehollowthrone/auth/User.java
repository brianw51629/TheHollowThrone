package com.brianwallenrod.thehollowthrone.auth;

public class User {
    private String username;
    private String hashedPassword;

    // Gson needs this
    public User() {}

    public User(String username, String hashedPassword) {
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public String getUsername() { return username; }
    public String getHashedPassword() { return hashedPassword; }
}