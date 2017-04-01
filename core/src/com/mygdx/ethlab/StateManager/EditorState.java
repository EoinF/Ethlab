package com.mygdx.ethlab.StateManager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.*;
import com.mygdx.ethlab.UI.SidePanel.CreateModeTable;
import com.mygdx.ethlab.Utils;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Stack;

public final class EditorState {

    private static ModeType currentMode;
    private static ObjectType currentType;

    private static Stack<Command> completedCommands = new Stack<>();
    private static GameObject focusedObject;
    private static Config config;

    private static CreateModeTable createModeTable;
    private static Stage gameStage;

    public static void init(CreateModeTable table, Config gameConfig) {
        createModeTable = table;
        config = gameConfig;
        setType(ObjectType.ENTITY);
        setMode(ModeType.CREATE);
    }
    public static GameObject getFocusedObject() {
        return focusedObject;
    }
    private static void setFocusedObject(GameObject object) {
        focusedObject = object;
    }

    private static void clearFocusedObject() {
        focusedObject = null;
    }

    //
    // Editor State interface
    //
    public static boolean isMode(ModeType mode) {
        return mode == currentMode;
    }
    public static void setMode(ModeType newMode) {
        currentMode = newMode;

        if (newMode == ModeType.CREATE) {
            System.out.println(getDefaultGameObject());
            setFocusedObject(getDefaultGameObject());
        }
    }

    public static boolean isType(ObjectType type) {
        return type == currentType;
    }
    public static void setType(ObjectType newType) {
        currentType = newType;
    }


    static void performAction(Command command) {
        switch(command.actionType) {
            case SET_ENTITY_POSITION:
                EntityActions.setEntityPosition(createModeTable.entityEditorTable, (Vector2)command.newValue);
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




    private static GameObject getDefaultGameObject() {
        final Dictionary<ObjectType, GameObject> objectTypeMap = new Hashtable<ObjectType, GameObject>() {
            {
                put(ObjectType.ENTITY, new Entity(config.baseEntityNames[0],
                        Entity.DEFAULT_COLOUR,
                        Vector2.Zero,
                        Utils.getBoundingBoxFromTexture(config.getTexture(config.baseEntityNames[0], Entity.class)),
                        Entity.DEFAULT_MASS,
                        Entity.DEFAULT_HEALTH,
                        AIType.NONE));
                put(ObjectType.ITEM, new Item());
                put(ObjectType.PROP, new Prop());
                put(ObjectType.TERRAIN, new TerrainShape());
            }
        };

        return objectTypeMap.get(currentType);
    }
}
