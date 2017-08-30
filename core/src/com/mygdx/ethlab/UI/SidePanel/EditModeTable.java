package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.Entity;
import com.mygdx.ethlab.StateManager.EditorState;
import com.mygdx.ethlab.StateManager.enums.ObjectType;
import com.mygdx.ethlab.UI.EditorObject;

public class EditModeTable extends Table {
    private EntityEditorTable entityEditorTable;
    private TerrainEditorTable terrainEditorTable;

    public EditModeTable(Config config, Skin skin) {
        Stack editorStack = new Stack();

        //
        //Create the table that controls entity attributes
        //
        entityEditorTable = new EntityEditorTable(config, skin);

        entityEditorTable.setVisible(false);

        terrainEditorTable = new TerrainEditorTable(config, skin);
        terrainEditorTable
                .align(Align.topLeft)
                .setVisible(false);

        editorStack.add(entityEditorTable);
        editorStack.add(terrainEditorTable);

        add(editorStack)
                .expandX()
                .fillX();
    }


    public void selectObject(EditorObject wrapper) {
        entityEditorTable.setVisible(false);
        terrainEditorTable.setVisible(false);

        if (wrapper != null) {
            Class objectClass = wrapper.instance.getClass();
            if (objectClass == Entity.class) {
                entityEditorTable.setVisible(true);
                entityEditorTable.setEntity((Entity) wrapper.instance, wrapper.getId());
                EditorState.setType(ObjectType.ENTITY);
            } else {

            }
        }
    }
}
