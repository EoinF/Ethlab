package com.mygdx.ethlab.UI;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.ethlab.Config;

/**
 * Created by Eoin on 03/06/2015.
 */
public class Toolbar extends Table{
    public Button createModeButton;
    public Button editModeButton;
    public Button triggerModeButton;

    public Toolbar(TextureAtlas atlas, Skin skin) {
        super();
        addDataRow(skin);
        row();
        addModeRow(atlas);
    }

    void addDataRow(Skin skin) {
        Table dataRow = new Table();

        dataRow
                .align(Align.center);

        dataRow.defaults()
                .width(80)
                .height(30);

        TextButton newMap = new TextButton("New", skin);
        //newMap.setFillParent(true);
        TextButton loadMap = new TextButton("Load", skin);
        TextButton saveMap = new TextButton("Save", skin);

        dataRow.add(newMap);
        dataRow.add(loadMap);
        dataRow.add(saveMap);

        add(dataRow).padTop(10).fillX();
    }

    void addModeRow(TextureAtlas atlas) {
        HorizontalGroup modeRow = new HorizontalGroup();

        //Create mode (Allows creation of game objects)
        SpriteDrawable up = new SpriteDrawable(new Sprite(atlas.findRegion("UI/SidePanel/MoveModeUp")));
        SpriteDrawable down = new SpriteDrawable(new Sprite(atlas.findRegion("UI/SidePanel/MoveModeDown")));
        createModeButton = new Button(new Button.ButtonStyle(up, down, down));
        modeRow.addActor(createModeButton);

        //Edit Mode (Allows attributes of objects to be modified and objects to be moved/removed)
        up = new SpriteDrawable(new Sprite(atlas.findRegion("UI/SidePanel/EditModeUp")));
        down = new SpriteDrawable(new Sprite(atlas.findRegion("UI/SidePanel/EditModeDown")));
        editModeButton = new Button(new Button.ButtonStyle(up, down, down));
        modeRow.addActor(editModeButton);

        //Triggers Mode (Allows game events to be added)
        up = new SpriteDrawable(new Sprite(atlas.findRegion("UI/SidePanel/CreateModeUp")));
        down = new SpriteDrawable(new Sprite(atlas.findRegion("UI/SidePanel/CreateModeDown")));
        triggerModeButton = new Button(new Button.ButtonStyle(up, down, down));
        modeRow.addActor(triggerModeButton);

        //Create a button group to only allow one button being pressed at a time
        ButtonGroup<Button> modeSelectGroup = new ButtonGroup<Button>(createModeButton, editModeButton, triggerModeButton);

        modeSelectGroup.setMaxCheckCount(1);
        modeSelectGroup.setMinCheckCount(1);

        createModeButton.setChecked(true);

        add(modeRow).padTop(10).fillX();
    }
}
