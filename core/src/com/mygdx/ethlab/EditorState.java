package com.mygdx.ethlab;

import java.util.function.Consumer;

public final class EditorState {

    private static ModeType currentMode = ModeType.CREATE;
    private static ObjectType currentType = ObjectType.ENTITY;

    // Handlers to be called when editor state is updated
    private static Consumer<ModeType> onSetMode = newMode -> { /*Do nothing*/ };
    private static Consumer<ObjectType> onSetType = newType -> { /*Do nothing*/ };

    //
    // Editor State hooks
    //
    public void addSetModeHandler(Consumer<ModeType> handler) {
        onSetMode.andThen(handler);
    }

    public void addSetTypeHandler(Consumer<ObjectType> handler) {
        onSetType.andThen(handler);
    }

    //
    // Editor State interface
    //
    public static ModeType getMode() {
        return currentMode;
    }
    public static void setMode(ModeType newMode) {
        currentMode = newMode;
        onSetMode.accept(newMode);
    }

    public static ObjectType getType() {
        return currentType;
    }
    public static void setType(ObjectType newType) {
        currentType = newType;
        onSetType.accept(newType);
    }
}
