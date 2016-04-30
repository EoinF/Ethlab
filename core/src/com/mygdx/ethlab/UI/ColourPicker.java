package com.mygdx.ethlab.UI;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class ColourPicker extends Table {
    private final TextField redField;
    private final TextField greenField;
    private final TextField blueField;
    private Image bindedImage;
    private Sprite bindedSprite;

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
        this.bindedImage = bindedImage;
        this.bindedSprite = bindedSprite;

        redField = createColourTextField((int)(defaultColour.r * 255), skin);
        greenField = createColourTextField((int)(defaultColour.g * 255), skin);
        blueField = createColourTextField((int)(defaultColour.b * 255), skin);

        Label label = new Label(attrName, skin);

        add(label);
        add(redField).width(DEFAULT_COLOUR_COMPONENT_WIDTH);
        add(greenField).width(DEFAULT_COLOUR_COMPONENT_WIDTH);
        add(blueField).width(DEFAULT_COLOUR_COMPONENT_WIDTH);
    }

    private TextField createColourTextField(int defaultValue, Skin skin) {
        TextField textField = new TextField(String.valueOf(defaultValue), skin);
        addTextFieldFocusLostHandler(textField);
        textField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField field, char c) {
                System.out.println("'" + (int) c + "'");

                Color newColour = bindedSprite.getColor();
                newColour.r = getR();
                newColour.g = getG();
                newColour.b = getB();
                // Update this text field if enter is pressed
                if (c == Input.Keys.ENTER) {
                    field.setText(String.valueOf(getByteFromTextField(field)));
                }
                System.out.println("'" + newColour.r + "," + newColour.g + "," + newColour.b + "'");
                bindedSprite.setColor(newColour);
                bindedImage.setDrawable(new SpriteDrawable(bindedSprite));
            }
        });
        textField.setMaxLength(3);
        return textField;
    }

    private static void addTextFieldFocusLostHandler(TextField field) {
        field.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                super.keyboardFocusChanged(event, actor, focused);
                // Set the value only when we lose focus (more user-friendly than changing every time anything is typed)
                if (!focused) {
                    int value = getByteFromTextField((TextField)actor);
                    ((TextField)actor).setText(String.valueOf(value));
                }
            }
        });
    }

    private static int getByteFromTextField(TextField field) {
        int value;
        String text = field.getText();

        // Check if the text field contains a valid integer
        if (text.matches("\\d+")) {
            value = Integer.parseInt(text);
        } else {
            value = 0;
        }

        // Limit to integers between 0 and 255
        if (value > 255) {
            return 255;
        } else if (value < 0) {
            return 0;
        }
        else {
            return value;
        }
    }
}
