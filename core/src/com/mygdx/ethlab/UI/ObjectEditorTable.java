package com.mygdx.ethlab.UI;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.GameObject;

public class ObjectEditorTable extends Table {
    Config config;
    protected GameObject myObject;

    Image textureDisplay;
    Sprite textureSprite;

    private static final float DEFAULT_COORD_COMPONENT_WIDTH = 70;
    private static final float DEFAULT_NUMBER_PICKER_WIDTH = 70;
    public static final float DEFAULT_LABEL_WIDTH = 70;

    public ObjectEditorTable(Config config, Skin skin) {
        this.config = config;
        myObject = new GameObject();
        init(skin);
    }

    public ObjectEditorTable(Config config, Skin skin, GameObject o) {
        this.config = config;
        myObject = o;
        init(skin);
    }

    /**
     * Create a control for each property of an object
     * @param skin The ui texture set to be used
     */
    private void init(Skin skin) {
        defaults()
                .padTop(4)
                .padLeft(5);

        addTexturePicker("Texture: ", config.getTextureNames(myObject), skin);
        addColourPicker("Colour: ", myObject.colour, skin);
        addCoordinatePicker("Position: ", myObject.position, skin);
    }

    void addTexturePicker(String attrName, String[] textureList, Skin skin) {
        //Ensure a valid texture is always selected by default
        if(myObject.textureName == null) {
            myObject.textureName = textureList[0];
        }

        TextureRegion tex = config.getTexture(myObject);
        textureSprite = new Sprite(tex);
        textureDisplay = new Image(textureSprite);

        addSelectionRow(attrName, myObject.textureName, textureList, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Update the texture being displayed, when a new one is selected
                SelectBox<String> selectWidget = (SelectBox<String>) actor;
                myObject.textureName = selectWidget.getSelected();
                TextureRegion tex = config.getTexture(myObject);
                textureSprite = new Sprite(tex);
                textureDisplay.setDrawable(new SpriteDrawable(textureSprite));
            }
        });
        row();

        //
        //One row to display the selected texture
        //
        Table textureDisplayContainer = new Table();
        SidePanel.setBackgroundColour(textureDisplayContainer, Color.GRAY);
        textureDisplayContainer.add(textureDisplay)
            .padTop(10)
            .padBottom(10);
        add(textureDisplayContainer)
                .colspan(2)
                .fillX()
                .expandX()
                .pad(10);
        row();
    }

    void addStringPicker(String attrName, String selectedItem, String[] itemList, Skin skin) {
        addSelectionRow(attrName, selectedItem, itemList, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
    }

    void addNumberPicker() {

    }

    void addFloatNumberPicker(String attrName, float defaultNumber, Skin skin) {
        Table numberPickerTable = new Table();
        numberPickerTable.align(Align.left);

        Label label = new Label(attrName, skin);

        TextField numberField = new TextField(String.valueOf(defaultNumber), skin);

        numberPickerTable.add(label).width(DEFAULT_LABEL_WIDTH);
        numberPickerTable.add(numberField).width(DEFAULT_NUMBER_PICKER_WIDTH);
        add(numberPickerTable).fillX().expandX();
        row();
    }

    void addColourPicker(String attrName, Color defaultColour, Skin skin) {
        Table colourPickerTable = new ColourPicker(attrName, defaultColour, textureDisplay, textureSprite, skin);
        colourPickerTable.align(Align.left);
        colourPickerTable
                .defaults()
                    .padRight(2);

        add(colourPickerTable)
                .fillX()
                .expandX();
        row();
    }


    private void addCoordinatePicker(String attrName, Vector2 defaultCoordinates, Skin skin) {
        Table coordPickerTable = new Table();
        coordPickerTable.align(Align.left);
        coordPickerTable
                .defaults()
                    .padRight(2);

        Label label = new Label(attrName, skin);
        TextField xField = new TextField(String.valueOf(defaultCoordinates.x), skin);
        xField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField field, char c) {
                //Handle change in x coordinates
            }
        });TextField yField = new TextField(String.valueOf(defaultCoordinates.y), skin);
        yField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField field, char c) {
                //Handle change in y coordinates
            }
        });

        coordPickerTable.add(label);
        coordPickerTable.add(xField).width(DEFAULT_COORD_COMPONENT_WIDTH);
        coordPickerTable.add(yField).width(DEFAULT_COORD_COMPONENT_WIDTH);
        add(coordPickerTable)
                .fillX()
                .expandX();
        row();
    }

    private void addSelectionRow(String selectTitle, String selectedOption, String[] selectOptions, Skin skin, ChangeListener listener) {
        Table selectionRow = new Table();

        Label label = new Label(selectTitle, skin);

        SelectBox<String> selectWidget = new SelectBox<String>(skin);
        selectWidget.setItems(selectOptions);
        selectWidget.addListener(listener);

        selectWidget.setSelected(selectedOption);

        selectionRow.add(label).width(DEFAULT_LABEL_WIDTH);
        selectionRow.add(selectWidget);

        add(selectionRow)
                .align(Align.left);
    }

}
