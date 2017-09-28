package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.github.eoinf.ethanolshared.Config;
import com.github.eoinf.ethanolshared.GameObjects.Entity;
import com.github.eoinf.ethanolshared.GameObjects.Item;
import com.github.eoinf.ethanolshared.GameObjects.Prop;
import com.github.eoinf.ethanolshared.GameObjects.TerrainShape;
import com.mygdx.ethlab.StateManager.EditorState;
import com.mygdx.ethlab.StateManager.enums.ObjectType;
import com.mygdx.ethlab.UI.EditorObject;

public class EditModeTable extends Table {
    private EntityEditorTable entityEditorTable;
    private ItemEditorTable itemEditorTable;
    private PropEditorTable propEditorTable;
    private TerrainEditorTable terrainEditorTable;

    public EditModeTable(Config config, Skin skin) {
        Stack editorStack = new Stack();

        //
        //Create the table that controls entity attributes
        //
        entityEditorTable = new EntityEditorTable(config, skin);
        entityEditorTable.setVisible(false);

        // Create the other tables (Terrain, Item, Prop)
        itemEditorTable = new ItemEditorTable(config, skin);
        itemEditorTable
                .align(Align.topLeft)
                .setVisible(false);
        propEditorTable = new PropEditorTable(config, skin);
        propEditorTable
                .align(Align.topLeft)
                .setVisible(false);

        terrainEditorTable = new TerrainEditorTable(config, skin);
        terrainEditorTable
                .align(Align.topLeft)
                .setVisible(false);

        editorStack.add(entityEditorTable);
        editorStack.add(itemEditorTable);
        editorStack.add(propEditorTable);
        editorStack.add(terrainEditorTable);

        add(editorStack)
                .expandX()
                .fillX();
    }


    public void selectObject(EditorObject wrapper) {
        entityEditorTable.setVisible(false);
        itemEditorTable.setVisible(false);
        propEditorTable.setVisible(false);
        terrainEditorTable.setVisible(false);

        if (wrapper != null) {
            Class objectClass = wrapper.instance.getClass();
            if (objectClass == Entity.class) {
                entityEditorTable.setVisible(true);
                entityEditorTable.setEntity((Entity) wrapper.instance, wrapper.getId());
                EditorState.setType(ObjectType.ENTITY);
            } else if (objectClass == Item.class) {
                itemEditorTable.setVisible(true);
                itemEditorTable.setItem((Item) wrapper.instance, wrapper.getId());
                EditorState.setType(ObjectType.ITEM);
            } else if (objectClass == Prop.class) {
                propEditorTable.setVisible(true);
                propEditorTable.setProp((Prop) wrapper.instance, wrapper.getId());
                EditorState.setType(ObjectType.PROP);
            } else if (objectClass == TerrainShape.class) {
                terrainEditorTable.setVisible(true);
                terrainEditorTable.setShape((TerrainShape) wrapper.instance, wrapper.getId());
                EditorState.setType(ObjectType.TERRAIN);
            }
        }
    }
}
