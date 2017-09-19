package com.mygdx.ethlab.GameObjects;

import java.util.Arrays;

public final class utils {
    public static String[] getEnumNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }
}
