package com.mygdx.ethlab.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.Entity;

/**
 * Created by Eoin on 01/06/2015.
 */
public class SidePanel extends Table {

    public SidePanel(float width, float height, Config config, Skin skin) {
        super();

        setWidth(width);
        setHeight(height);
        align(Align.top);

        //
        //Set the background for the side panel
        //
        setBackgroundColour(this, new Color(0.1f, 0.1f, 0.1f, 1));

        //
        //Toolbar control that is always visible
        //
        Toolbar toolbar = new Toolbar(config.atlas, skin);
        add(toolbar).expandX().fillX();
        row();
//
        //Scroll table within sidepanel
        //
        Table scrolltable = new Table()
                .align(Align.left | Align.top);

        ScrollPane scroll = new ScrollPane(scrolltable, skin);

        add(scroll)
                .pad(10)
                .expand().fill();

        row();

        //
        //Contents of sidebar (Changes based on which mode is selected)
        //
        Table createModeTable = buildCreateModeTable(config, skin);
        scrolltable.add(createModeTable).expandX().fillX();
        //addSidebarContents(scrolltable, atlas);
    }

    void addSidebarContents(Table scrolltable, Config config) {

    }

    Table buildCreateModeTable(Config config, Skin skin) {
        Table createModeTable = new Table();

        //
        //Create a button set for choosing the type of object to create
        //
        HorizontalGroup modeRow = new HorizontalGroup();

        //Entities (NPCs with AI)
        SpriteDrawable up = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreateEntityUp")));
        SpriteDrawable down = new SpriteDrawable(new Sprite(config.atlas.findRegion("UI/SidePanel/CreateEntityDown")));
        Button createEntityButton = new Button(new Button.ButtonStyle(up, down, down));
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
        Button createTerrainButton = new Button(new Button.ButtonStyle(up, down, down));
        modeRow.addActor(createTerrainButton);

        //Create a button group to only allow one button being pressed at a time
        ButtonGroup<Button> modeSelectGroup = new ButtonGroup<Button>(createEntityButton, createPropButton, createItemButton, createTerrainButton);

        modeSelectGroup.setMaxCheckCount(1);
        modeSelectGroup.setMinCheckCount(1);

        createEntityButton.setChecked(true);

        createModeTable.add(modeRow)
                .padTop(5)
                .expandX()
                .fillX()
                .row();

        //
        //Create the table that contains the contents of
        //
        EntityEditor editorTable = new EntityEditor(config, skin);

        createModeTable.add(editorTable)
                .expandX()
                .fillX();

        return createModeTable;
    }

    public static void setBackgroundColour(Table table, Color colour) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        pixmap.setColor(colour);
        pixmap.fill();
        Drawable background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        table.setBackground(background);
        pixmap.dispose();
    }

}
