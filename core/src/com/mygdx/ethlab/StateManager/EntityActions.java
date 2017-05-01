package com.mygdx.ethlab.StateManager;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.ethlab.GameMap;
import com.mygdx.ethlab.GameObjects.Entity;
import com.mygdx.ethlab.GameObjects.GameObject;
import com.mygdx.ethlab.UI.MainView.MainView;
import com.mygdx.ethlab.UI.SidePanel.SidePanel;

public class EntityActions {

    public static void setEntityPosition(SidePanel sidePanel, MainView mainView, GameMap map, int id, Vector2 position) {
        Entity entity = (Entity)map.getObjectById(id);

        entity.position = position;
        sidePanel.getCreateModeTable().entityEditorTable.setPosition(entity.position);
        mainView.updateGameObject(entity);
    }

    public static void createObject(SidePanel sidePanel, MainView mainView, GameMap map, int id, GameObject newObject) {
        map.addObject(newObject);

        setEntityPosition(sidePanel, mainView, map, id, newObject.position);
    }
}
