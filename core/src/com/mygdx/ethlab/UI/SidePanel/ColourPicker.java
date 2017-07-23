package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import java.util.function.Consumer;

import static com.mygdx.ethlab.UI.SidePanel.utils.addTextFieldCommitInputHandler;
import static com.mygdx.ethlab.UI.SidePanel.utils.getByteFromTextField;

public class ColourPicker extends Table {
    private TextField redField;
    private TextField greenField;
    private TextField blueField;
    private Image bindedImage;
    private Sprite bindedSprite;

    private Consumer<Color> onChangeListener;

    public float getR() {
        return getByteFromTextField(redField) / 255f;
    }
    public float getG() {
        return getByteFromTextField(greenField) / 255f;
    }
    public float getB() {
        return getByteFromTextField(blueField) / 255f;
    }

    private static final float DEFAULT_COLOUR_COMPONENT_WIDTH = 42;

    public ColourPicker (String attrName, Color defaultColour, Image bindedImage, Sprite bindedSprite, Skin skin) {
        redField = createColourTextField((int)(defaultColour.r * 255), skin);
        greenField = createColourTextField((int)(defaultColour.g * 255), skin);
        blueField = createColourTextField((int)(defaultColour.b * 255), skin);

        setImageBinding(bindedImage, bindedSprite);

        Label label = new Label(attrName, skin);

        add(label);
        add(redField).width(DEFAULT_COLOUR_COMPONENT_WIDTH);
        add(greenField).width(DEFAULT_COLOUR_COMPONENT_WIDTH);
        add(blueField).width(DEFAULT_COLOUR_COMPONENT_WIDTH);

        onChangeListener = (newColor) -> {};
    }

    public void setImageBinding(Image bindedImage, Sprite bindedSprite) {
        this.bindedImage = bindedImage;
        this.bindedSprite = bindedSprite;
        updateBindedImageColour();
    }

    public void addChangeListener(Consumer<Color> consumer) {
        this.onChangeListener = this.onChangeListener.andThen(consumer);
    }

    private TextField createColourTextField(int defaultValue, Skin skin) {
        TextField textField = new TextField(String.valueOf(defaultValue), skin);
        addTextFieldCommitInputHandler(textField, field -> {
            int value = getByteFromTextField(field);
            field.setText(String.valueOf(value));
            Color newColour = updateBindedImageColour();
            onChangeListener.accept(newColour);
        });
        textField.setMaxLength(3);
        return textField;
    }

    private Color updateBindedImageColour() {
        Color newColour = bindedSprite.getColor();
        newColour.r = getR();
        newColour.g = getG();
        newColour.b = getB();
        bindedSprite.setColor(newColour);
        bindedImage.setDrawable(new SpriteDrawable(bindedSprite));
        return newColour;
    }

    public void setValues(Color newColour) {
        redField.setText(String.valueOf(newColour.r));
        greenField.setText(String.valueOf(newColour.g));
        blueField.setText(String.valueOf(newColour.b));
    }
}
