package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;

import java.util.function.Consumer;

public final class utils {
    // Need to declare this as it doesn't match with Input.Keys.ENTER or Input.Keys.CENTER
    private static int TEXTFIELD_ENTER = 13;

    public static void addTextFieldCommitInputHandler(TextField textField, Consumer<TextField> onCommitInput) {
        textField.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                super.keyboardFocusChanged(event, actor, focused);
                // Remove invalid integers (e.g. random symbols or characters) only when we lose focus
                // (more user-friendly than changing every time anything is typed)
                if (!focused) {
                    onCommitInput.accept((TextField)actor);
                }
            }
        });

        textField.setTextFieldListener((field, c) -> {
            // Update this text field if enter is pressed
            if (c == TEXTFIELD_ENTER) {
                onCommitInput.accept(field);
            }
        });
    }

    public static int getByteFromTextField(TextField field) {
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

    public static float getFloatFromTextField(TextField field) {
        float value;

        String text = field.getText();
        if (text.matches("\\d+\\.?\\d+")) {
            value = Float.parseFloat(text);
        } else {
            value = 0;
        }

        return value;
    }

}
