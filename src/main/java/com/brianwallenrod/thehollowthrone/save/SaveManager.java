package com.brianwallenrod.thehollowthrone.save;

import com.brianwallenrod.thehollowthrone.game.GameCharacter;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.*;

public class SaveManager {

    private static final String SAVES_DIR = "data/saves/";
    private static final Gson gson = new Gson();

    public static void saveCharacter(String username, GameCharacter character) {
        try {
            Files.createDirectories(Path.of(SAVES_DIR));
            String json = gson.toJson(character);
            Files.writeString(Path.of(SAVES_DIR + username + ".json"), json);
            System.out.println("Character saved for: " + username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameCharacter loadCharacter(String username) {
        Path savePath = Path.of(SAVES_DIR + username + ".json");
        if (!Files.exists(savePath)) return null;

        try {
            String json = Files.readString(savePath);
            return gson.fromJson(json, GameCharacter.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void deleteSave(String username) {
        try {
            Files.deleteIfExists(Path.of(SAVES_DIR + username + ".json"));
            System.out.println("Save deleted for: " + username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasSave(String username) {
        return Files.exists(Path.of(SAVES_DIR + username + ".json"));
    }
}