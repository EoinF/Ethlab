package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.AIType;
import com.mygdx.ethlab.GameObjects.Entity;
import com.mygdx.ethlab.GameObjects.GameObject;
import com.mygdx.ethlab.GameObjects.TerrainShape;
import com.mygdx.ethlab.StateManager.ObjectType;
import com.mygdx.ethlab.StateManager.EditorState;

public class CreateModeTable extends Table {

    public EntityEditorTable entityEditorTable;
    public TerrainEditorTable terrainEditorTable;

    public CreateModeTable(Config config, Skin skin) {
        //
        //Create a button set for choosing the type of object to create
        //
        HorizontalGroup modeRow = new HorizontalGroup();

        //Entities (NPCs with AI)
        SpriteDrawable up = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreateEntityUp")));
        SpriteDrawable down = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreateEntityDown")));
        final Button createEntityButton = new Button(new Button.ButtonStyle(up, down, down));
        modeRow.addActor(createEntityButton);

        //Props (Inanimate Objects affected by physics)
        up = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreatePropUp")));
        down = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreatePropDown")));
        Button createPropButton = new Button(new Button.ButtonStyle(up, down, down));
        modeRow.addActor(createPropButton);

        //Items (Objects that can be picked up or interacted with by Entities)
        up = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreateItemUp")));
        down = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreateItemDown")));
        Button createItemButton = new Button(new Button.ButtonStyle(up, down, down));
        modeRow.addActor(createItemButton);

        //Terrain (Shapes that make up the terrain of the map)
        up = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreateTerrainUp")));
        down = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreateTerrainDown")));
        final Button createTerrainButton = new Button(new Button.ButtonStyle(up, down, down));
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
        entityEditorTable = new EntityEditorTable(config, skin,
                new Entity(config.baseEntityNames[0],
                        GameObject.DEFAULT_COLOUR,
                        Vector2.Zero,
                        Entity.DEFAULT_BOUNDING_BOX,
                        Entity.DEFAULT_MASS,
                        Entity.DEFAULT_HEALTH,
                        AIType.NONE));

        terrainEditorTable = new TerrainEditorTable(config, skin, new TerrainShape(config.textureNames[0], new float[]{}));
        terrainEditorTable
                .align(Align.topLeft)
                .setVisible(false);

        editorStack.add(entityEditorTable);
        editorStack.add(terrainEditorTable);


        //
        //Add the logic for switching between modes
        //
        Runnable updateTableVisibility = () -> {
            entityEditorTable.setVisible(createEntityButton.isChecked());
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
}
