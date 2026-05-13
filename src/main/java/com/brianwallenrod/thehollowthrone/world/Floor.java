package com.brianwallenrod.thehollowthrone.world;

import java.util.Random;

public class Floor {

    private Room[][] grid;
    private int size;
    private int floorNumber;
    private int playerRow;
    private int playerCol;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.size = getSizeForFloor(floorNumber);
        this.grid = new Room[size][size];
        generateFloor();
        // Start player in center
        this.playerRow = size / 2;
        this.playerCol = size / 2;
        grid[playerRow][playerCol].setVisited(true);
        grid[playerRow][playerCol].setRevealed(true);
    }

    private int getSizeForFloor(int floor) {
        if (floor == 1) return 5;
        else if (floor <= 4) return 10;
        else if (floor <= 7) return 15;
        else if (floor <= 10) return 20;
        else return 3; // final floor
    }

    private void generateFloor() {
        Random rand = new Random();

        // Fill everything with empty first
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                grid[r][c] = new Room(Room.RoomType.EMPTY);
            }
        }

        if (floorNumber == 1) {
            placeRooms(rand, Room.RoomType.ENEMY, 2);
            placeRooms(rand, Room.RoomType.HEAL, 1);
            placeRooms(rand, Room.RoomType.EXIT, 1);
        } else if (floorNumber <= 4) {
            placeRooms(rand, Room.RoomType.ENEMY, 6);
            placeRooms(rand, Room.RoomType.ELITE, 1);
            placeRooms(rand, Room.RoomType.TREASURE, 2);
            placeRooms(rand, Room.RoomType.SHOP, 1);
            placeRooms(rand, Room.RoomType.HEAL, 1);
            placeRooms(rand, Room.RoomType.TRAP, 2);
            placeRooms(rand, Room.RoomType.EXIT, 1);
        } else if (floorNumber <= 7) {
            placeRooms(rand, Room.RoomType.ENEMY, 12);
            placeRooms(rand, Room.RoomType.ELITE, 3);
            placeRooms(rand, Room.RoomType.TREASURE, 3);
            placeRooms(rand, Room.RoomType.SHOP, 2);
            placeRooms(rand, Room.RoomType.HEAL, 1);
            placeRooms(rand, Room.RoomType.TRAP, 3);
            placeRooms(rand, Room.RoomType.BOSS, 1);
            placeRooms(rand, Room.RoomType.EXIT, 1);
        } else if (floorNumber <= 10) {
            placeRooms(rand, Room.RoomType.ENEMY, 20);
            placeRooms(rand, Room.RoomType.ELITE, 4);
            placeRooms(rand, Room.RoomType.TREASURE, 4);
            placeRooms(rand, Room.RoomType.SHOP, 2);
            placeRooms(rand, Room.RoomType.HEAL, 2);
            placeRooms(rand, Room.RoomType.TRAP, 4);
            placeRooms(rand, Room.RoomType.BOSS, 1);
            placeRooms(rand, Room.RoomType.EXIT, 1);
        } else {
            // Final floor
            placeRooms(rand, Room.RoomType.SHOP, 1);
            placeRooms(rand, Room.RoomType.BOSS_DOOR, 1);
        }
    }

    private void placeRooms(Random rand, Room.RoomType type, int count) {
        int placed = 0;
        int attempts = 0;
        while (placed < count && attempts < 1000) {
            int r = rand.nextInt(size);
            int c = rand.nextInt(size);
            // Don't place on center (starting room) or already placed rooms
            if (grid[r][c].getType() == Room.RoomType.EMPTY
                    && !(r == size / 2 && c == size / 2)) {
                grid[r][c] = new Room(type);
                placed++;
            }
            attempts++;
        }
    }

    public boolean movePlayer(int dRow, int dCol) {
        int newRow = playerRow + dRow;
        int newCol = playerCol + dCol;

        if (newRow < 0 || newRow >= size || newCol < 0 || newCol >= size) {
            return false; // out of bounds
        }

        playerRow = newRow;
        playerCol = newCol;
        grid[playerRow][playerCol].setVisited(true);

        // Reveal surrounding cells
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                int rr = playerRow + r;
                int cc = playerCol + c;
                if (rr >= 0 && rr < size && cc >= 0 && cc < size) {
                    grid[rr][cc].setRevealed(true);
                }
            }
        }
        return true;
    }

    public Room getCurrentRoom() {
        return grid[playerRow][playerCol];
    }

    public Room[][] getGrid() { return grid; }
    public int getSize() { return size; }
    public int getFloorNumber() { return floorNumber; }
    public int getPlayerRow() { return playerRow; }
    public int getPlayerCol() { return playerCol; }
}