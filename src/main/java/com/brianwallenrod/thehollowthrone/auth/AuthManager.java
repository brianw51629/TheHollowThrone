package com.brianwallenrod.thehollowthrone.auth;

import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;
import java.io.*;
import java.nio.file.*;

public class AuthManager {

    private static final String USERS_DIR = System.getProperty("user.home") + "/TheHollowThrone/users/";
    private static final Gson gson = new Gson();

    public static boolean register(String username, String password) {
        Path userFile = Path.of(USERS_DIR + username + ".json");

        if (Files.exists(userFile)) {
            return false; // username already taken
        }

        try {
            Files.createDirectories(Path.of(USERS_DIR));
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
            User user = new User(username, hashed);
            Files.writeString(userFile, gson.toJson(user));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean login(String username, String password) {
        Path userFile = Path.of(USERS_DIR + username + ".json");

        if (!Files.exists(userFile)) {
            return false; // user doesn't exist
        }

        try {
            String json = Files.readString(userFile);
            User user = gson.fromJson(json, User.class);
            return BCrypt.checkpw(password, user.getHashedPassword());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}