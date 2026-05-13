package com.brianwallenrod.thehollowthrone.world;

public class WorldMap {

    private Floor currentFloor;
    private int currentFloorNumber;

    public WorldMap() {
        this.currentFloorNumber = 1;
        this.currentFloor = new Floor(currentFloorNumber);
    }

    public void descend() {
        currentFloorNumber++;
        currentFloor = new Floor(currentFloorNumber);
    }

    public Floor getCurrentFloor() { return currentFloor; }
    public int getCurrentFloorNumber() { return currentFloorNumber; }
    public boolean isFinalFloor() { return currentFloorNumber == 11; }
}