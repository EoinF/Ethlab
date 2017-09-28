package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.github.eoinf.ethanolshared.Config;
import com.github.eoinf.ethanolshared.GameObjects.GameObject;
import com.mygdx.ethlab.StateManager.CommandFactory;
import com.mygdx.ethlab.StateManager.EditorState;
import com.mygdx.ethlab.StateManager.enums.ModeType;
import com.mygdx.ethlab.UI.EditorObject;
import com.mygdx.ethlab.UI.SidePanel.Widgets.ColourPicker;
import com.mygdx.ethlab.UI.SidePanel.Widgets.TexturePicker;

import static com.mygdx.ethlab.UI.SidePanel.utils.*;

public abstract class ObjectEditorTable extends Table {
    private Config config;

    private ColourPicker colourField;
    private TexturePicker textureField;
    private TextField[] positionFields;

    private int id;
    protected Table scrollTable;

    public static final float DEFAULT_COORD_COMPONENT_WIDTH = 70;
    public static final float DEFAULT_NUMBER_PICKER_WIDTH = 70;
    public static final float DEFAULT_LABEL_WIDTH = 70;


    public ObjectEditorTable(Config config, Skin skin, GameObject gameObject) {
        this.config = config;
        //
        //Scroll table within sidepanel
        //
        scrollTable = new Table()
                .align(Align.left | Align.top)
                .padRight(10)
                .padBottom(10);

        ScrollPane scroll = new ScrollPane(scrollTable, skin);

        add(scroll)
                .expand().fill();

        init(skin, gameObject);
    }

    public void setObject(GameObject newValues, int id) {
        this.id = id;
        setPosition(newValues.getPosition());
        colourField.setValues(newValues.colour);
        setTexture(newValues.textureName, newValues.getClass());
    }

    public void setPosition(Vector2 position) {
        positionFields[0].setText(String.valueOf(position.x));
        positionFields[1].setText(String.valueOf(position.y));
    }

    public void setTexture(String textureName, Class<?> objectType) {
        textureField.setTexture(textureName, config, objectType);
        textureField.setColour(colourField.getColour());
    }

    public void setColour(Color newColour) {
        colourField.setValues(newColour);
        textureField.setColour(newColour);
    }

    /**
     * Create a control for each property of an object
     * @param skin The ui texture set to be used
     */
    private void init(Skin skin, GameObject gameObject) {
        scrollTable.defaults()
                .padTop(4)
                .padLeft(5);

        textureField = addTexturePicker("Texture: ",
                config.getTextureNames(gameObject.getClass()), gameObject.getClass(), gameObject.textureName, skin);
        colourField = addColourPicker("Colour: ", gameObject.colour, skin);
        positionFields = addCoordinatePicker("Position: ", gameObject.getPosition(), skin);

        addTextFieldCommitInputHandler(positionFields[0], field -> {
            float newX = getFloatFromTextField(field);
            float oldY = getFloatFromTextField(positionFields[1]);
            field.setText(String.valueOf(newX));

            Vector2 newPosition = new Vector2(newX, oldY);
            updatePosition(this.id, newPosition);
        });
        addTextFieldCommitInputHandler(positionFields[1], field -> {
            float newY = getFloatFromTextField(field);
            float oldX = getFloatFromTextField(positionFields[0]);
            field.setText(String.valueOf(newY));

            Vector2 newPosition = new Vector2(oldX, newY);
            updatePosition(this.id, newPosition);
        });

        colourField.addChangeListener((newColour) -> {
            if (EditorState.isMode(ModeType.CREATE)) {
                EditorObject focusedObject = EditorState.getFocusedObject();
                focusedObject.setColour(newColour);
                EditorState.setFocusedObject(focusedObject, true);
            } else {
                EditorState.performAction(
                        CommandFactory.setObjectColour(
                                this.id, newColour, true), config);
            }
            textureField.setColour(newColour);
        });

        textureField.addChangeListener((newTexture) -> {
            if (EditorState.isMode(ModeType.CREATE)) {
                EditorObject focusedObject = EditorState.getFocusedObject();
                focusedObject.setTexture(newTexture, config);
                EditorState.setFocusedObject(focusedObject, true);
            } else {
                EditorState.performAction(
                        CommandFactory.setObjectTexture(
                                this.id, newTexture, true), config);
            }
            textureField.setColour(colourField.getColour());
        });
    }

    private void updatePosition(int objectId, Vector2 newPosition) {
        if (EditorState.isMode(ModeType.CREATE)
                || EditorState.isMode(ModeType.ADD_VERTICES)) {
            EditorObject focusedObject = EditorState.getFocusedObject();
            focusedObject.setPosition(newPosition);
            EditorState.setFocusedObject(focusedObject, true);
        } else {
            EditorState.performAction(
                    CommandFactory.setObjectPosition(objectId, newPosition, true), config
            );
        }
    }

    TexturePicker addTexturePicker(String attrName, String[] textureList, Class<?> objectType, String textureName, Skin skin) {
        TexturePicker texturePicker = new TexturePicker(attrName, textureName, textureList, objectType, config, skin);

        scrollTable.add(texturePicker)
                .colspan(2)
                .fillX()
                .expandX();
        scrollTable.row();
        return texturePicker;
    }

    TextField addFloatNumberPicker(String attrName, float defaultNumber, Skin skin) {
        Table numberPickerTable = new Table();
        numberPickerTable.align(Align.left);

        Label label = new Label(attrName, skin);

        TextField numberField = new TextField(String.valueOf(defaultNumber), skin);

        numberPickerTable.add(label).width(DEFAULT_LABEL_WIDTH);
        numberPickerTable.add(numberField).width(DEFAULT_NUMBER_PICKER_WIDTH);
        scrollTable.add(numberPickerTable).fillX().expandX();
        scrollTable.row();
        return numberField;
    }

    ColourPicker addColourPicker(String attrName, Color defaultColour, Skin skin) {
        ColourPicker colourPickerTable = new ColourPicker(attrName, defaultColour, skin);
        colourPickerTable.align(Align.left);
        colourPickerTable
                .defaults()
                    .padRight(2);

        scrollTable.add(colourPickerTable)
                .fillX()
                .expandX();
        scrollTable.row();
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
        scrollTable.add(coordPickerTable)
                .fillX()
                .expandX();
        scrollTable.row();

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

        scrollTable.add(selectionRow)
                .align(Align.left);
        return selectWidget;
    }

}
