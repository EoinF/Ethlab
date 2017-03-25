package com.mygdx.ethlab.UI;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import static com.mygdx.ethlab.UI.ObjectEditorTable.DEFAULT_COORD_COMPONENT_WIDTH;

public class PointsPicker extends Table {
    private Skin skin;

    // Need to declare this as it doesn't match with Input.Keys.ENTER or Input.Keys.CENTER
    private static int TEXTFIELD_ENTER = 13;

    PointsPicker(float[] points, Skin skin) {
        align(Align.left);
        defaults()
                .padRight(2)
                .padBottom(5)
                .padTop(10);

        this.skin = skin;
        SidePanel.setBackgroundColour(this, Color.GRAY);
        updatePoints(points);
    }

    private void addPointControl(final float point) {
        final TextField xField = new TextField(String.valueOf(point), skin);
        add(xField).width(DEFAULT_COORD_COMPONENT_WIDTH);

        addTextFieldFocusLostHandler(xField);
        xField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField field, char c) {

                float newPoint = getFloatFromTextField(field);

                // Update this text field if enter is pressed
                if (c == TEXTFIELD_ENTER) {
                    System.out.println(newPoint);
                    field.setText(String.valueOf(newPoint));
                }
            }
        });
    }

    private static void addTextFieldFocusLostHandler(TextField field) {
        field.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                super.keyboardFocusChanged(event, actor, focused);
                // Set the value only when we lose focus (more user-friendly than changing every time anything is typed)
                if (!focused) {
                    float value = getFloatFromTextField((TextField)actor);
                    ((TextField)actor).setText(String.valueOf(value));
                }
            }
        });
    }

    void updatePoints(float[] points) {
        clearChildren();

        for (int i = 0; i < points.length / 2; i++) {
            addPointControl(i * 2);
            addPointControl(i * 2 + 1);
            row();
        }
    }

    private static float getFloatFromTextField(TextField field)
    {
        try {
            return Float.parseFloat(field.getText());
        }
        catch (Exception ex) {
            return 0;
        }
    }
}