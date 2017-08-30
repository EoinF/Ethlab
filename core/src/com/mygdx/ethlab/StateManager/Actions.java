package com.mygdx.ethlab.StateManager;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.ethlab.EditorMap;
import com.mygdx.ethlab.GameObjects.Entity;
import com.mygdx.ethlab.GameObjects.GameObject;
import com.mygdx.ethlab.UI.EditorObject;
import com.mygdx.ethlab.UI.MainView.MainView;
import com.mygdx.ethlab.UI.SidePanel.SidePanel;

public class Actions {

    public static void setObjectPosition(SidePanel sidePanel, MainView mainView, EditorMap map, int id, Vector2 position) {
        EditorObject wrapper = map.getObjectById(id);
        GameObject gameObject = wrapper.instance;

        gameObject.position = position;

        map.updateEntity(wrapper);
        sidePanel.getCreateModeTable().setObjectPosition(gameObject.position);
        mainView.updateGameObject(wrapper);
    }

    public static void setObjectTexture(SidePanel sidePanel, MainView mainView, EditorMap map, int id, String textureName) {
        EditorObject wrapper = map.getObjectById(id);
        GameObject gameObject = wrapper.instance;

        gameObject.textureName = textureName;
        //map.updateEntity(id, gameObject);
        //sidePanel.getCreateModeTable().setObjectTexture(gameObject.textureName);
        //mainView.updateGameObject(wrapper);
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
