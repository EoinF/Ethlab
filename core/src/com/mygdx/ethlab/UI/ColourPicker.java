package com.mygdx.ethlab.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class ColourPicker extends Table {
    private TextField redField;
    private TextField greenField;
    private TextField blueField;
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
    // Need to declare this as it doesn't match with Input.Keys.ENTER or Input.Keys.CENTER
    private static int TEXTFIELD_ENTER = 13;

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
    }

    public void setImageBinding(Image bindedImage, Sprite bindedSprite) {
        this.bindedImage = bindedImage;
        this.bindedSprite = bindedSprite;
        updateBindedImageColour();
    }

    private TextField createColourTextField(int defaultValue, Skin skin) {
        TextField textField = new TextField(String.valueOf(defaultValue), skin);
        addTextFieldFocusLostHandler(textField);
        textField.setTextFieldListener((field, c) -> {
            // Update this text field if enter is pressed
            if (c == TEXTFIELD_ENTER) {
                field.setText(String.valueOf(getByteFromTextField(field)));
                getParent().getStage().unfocusAll();
            }
            updateBindedImageColour();
        });
        textField.setMaxLength(3);
        return textField;
    }

    private void updateBindedImageColour() {
        Color newColour = bindedSprite.getColor();
        newColour.r = getR();
        newColour.g = getG();
        newColour.b = getB();
        bindedSprite.setColor(newColour);
        bindedImage.setDrawable(new SpriteDrawable(bindedSprite));
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

    public void setValues(Color newColour) {
        redField.setText(String.valueOf(newColour.r));
        greenField.setText(String.valueOf(newColour.g));
        blueField.setText(String.valueOf(newColour.b));
    }
}
