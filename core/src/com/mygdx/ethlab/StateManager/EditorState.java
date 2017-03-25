package com.mygdx.ethlab.StateManager;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.ethlab.GameObjects.GameObject;
import com.mygdx.ethlab.ModeType;
import com.mygdx.ethlab.ObjectType;
import com.mygdx.ethlab.UI.EntityEditorTable;
import com.mygdx.ethlab.UI.ObjectEditorTable;

import java.util.Stack;
import java.util.function.Consumer;

public final class EditorState {

    private static ModeType currentMode = ModeType.CREATE;
    private static ObjectType currentType = ObjectType.ENTITY;

    private static Stack<Command> completedCommands = new Stack<>();

    // Handlers to be called when editor state is updated
    private static Consumer<ModeType> onSetMode = newMode -> { /*Do nothing*/ };
    private static Consumer<ObjectType> onSetType = newType -> { /*Do nothing*/ };

    private static ObjectEditorTable editorTable;
    private static GameObject currentObject;

    public void setEditorTable(ObjectEditorTable table) {
        editorTable = table;
    }
    public void setCurrentObject(GameObject gameObject) {
        currentObject = gameObject;
    }

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


    static void performAction(Command command) {
        switch(command.actionType) {
            case SET_ENTITY_POSITION:
                EntityActions.setEntityPosition((EntityEditorTable)editorTable, (Vector2)command.newValue);
        }

        // Save the commands that originate from the UI
        // Every other command will trigger a change in the UI
        // and result in a duplicate command being triggered originating from the UI
        // so we only want to ever store it once
        if (command.isOriginUI) {
            command.isOriginUI = false;
            completedCommands.push(command);
        }
    }

    static void undoAction(Command command) {

    }

    static void undoLast() {
        undoAction(completedCommands.pop());
    }


}
