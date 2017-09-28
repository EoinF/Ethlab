package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.github.eoinf.ethanolshared.Config;
import com.github.eoinf.ethanolshared.GameObjects.Entity;
import com.github.eoinf.ethanolshared.GameObjects.TerrainShape;
import com.mygdx.ethlab.StateManager.enums.ObjectType;
import com.mygdx.ethlab.StateManager.EditorState;
import com.mygdx.ethlab.UI.EditorObject;

public class CreateModeTable extends Table {

    private EntityEditorTable entityEditorTable;
    private ItemEditorTable itemEditorTable;
    private PropEditorTable propEditorTable;
    private TerrainEditorTable terrainEditorTable;

    private Button createEntityButton;
    private Button createPropButton;
    private Button createItemButton;
    private Button createTerrainButton;

    public CreateModeTable(Config config, Skin skin) {
        //
        //Create a button set for choosing the type of object to create
        //
        HorizontalGroup modeRow = new HorizontalGroup();

        //Entities (NPCs with AI)
        SpriteDrawable up = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreateEntityUp")));
        SpriteDrawable down = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreateEntityDown")));
        createEntityButton = new Button(new Button.ButtonStyle(up, down, down));
        modeRow.addActor(createEntityButton);

        //Props (Inanimate Objects affected by physics)
        up = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreatePropUp")));
        down = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreatePropDown")));
        createPropButton = new Button(new Button.ButtonStyle(up, down, down));
        modeRow.addActor(createPropButton);

        //Items (Objects that can be picked up or interacted with by Entities)
        up = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreateItemUp")));
        down = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreateItemDown")));
        createItemButton = new Button(new Button.ButtonStyle(up, down, down));
        modeRow.addActor(createItemButton);

        //Terrain (Shapes that make up the terrain of the map)
        up = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreateTerrainUp")));
        down = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreateTerrainDown")));
        createTerrainButton = new Button(new Button.ButtonStyle(up, down, down));
        modeRow.addActor(createTerrainButton);

        //Create a button group to only allow one tab selected at a time
        ButtonGroup<Button> modeSelectGroup = new ButtonGroup<Button>(createEntityButton, createPropButton, createItemButton, createTerrainButton);

        modeSelectGroup.setMaxCheckCount(1);
        modeSelectGroup.setMinCheckCount(1);

        createEntityButton.setChecked(true);

        add(modeRow)
                .expandX()
                .fillX()
                .row();

        Stack editorStack = new Stack();

        //
        //Create the table that controls entity attributes
        //
        entityEditorTable = new EntityEditorTable(config, skin);

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


        //
        //Add the logic for switching between modes
        //
        Runnable updateTableVisibility = () -> {
            entityEditorTable.setVisible(createEntityButton.isChecked());
            itemEditorTable.setVisible(createItemButton.isChecked());
            propEditorTable.setVisible(createPropButton.isChecked());
            terrainEditorTable.setVisible(createTerrainButton.isChecked());
        };

        // Switch to Entity
        createEntityButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateTableVisibility.run();
                EditorState.setType(ObjectType.ENTITY);
            }
        });

        // Switch to Item
        createItemButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateTableVisibility.run();
                EditorState.setType(ObjectType.ITEM);
            }
        });

        // Switch to Prop
        createPropButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateTableVisibility.run();
                EditorState.setType(ObjectType.PROP);
            }
        });

        // Switch to Terrain
        createTerrainButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateTableVisibility.run();
                EditorState.setType(ObjectType.TERRAIN);
            }
        });

        add(editorStack)
                .expandX()
                .fillX();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            createEntityButton.setChecked(EditorState.isType(ObjectType.ENTITY));
            createItemButton.setChecked(EditorState.isType(ObjectType.ITEM));
            createPropButton.setChecked(EditorState.isType(ObjectType.PROP));
            createTerrainButton.setChecked(EditorState.isType(ObjectType.TERRAIN));
        }
    }

    public void setObject(EditorObject wrapper) {
        Class objectClass = wrapper.instance.getClass();

        if (objectClass == Entity.class) {
            entityEditorTable.setEntity((Entity) wrapper.instance, wrapper.getId());
        }
        if (objectClass == TerrainShape.class) {
            terrainEditorTable.setShape((TerrainShape) wrapper.instance, wrapper.getId());
        }
    }

    public void setObjectPosition(Vector2 position) {
        switch (EditorState.getType()) {
            case ENTITY:
                entityEditorTable.setPosition(position);
                break;
            default:
                throw new RuntimeException("Unhandled object type: " + EditorState.getType());
        }
    }
    public void setObjectTexture(String textureName) {
        switch (EditorState.getType()) {
            case ENTITY:
                entityEditorTable.setTexture(textureName, Entity.class);
                break;
            default:
                throw new RuntimeException("Unhandled object type: " + EditorState.getType());
        }
    }

    public void setObjectColour(Color newColour) {
        switch (EditorState.getType()) {
            case ENTITY:
                entityEditorTable.setColour(newColour);
                break;
            default:
                throw new RuntimeException("Unhandled object type: " + EditorState.getType());
        }
    }
}
