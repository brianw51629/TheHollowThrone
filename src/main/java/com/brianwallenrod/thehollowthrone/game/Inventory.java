package com.brianwallenrod.thehollowthrone.game;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private static final int MAX_SIZE = 10;
    private List<Item> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public boolean addItem(Item item) {
        if (items.size() >= MAX_SIZE) {
            return false; // inventory full
        }
        items.add(item);
        return true;
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public Item getItem(int index) {
        if (index < 0 || index >= items.size()) return null;
        return items.get(index);
    }

    public List<Item> getItems() { return items; }
    public int getSize() { return items.size(); }
    public boolean isFull() { return items.size() >= MAX_SIZE; }
    public boolean isEmpty() { return items.isEmpty(); }
}