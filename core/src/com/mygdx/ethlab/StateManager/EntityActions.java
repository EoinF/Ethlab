package com.mygdx.ethlab.StateManager;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.ethlab.EditorMap;
import com.mygdx.ethlab.GameObjects.Entity;
import com.mygdx.ethlab.UI.EditorObject;
import com.mygdx.ethlab.UI.MainView.MainView;
import com.mygdx.ethlab.UI.SidePanel.SidePanel;

public class EntityActions {

    public static void setEntityPosition(SidePanel sidePanel, MainView mainView, EditorMap map, int id, Vector2 position) {
        EditorObject wrapper = map.getObjectById(id);
        Entity entity = (Entity)wrapper.instance;

        entity.position = position;
        sidePanel.getCreateModeTable().entityEditorTable.setPosition(entity.position);
        mainView.updateGameObject(wrapper);
    }

    public static void createEntity(SidePanel sidePanel, MainView mainView, EditorMap map, int id, EditorObject entity) {
        map.addEntity(entity);
        mainView.addGameObject(entity);

        setEntityPosition(sidePanel, mainView, map, id, entity.instance.position);
    }
}
