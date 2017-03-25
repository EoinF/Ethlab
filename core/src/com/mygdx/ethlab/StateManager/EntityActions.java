package com.mygdx.ethlab.StateManager;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.ethlab.UI.EntityEditorTable;

public class EntityActions {

    public static void setEntityPosition(EntityEditorTable editorTable, Vector2 position) {
        editorTable.setPosition(position);
    }
}
