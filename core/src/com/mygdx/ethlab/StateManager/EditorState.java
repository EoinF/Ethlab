package com.mygdx.ethlab.StateManager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.EditorMap;
import com.mygdx.ethlab.GameObjects.*;
import com.mygdx.ethlab.StateManager.enums.ModeType;
import com.mygdx.ethlab.StateManager.enums.ObjectType;
import com.mygdx.ethlab.UI.EditorObject;
import com.mygdx.ethlab.UI.MainView.MainView;
import com.mygdx.ethlab.UI.SidePanel.SidePanel;
import com.mygdx.ethlab.StateManager.CommandFactory.Command;

import java.util.*;

public final class EditorState {

    private static ModeType currentMode;
    private static ObjectType currentType;

    private static Stack<Command> completedCommands = new Stack<>();
    private static EditorObject focusedObject;
    // Record the state of the toolbar objects so when we
    // switch between types we keep the attributes we selected earlier
    private static Map<ObjectType, EditorObject> toolbarObjects;

    public static EditorObject getFocusedObject() {
        return focusedObject;
    }

    public static void setFocusedObject(EditorObject editorObject) {
        focusedObject = editorObject;
        mainView.setFocusedObject(editorObject);
    }

    public static void setFocusedObject(int id) {
        focusedObject = getObjectById(id);
        mainView.setFocusedObject(focusedObject);
    }

    /*
        The three representations of the current map's state.
        Only 'map' is the source of truth. The others are just for rendering
     */
    private static SidePanel sidePanel;
    private static MainView mainView;
    private static EditorMap map;

    public static EditorMap getMap() {
        return map;
    }

    public static void setMap(EditorMap _map) {
        map = _map;

        mainView.reset();
        mainView.setEntities(map.entities);
        mainView.setItems(map.items);
        mainView.setShapes(map.shapes);
    }

    public static void init(SidePanel _sidePanel, MainView _mainView, EditorMap _map, Config gameConfig) {
        sidePanel = _sidePanel;
        mainView = _mainView;

        setMap(_map);

        toolbarObjects = new Hashtable<ObjectType, EditorObject>() {
            {
                put(ObjectType.ENTITY, new EditorObject<>(new Entity(gameConfig.baseEntityNames[0],
                        Entity.DEFAULT_COLOUR,
                        Vector2.Zero,
                        new Rectangle(0, 0, 0, 0),
                        Entity.DEFAULT_MASS,
                        Entity.DEFAULT_HEALTH,
                        AIType.NONE), true, gameConfig));
                put(ObjectType.ITEM, new EditorObject<>(new Item(), true, gameConfig));
                put(ObjectType.PROP, new EditorObject<>(new Prop(), true, gameConfig));
                put(ObjectType.TERRAIN, new EditorObject<>(new TerrainShape(), true, gameConfig));
            }
        };

        for (EditorObject e: toolbarObjects.values()) {
            sidePanel.getCreateModeTable().setObject(e);
        }

        setType(ObjectType.ENTITY);
        setMode(ModeType.CREATE);
    }

    private static int idGenerator = 0;
    public static int getNextIdAndIncrement() {
        return idGenerator++;
    }

    public static EditorObject getObjectById(int id) {
        if (focusedObject != null
                && id == focusedObject.getId()) {
            return focusedObject;
        } else {
            return map.getObjectById(id);
        }
    }

    public static EditorObject incrementFocusedObject() {
        toolbarObjects.put(currentType, new EditorObject<>(toolbarObjects.get(currentType).instance.copy()));
        focusedObject = toolbarObjects.get(currentType);
        return focusedObject;
    }

    public static void setEditModeObject(int id) {
        EditorObject wrapper = getObjectById(id);
        sidePanel.getEditModeTable().selectObject(wrapper);
    }

    public static void setEditModeObject(EditorObject wrapper) {
        sidePanel.getEditModeTable().selectObject(wrapper);
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
            setFocusedObject(toolbarObjects.get(currentType));
        }
        else if (newMode == ModeType.EDIT) {
            setFocusedObject(null);
        }
    }

    public static ObjectType getType() {
        return currentType;
    }
    public static void setType(ObjectType newType) {
        currentType = newType;
        focusedObject = toolbarObjects.get(currentType);
    }


    public static void performAction(Command command, Config gameConfig) {
        switch(command.actionType) {
            case SET_OBJECT_POSITION:
                Actions.setObjectPosition(
                        sidePanel,
                        mainView,
                        map,
                        command.objectId,
                        (Vector2)command.newValue);
                break;
            case SET_OBJECT_COLOUR:
                Actions.setObjectColour(
                        sidePanel,
                        mainView,
                        map,
                        command.objectId,
                        (Color)command.newValue);
                break;
            case SET_OBJECT_TEXTURE:
                Actions.setObjectTexture(
                        sidePanel,
                        mainView,
                        map,
                        command.objectId,
                        (String)command.newValue,
                        gameConfig);
                break;
            case CREATE_OBJECT:
                Actions.createObject(
                        sidePanel,
                        mainView,
                        map,
                        (EditorObject) command.newValue);
                break;
            case REMOVE_OBJECT:
                Actions.removeObject(
                        mainView,
                        map,
                        (EditorObject) command.previousState);
                break;
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
}
