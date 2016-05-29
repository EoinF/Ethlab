package com.mygdx.ethlab.UI;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.AIType;
import com.mygdx.ethlab.GameObjects.Entity;

public class EntityEditorTable extends ObjectEditorTable {
    private static final float DEFAULT_BOUNDS_PICKER_WIDTH = 70;

    private Entity myEntity() {
        return (Entity)myObject;
    }

    public EntityEditorTable(Config config, Skin skin) {
        super(config, skin, new Entity());
        init(skin);
    }
    public EntityEditorTable(Config config, Skin skin, Entity e) {
        super(config, skin, e);
        init(skin);
    }

    /**
     * Create a control for each property of an entity
     * @param skin The ui texture set to be used
     */
    private void init(Skin skin) {
        //addBoundingBoxPicker("Bounds: ", myEntity.boundingBox, skin);
        addFloatNumberPicker("Mass: ", myEntity().mass, skin);
        addFloatNumberPicker("Health: ", myEntity().health, skin);
        addStringPicker("AI: ", myEntity().ai.name(), AIType.getNames(), skin);
    }

    private void addBoundingBoxPicker(String attrName, Rectangle defaultBoundingBox, Skin skin) {
        Table coordPickerTable = new Table();
        coordPickerTable.align(Align.left);
        coordPickerTable
                .defaults()
                .padRight(2)
                .padBottom(5);

        Label label = new Label(attrName, skin);
        TextField xField = new TextField(String.valueOf(defaultBoundingBox.x), skin);
        xField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField field, char c) {
                //Handle change in x coordinates

            }
        });
        TextField yField = new TextField(String.valueOf(defaultBoundingBox.y), skin);
        yField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField field, char c) {
                //Handle change in y coordinates
            }
        });
        TextField widthField = new TextField(String.valueOf(defaultBoundingBox.width), skin);
        widthField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField field, char c) {
                //Handle change in width
            }
        });
        TextField heightField = new TextField(String.valueOf(defaultBoundingBox.height), skin);
        heightField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField field, char c) {
                //Handle change in height
            }
        });

        coordPickerTable.add(label).width(DEFAULT_LABEL_WIDTH);
        coordPickerTable.add(xField).width(DEFAULT_BOUNDS_PICKER_WIDTH);
        coordPickerTable.add(yField).width(DEFAULT_BOUNDS_PICKER_WIDTH);
        coordPickerTable.row();

        coordPickerTable.add(); //Skip to the next column so the x,y and width,height pickers line up
        coordPickerTable.add(widthField).width(DEFAULT_BOUNDS_PICKER_WIDTH);
        coordPickerTable.add(heightField).width(DEFAULT_BOUNDS_PICKER_WIDTH);

        add(coordPickerTable)
                .fillX()
                .expandX();
        row();
    }



}
