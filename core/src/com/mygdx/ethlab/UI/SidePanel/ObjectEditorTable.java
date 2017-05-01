package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.GameObject;
import com.mygdx.ethlab.StateManager.CommandFactory;
import com.mygdx.ethlab.StateManager.EditorState;
import com.mygdx.ethlab.StateManager.ModeType;

import static com.mygdx.ethlab.UI.SidePanel.utils.*;

public abstract class ObjectEditorTable extends Table {
    Config config;

    private Image textureDisplay;
    private Sprite textureSprite;

    private SelectBox<String> textureField;
    private ColourPicker colourField;
    private TextField[] positionFields;

    public static final float DEFAULT_COORD_COMPONENT_WIDTH = 70;
    public static final float DEFAULT_NUMBER_PICKER_WIDTH = 70;
    public static final float DEFAULT_LABEL_WIDTH = 70;


    public ObjectEditorTable(Config config, Skin skin, GameObject o) {
        this.config = config;
        init(skin, o);
    }

    public void setObject(GameObject newValues) {
        textureField.setSelected(newValues.textureName);
    }

    public void setPosition(Vector2 position) {
        positionFields[0].setText(String.valueOf(position.x));
        positionFields[1].setText(String.valueOf(position.y));
    }

    /**
     * Create a control for each property of an object
     * @param skin The ui texture set to be used
     */
    private void init(Skin skin, GameObject myObject) {
        defaults()
                .padTop(4)
                .padLeft(5);

        textureField = addTexturePicker("Texture: ", config.getTextureNames(myObject), myObject.getClass(), myObject.textureName, skin);
        colourField = addColourPicker("Colour: ", myObject.colour, skin);
        positionFields = addCoordinatePicker("Position: ", myObject.position, skin);

        addTextFieldCommitInputHandler(positionFields[0], field -> {
            float newX = getFloatFromTextField(field);
            System.out.println(newX);
            field.setText(String.valueOf(newX));
            //CommandFactory.setObjectPosition(myObject.id, new Vector2(newX, myObject.position.y), true);
        });
        addTextFieldCommitInputHandler(positionFields[1], field -> {
            float newY = getFloatFromTextField(field);
            System.out.println(newY);
            field.setText(String.valueOf(newY));
            //CommandFactory.setObjectPosition(myObject.id, new Vector2(myObject.position.x, newY), true);
        });
    }

    SelectBox<String> addTexturePicker(String attrName, String[] textureList, Class<?> objectType, String textureName, Skin skin) {
        TextureRegion tex = config.getTexture(textureName, objectType);
        textureSprite = new Sprite(tex);
        textureDisplay = new Image(textureSprite);

        SelectBox<String> _textureField = addSelectBox(attrName, textureName, textureList, skin);

        _textureField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Update the texture being displayed, when a new one is selected
                SelectBox<String> selectWidget = (SelectBox<String>) actor;
                TextureRegion tex = config.getTexture(selectWidget.getSelected(), objectType);
                textureSprite = new Sprite(tex);
                textureDisplay.setDrawable(new SpriteDrawable(textureSprite));
                colourField.setImageBinding(textureDisplay, textureSprite);
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
        return _textureField;
    }

    TextField addFloatNumberPicker(String attrName, float defaultNumber, Skin skin) {
        Table numberPickerTable = new Table();
        numberPickerTable.align(Align.left);

        Label label = new Label(attrName, skin);

        TextField numberField = new TextField(String.valueOf(defaultNumber), skin);

        numberPickerTable.add(label).width(DEFAULT_LABEL_WIDTH);
        numberPickerTable.add(numberField).width(DEFAULT_NUMBER_PICKER_WIDTH);
        add(numberPickerTable).fillX().expandX();
        row();
        return numberField;
    }

    ColourPicker addColourPicker(String attrName, Color defaultColour, Skin skin) {
        ColourPicker colourPickerTable = new ColourPicker(attrName, defaultColour, textureDisplay, textureSprite, skin);
        colourPickerTable.align(Align.left);
        colourPickerTable
                .defaults()
                    .padRight(2);

        add(colourPickerTable)
                .fillX()
                .expandX();
        row();
        return colourPickerTable;
    }


    private TextField[] addCoordinatePicker(String attrName, Vector2 defaultCoordinates, Skin skin) {
        Table coordPickerTable = new Table();
        coordPickerTable.align(Align.left);
        coordPickerTable
                .defaults()
                    .padRight(2);

        Label label = new Label(attrName, skin);

        TextField xField = new TextField(String.valueOf(defaultCoordinates.x), skin);
        TextField yField = new TextField(String.valueOf(defaultCoordinates.y), skin);

        coordPickerTable.add(label);
        coordPickerTable.add(xField).width(DEFAULT_COORD_COMPONENT_WIDTH);
        coordPickerTable.add(yField).width(DEFAULT_COORD_COMPONENT_WIDTH);
        add(coordPickerTable)
                .fillX()
                .expandX();
        row();

        return new TextField[] { xField, yField };
    }

    SelectBox<String> addSelectBox(String selectTitle, String selectedOption, String[] selectOptions, Skin skin) {
        Table selectionRow = new Table();

        Label label = new Label(selectTitle, skin);

        SelectBox<String> selectWidget = new SelectBox<String>(skin);
        selectWidget.setItems(selectOptions);

        selectWidget.setSelected(selectedOption);

        selectionRow.add(label).width(DEFAULT_LABEL_WIDTH);
        selectionRow.add(selectWidget);

        add(selectionRow)
                .align(Align.left);
        return selectWidget;
    }

}
