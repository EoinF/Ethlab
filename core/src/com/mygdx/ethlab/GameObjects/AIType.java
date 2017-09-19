package com.mygdx.ethlab.GameObjects;

public enum AIType {
    NONE(0),
    SIMPLE(1);

    private final int value;

    AIType(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
