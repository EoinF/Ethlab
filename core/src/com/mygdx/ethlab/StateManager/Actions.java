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
        if (!isOriginUI) {
            sidePanel.getCreateModeTable().setObjectPosition(gameObject.position);
        }
    }

    public static void setObjectColour(SidePanel sidePanel, EditorMap map, int id, Color newColour,
                                       boolean isOriginUI) {
        EditorObject wrapper = map.getObjectById(id);
        GameObject gameObject = wrapper.instance;

        wrapper.setColour(newColour);

        EditorState.updateObject(wrapper);
        if (!isOriginUI) {
            sidePanel.getCreateModeTable().setObjectColour(gameObject.colour);
        }
    }

    public static void setObjectTexture(SidePanel sidePanel, EditorMap map, int id, String textureName, Config config,
                                        boolean isOriginUI) {
        EditorObject wrapper = map.getObjectById(id);
        GameObject gameObject = wrapper.instance;

        wrapper.setTexture(textureName, config);

        EditorState.updateObject(wrapper);

        if (!isOriginUI) {
            sidePanel.getCreateModeTable().setObjectTexture(gameObject.textureName);
        }
    }

    public static void createObject(SidePanel sidePanel, MainView mainView, EditorMap map, EditorObject entity) {
        map.addEntity(entity);
        mainView.addGameObject(entity);
        sidePanel.getCreateModeTable().setObjectPosition(entity.instance.position);
    }
    public static void removeObject(MainView mainView, EditorMap map, EditorObject entity) {
        map.removeEntity(entity);
        mainView.removeGameObject(entity);
    }
}
