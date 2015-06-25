package com.mygdx.ethlab.GameObjects;

/**
 * Created by Eoin on 13/06/2015.
 */
public enum AIType {
    NONE,
    SIMPLE;

    public static String[] getNames() {
        AIType[] values = values();
        String[] names = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            names[i] = values[i].name();
        }
        return names;
    }
}
