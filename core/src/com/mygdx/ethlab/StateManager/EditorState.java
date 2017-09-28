package com.mygdx.ethlab.StateManager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.ethanolshared.Config;
import com.mygdx.ethlab.EditorMap;
import com.github.eoinf.ethanolshared.GameObjects.*;
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

    public static void setFocusedObject(EditorObject editorObject, boolean isOriginUI) {
        focusedObject = editorObject;
        mainView.setFocusedObject(editorObject);

        if (!isOriginUI) {
            switch (currentMode) {
                case CREATE:
                    sidePanel.getCreateModeTable().setObject(editorObject);
                    break;
                case EDIT:
                    sidePanel.getEditModeTable().selectObject(editorObject);
                    break;
            }
        }
    }

    public static void setFocusedObject(int id, boolean isOriginUI) {
        setFocusedObject(getObjectById(id), isOriginUI);
    }

    public static boolean isFocused(int id) {
        return focusedObject.getId() == id;
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
                put(ObjectType.ITEM, new EditorObject<>(new Item(ItemType.PLAYER_SPAWN,
                        gameConfig.baseItemNames[0],
                        Item.DEFAULT_COLOUR,
                        Vector2.Zero), true, gameConfig));
                put(ObjectType.PROP, new EditorObject<>(new Prop(), true, gameConfig));
                put(ObjectType.TERRAIN, new EditorObject<>(new TerrainShape(gameConfig.getDefaultTerrainTexture()), true, gameConfig));
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

    public static void updateObject(EditorObject wrapper) {
        if (focusedObject != null &&
                focusedObject.getId() == wrapper.getId()) {
            setFocusedObject(wrapper, false);
        }
        map.updateEntity(wrapper);
        mainView.updateGameObject(wrapper);
    }

    public static void addShapeVertex(EditorObject wrapper) {
        sidePanel.getCreateModeTable().setObject(wrapper);
    }

    public static void cancelShapeCreation() {
        EditorObject wrapper = toolbarObjects.get(ObjectType.TERRAIN);
        ((TerrainShape)wrapper.instance).setPoints(null);
        sidePanel.getCreateModeTable().setObject(wrapper);
    }

    public static EditorObject getObjectById(int id) {
        if (focusedObject != null
                && id == focusedObject.getId()) {
            return focusedObject;
        } else {
            return map.getObjectById(id);
        }
    }

    public static EditorObject incrementFocusedObject(Config config) {
        toolbarObjects.put(currentType, new EditorObject(toolbarObjects.get(currentType), config));
        focusedObject = toolbarObjects.get(currentType);
        return focusedObject;
    }
    //
    // Editor State interface
    //
    public static boolean isMode(ModeType mode) {
        return mode == currentMode;
    }

    public static void setMode(ModeType newMode) {
        if (currentMode != newMode) {
            if (currentMode == ModeType.ADD_VERTICES) {
                cancelShapeCreation();
            }
            currentMode = newMode;

            switch (newMode) {
                case CREATE:
                    setFocusedObject(toolbarObjects.get(currentType), false);
                    break;
                case ADD_VERTICES:
                    break;
                case EDIT:
                    setFocusedObject(null, false);
                    break;
                case TRIGGERS:
                    break;
            }
        }
    }

    public static boolean isType(ObjectType type) {
        return currentType == type;
    }
    public static ObjectType getType() {
        return currentType;
    }
    public static void setType(ObjectType newType) {
        if (currentType != newType) {
            currentType = newType;

            if (currentMode == ModeType.CREATE) {
                setFocusedObject(toolbarObjects.get(currentType), false);
            } else if (currentMode == ModeType.ADD_VERTICES) {
                cancelShapeCreation();
                setMode(ModeType.CREATE);
            }
        }
    }


    public static void performAction(Command command, Config gameConfig) {
        switch(command.actionType) {
            case SET_OBJECT_POSITION:
                Actions.setObjectPosition(
                        sidePanel,
                        map,
                        command.objectId,
                        (Vector2)command.newValue,
                        command.isOriginUI);
                break;
            case SET_OBJECT_COLOUR:
                Actions.setObjectColour(
                        sidePanel,
                        map,
                        command.objectId,
                        (Color)command.newValue,
                        command.isOriginUI);
                break;
            case SET_OBJECT_TEXTURE:
                Actions.setObjectTexture(
                        sidePanel,
                        map,
                        command.objectId,
                        (String)command.newValue,
                        gameConfig,
                        command.isOriginUI);
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
                        (EditorObject) command.previousState,
                        command.isOriginUI);
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
