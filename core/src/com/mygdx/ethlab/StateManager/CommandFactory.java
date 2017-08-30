package com.mygdx.ethlab.StateManager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.ethlab.StateManager.enums.ActionType;
import com.mygdx.ethlab.UI.EditorObject;

public final class CommandFactory {

    public static Command addNewObject(int id, EditorObject newObject, boolean isOriginUI) {
        return new Command(null, newObject, id, ActionType.CREATE_OBJECT, isOriginUI);
    }

    public static Command setObjectPosition(int id, Vector2 newPosition, boolean isOriginUI) {
        return new Command(EditorState.getObjectById(id), newPosition, id, ActionType.SET_OBJECT_POSITION, isOriginUI);
    }

    public static Command setObjectColour(int id, Color newColour, boolean isOriginUI) {
        return new Command(EditorState.getObjectById(id), newColour, id, ActionType.SET_OBJECT_COLOUR, isOriginUI);
    }

    public static Command setObjectTexture(int id, String newTexture, boolean isOriginUI) {
        return new Command(EditorState.getObjectById(id), newTexture, id, ActionType.SET_OBJECT_TEXTURE, isOriginUI);
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
