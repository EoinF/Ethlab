package com.mygdx.ethlab.GameObjects;

public class Item extends GameObject {

    public ItemType type;

    public Item(ItemType type) {
        this.type = type;
    }

    // Default constructor for json deserialization
    public Item() {
        this.type = ItemType.INVALID;
    }
}
