package com.brianwallenrod.thehollowthrone.world;

public class Room {

    public enum RoomType {
        EMPTY, ENEMY, ELITE, TREASURE, SHOP, TRAP, HEAL, EXIT, BOSS, BOSS_DOOR
    }

    private RoomType type;
    private boolean visited;
    private boolean revealed;
    private boolean consumed;

    public Room() {
        this.type = RoomType.EMPTY;
        this.visited = false;
        this.revealed = false;
    }

    public Room(RoomType type) {
        this.type = type;
        this.visited = false;
        this.revealed = false;
        this.consumed = false;
    }

    public RoomType getType() { return type; }
    public boolean isVisited() { return visited; }
    public boolean isRevealed() { return revealed; }
    public void setVisited(boolean visited) { this.visited = visited; }
    public void setRevealed(boolean revealed) { this.revealed = revealed; }
    public boolean isConsumed() { return consumed; }
    public void setConsumed(boolean consumed) { this.consumed = consumed; }
}