package com.mygdx.ethlab.GameObjects;

public enum ItemType {
    PLAYER_SPAWN(0),
    CHECKPOINT(1),

    // Only used when no item type is provided when deserializing
    INVALID(-1);


    private final int value;

    ItemType(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
