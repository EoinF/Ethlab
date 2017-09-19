package com.mygdx.ethlab.StateManager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.EditorMap;
import com.mygdx.ethlab.GameObjects.Entity;
import com.mygdx.ethlab.GameObjects.GameObject;
import com.mygdx.ethlab.UI.EditorObject;
import com.mygdx.ethlab.UI.MainView.MainView;
import com.mygdx.ethlab.UI.SidePanel.SidePanel;

public class Actions {

    public static void setObjectPosition(SidePanel sidePanel, EditorMap map, int id, Vector2 position,
                                         boolean isOriginUI) {
        EditorObject wrapper = map.getObjectById(id);
        GameObject gameObject = wrapper.instance;

        wrapper.setPosition(position);

        EditorState.updateObject(wrapper);
        if (!isOriginUI && EditorState.isFocused(wrapper.getId())) {
            sidePanel.getCreateModeTable().setObjectPosition(gameObject.getPosition());
        }
    }

    public static void setObjectColour(SidePanel sidePanel, EditorMap map, int id, Color newColour,
                                       boolean isOriginUI) {
        EditorObject wrapper = map.getObjectById(id);
        GameObject gameObject = wrapper.instance;

        wrapper.setColour(newColour);

        EditorState.updateObject(wrapper);
        if (!isOriginUI && EditorState.isFocused(wrapper.getId())) {
            sidePanel.getCreateModeTable().setObjectColour(gameObject.colour);
        }
    }

    public static void setObjectTexture(SidePanel sidePanel, EditorMap map, int id, String textureName, Config config,
                                        boolean isOriginUI) {
        EditorObject wrapper = map.getObjectById(id);
        GameObject gameObject = wrapper.instance;

        wrapper.setTexture(textureName, config);

        EditorState.updateObject(wrapper);

        if (!isOriginUI && EditorState.isFocused(wrapper.getId())) {
            sidePanel.getCreateModeTable().setObjectTexture(gameObject.textureName);
        }
    }

    public static void createObject(SidePanel sidePanel, MainView mainView, EditorMap map, EditorObject wrapper) {
        map.addObject(wrapper);
        mainView.addGameObject(wrapper);
        sidePanel.getCreateModeTable().setObject(wrapper);
    }
    public static void removeObject(MainView mainView, EditorMap map, EditorObject wrapper, boolean isOriginUI) {
        map.removeObject(wrapper);
        mainView.removeGameObject(wrapper);
        if (EditorState.isFocused(wrapper.getId())) {
            EditorState.setFocusedObject(null, isOriginUI);
        }
    }
}
