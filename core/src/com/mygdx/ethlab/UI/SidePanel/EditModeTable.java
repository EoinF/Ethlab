package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.AIType;
import com.mygdx.ethlab.GameObjects.Entity;
import com.mygdx.ethlab.GameObjects.GameObject;
import com.mygdx.ethlab.GameObjects.TerrainShape;
import com.mygdx.ethlab.StateManager.EditorState;
import com.mygdx.ethlab.StateManager.ObjectType;
import com.mygdx.ethlab.UI.EditorObject;

public class EditModeTable extends Table {
    private EntityEditorTable entityEditorTable;
    private TerrainEditorTable terrainEditorTable;

    public EditModeTable(Config config, Skin skin) {
        Stack editorStack = new Stack();

        //
        //Create the table that controls entity attributes
        //
        entityEditorTable = new EntityEditorTable(config, skin,
                new EditorObject<>(
                        new Entity(config.baseEntityNames[0],
                                GameObject.DEFAULT_COLOUR,
                                Vector2.Zero,
                                Entity.DEFAULT_BOUNDING_BOX,
                                Entity.DEFAULT_MASS,
                                Entity.DEFAULT_HEALTH,
                                AIType.NONE)
                )
        );

        entityEditorTable.setVisible(false);

        terrainEditorTable = new TerrainEditorTable(config, skin,
                new EditorObject<>(
                        new TerrainShape(config.textureNames[0], new float[]{})
                )
        );
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

        if (wrapper.instance != null) {
            Class objectClass = wrapper.instance.getClass();
            if (objectClass == Entity.class) {
                entityEditorTable.setVisible(true);
                entityEditorTable.setEntity((Entity) wrapper.instance);
                EditorState.setType(ObjectType.ENTITY);
            } else {

            }
        }
    }
}
