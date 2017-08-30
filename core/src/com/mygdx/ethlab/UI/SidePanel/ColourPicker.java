package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import java.util.function.Consumer;

import static com.mygdx.ethlab.UI.SidePanel.utils.addTextFieldCommitInputHandler;
import static com.mygdx.ethlab.UI.SidePanel.utils.getFloatFromTextField;

public class ColourPicker extends Table {
    private TextField redField;
    private TextField greenField;
    private TextField blueField;

    private Consumer<Color> onChangeListener;

    public float getR() {
        return getFloatFromTextField(redField);
    }
    public float getG() {
        return getFloatFromTextField(greenField);
    }
    public float getB() {
        return getFloatFromTextField(blueField);
    }

    public Color getColour() {
        return new Color(getR(), getG(), getB(), 1);
    }

    private static final float DEFAULT_COLOUR_COMPONENT_WIDTH = 42;

    public ColourPicker (String attrName, Color defaultColour, Skin skin) {
        redField = createColourTextField(defaultColour.r, skin);
        greenField = createColourTextField(defaultColour.g, skin);
        blueField = createColourTextField(defaultColour.b, skin);

        Label label = new Label(attrName, skin);

        add(label);
        add(redField).width(DEFAULT_COLOUR_COMPONENT_WIDTH);
        add(greenField).width(DEFAULT_COLOUR_COMPONENT_WIDTH);
        add(blueField).width(DEFAULT_COLOUR_COMPONENT_WIDTH);

        onChangeListener = (newColor) -> {};
    }

    public void addChangeListener(Consumer<Color> consumer) {
        this.onChangeListener = this.onChangeListener.andThen(consumer);
    }

    private TextField createColourTextField(float defaultValue, Skin skin) {
        TextField textField = new TextField(String.valueOf(defaultValue), skin);
        addTextFieldCommitInputHandler(textField, field -> {
            float value = getFloatFromTextField(field);
            field.setText(String.valueOf(value));
            Color newColour = new Color(getR(), getG(), getB(), 1);
            onChangeListener.accept(newColour);
        });
        textField.setMaxLength(6);
        return textField;
    }

    public void setValues(Color newColour) {
        redField.setText(String.valueOf(newColour.r));
        greenField.setText(String.valueOf(newColour.g));
        blueField.setText(String.valueOf(newColour.b));
    }
}
