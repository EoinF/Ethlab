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
    private IShape2D shape;

    PointsPicker(IShape2D shape2D, Skin skin) {
        align(Align.left);
        defaults()
                .padRight(2)
                .padBottom(5)
                .padTop(10);

        this.shape = shape2D;
        this.skin = skin;
        SidePanel.setBackgroundColour(this, Color.GRAY);
        updatePoints(shape2D);
    }

    private void addPointControl(final int index) {
        final TextField xField = new TextField(String.valueOf(shape.getPoints()[index]), skin);
        add(xField).width(DEFAULT_COORD_COMPONENT_WIDTH);

        addTextFieldFocusLostHandler(xField);
        xField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField field, char c) {
                float[] points = shape.getPoints();

                float newPoint = getFloatFromTextField(field);
                // Only re-render the shape if the point has changed
                if (points[index] != newPoint) {
                    points[index] = newPoint;
                    shape.setPoints(points);
                }

                // Update this text field if enter is pressed
                System.out.println("point");
                System.out.println(c);
                if (c == Input.Keys.ENTER) {
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

    void updatePoints(IShape2D shape2D) {
        this.shape = shape2D;
        clearChildren();

        float[] points = shape.getPoints();
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