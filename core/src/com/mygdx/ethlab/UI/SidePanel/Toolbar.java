package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.ethlab.EditorMap;
import com.mygdx.ethlab.StateManager.EditorState;

import javax.swing.*;

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
        TextButton loadMap = new TextButton("Load", skin);
        TextButton saveMap = new TextButton("Save", skin);

        loadMap.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                JFileChooser fileChooser = new JFileChooser("core\\assets");
                int value = fileChooser.showOpenDialog(null);
                if (value == JFileChooser.APPROVE_OPTION) {
                    EditorMap map = EditorMap.loadMap(new FileHandle(fileChooser.getSelectedFile()));
                    EditorState.setMap(map);
                }
            }
        });

        saveMap.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                JFileChooser fileChooser = new JFileChooser("core\\assets");
                int value = fileChooser.showSaveDialog(null);
                if (value == JFileChooser.APPROVE_OPTION) {
                    EditorMap map = EditorState.getMap();
                    Json json = new Json(JsonWriter.OutputType.json);
                    json.toJson(map, new FileHandle(fileChooser.getSelectedFile()));
                }
            }
        });

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
