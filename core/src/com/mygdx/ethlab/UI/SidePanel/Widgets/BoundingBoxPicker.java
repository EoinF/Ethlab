package com.mygdx.ethlab.UI.SidePanel.Widgets;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.mygdx.ethlab.UI.SidePanel.ObjectEditorTable;

public class BoundingBoxPicker extends Table {
    private static final float DEFAULT_BOUNDS_PICKER_WIDTH = 70;

    TextField xField;
    TextField yField;
    TextField widthField;
    TextField heightField;


    public BoundingBoxPicker(String labelName, Rectangle defaultBoundingBox, Skin skin) {
        Table coordPickerTable = new Table();
        coordPickerTable.align(Align.left);
        coordPickerTable
                .defaults()
                .padRight(2)
                .padBottom(5);

        Label label = new Label(labelName, skin);
        xField = new TextField(String.valueOf(defaultBoundingBox.x), skin);
        xField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField field, char c) {
                //Handle change in x coordinates

            }
        });
        yField = new TextField(String.valueOf(defaultBoundingBox.y), skin);
        yField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField field, char c) {
                //Handle change in y coordinates
            }
        });
        widthField = new TextField(String.valueOf(defaultBoundingBox.width), skin);
        widthField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField field, char c) {
                //Handle change in width
            }
        });
        heightField = new TextField(String.valueOf(defaultBoundingBox.height), skin);
        heightField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField field, char c) {
                //Handle change in height
            }
        });

        coordPickerTable.add(label).width(ObjectEditorTable.DEFAULT_LABEL_WIDTH);
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

    public void setValues(Rectangle newValues) {
        xField.setText(String.valueOf(newValues.getX()));
        yField.setText(String.valueOf(newValues.getY()));
        widthField.setText(String.valueOf(newValues.getWidth()));
        heightField.setText(String.valueOf(newValues.getHeight()));
    }
}
