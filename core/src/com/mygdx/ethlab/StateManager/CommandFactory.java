package com.mygdx.ethlab.StateManager;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.ethlab.GameObjects.GameObject;

public final class CommandFactory {

    public static Command addNewObject(int id, GameObject newObject, boolean isOriginUI) {
        return new Command(null, newObject, id, ActionType.CREATE_OBJECT, isOriginUI);
    }

    public static Command setObjectPosition(int id, Vector2 newPosition, boolean isOriginUI) {
        return new Command(EditorState.getObjectById(id), newPosition, id, ActionType.SET_ENTITY_POSITION, isOriginUI);
    }

    public static class Command {
        Object previousState;
        Object newValue;
        int objectId;
        ActionType actionType;

        // If the origin of the command is from the ui, we avoid an infinite loop by not updating the UI (it's already set correctly)
        boolean isOriginUI;

        protected Command(Object previousState, Object newValue, int objectId, ActionType actionType, boolean isOriginUI) {
            this.previousState = previousState;
            this.newValue = newValue;
            this.objectId = objectId;
            this.actionType = actionType;
            this.isOriginUI = isOriginUI;
        }
    }

}