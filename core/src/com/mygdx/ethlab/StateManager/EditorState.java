package com.mygdx.ethlab.StateManager;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.EditorMap;
import com.mygdx.ethlab.GameObjects.*;
import com.mygdx.ethlab.UI.EditorObject;
import com.mygdx.ethlab.UI.MainView.MainView;
import com.mygdx.ethlab.UI.SidePanel.SidePanel;
import com.mygdx.ethlab.Utils;
import com.mygdx.ethlab.StateManager.CommandFactory.Command;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Stack;

public final class EditorState {

    private static ModeType currentMode;
    private static ObjectType currentType;

    private static Stack<Command> completedCommands = new Stack<>();
    private static EditorObject focusedObject;
    private static Config config;

    private static void setFocusedObject(EditorObject gameObject) {
        focusedObject = gameObject;
        mainView.setFocusedObject(gameObject);
    }

    /*
        The three representations of the current map's state.
        Only 'map' is the source of truth. The others are just for rendering
     */
    private static SidePanel sidePanel;
    private static MainView mainView;
    private static EditorMap map;

    public static void init(SidePanel _sidePanel, MainView _mainView, EditorMap _map, Config gameConfig) {
        sidePanel = _sidePanel;
        mainView = _mainView;
        map = _map;

        mainView.setEntities(map.entities);
        mainView.setItems(map.items);
        mainView.setShapes(map.shapes);

        config = gameConfig;
        setType(ObjectType.ENTITY);
        setMode(ModeType.CREATE);
    }

    private static int idGenerator = 0;
    public static int getNextIdAndIncrement() {
        return idGenerator++;
    }
    public static int peekNextId() {
        return idGenerator;
    }

    public static EditorObject getObjectById(int id) {
        if (id == focusedObject.getId()) {
            return focusedObject;
        } else {
            return map.getObjectById(id);
        }
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
            setFocusedObject(getDefaultGameObject());
        }
    }

    public static boolean isType(ObjectType type) {
        return type == currentType;
    }
    public static void setType(ObjectType newType) {
        currentType = newType;
    }


    public static void performAction(Command command) {
        switch(command.actionType) {
            case SET_ENTITY_POSITION:
                EntityActions.setEntityPosition(
                        sidePanel,
                        mainView,
                        map,
                        command.objectId,
                        (Vector2)command.newValue);
            case CREATE_ENTITY:
                EntityActions.createEntity(
                        sidePanel,
                        mainView,
                        map,
                        command.objectId,
                        (EditorObject) command.newValue);
        }

        // Save the commands that originate from the UI
        // Every other command will trigger a change in the UI (because we use event listeners)
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


    private static EditorObject getDefaultGameObject() {
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

        return new EditorObject(objectTypeMap.get(currentType));
    }
}
