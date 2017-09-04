package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PointsPicker extends Table {
    private Skin skin;

    // Need to declare this as it doesn't match with Input.Keys.ENTER or Input.Keys.CENTER
    private static int TEXTFIELD_ENTER = 13;

    private BiConsumer<Integer, Float> onChangeListener;

    PointsPicker(float[] points, Skin skin) {
        align(Align.left);
        defaults()
                .padRight(2)
                .padBottom(5)
                .padTop(10);

        this.skin = skin;

        SidePanel.setBackgroundColour(this, Color.GRAY);
        updatePoints(points);

        this.onChangeListener = (index, value) -> {};
    }

    public void addChangeListener(BiConsumer<Integer, Float> consumer) {
        this.onChangeListener = this.onChangeListener.andThen(consumer);
    }

    private void addPointControl(final int index, final float point) {
        final TextField xField = new TextField(String.valueOf(point), skin);
        add(xField).width(ObjectEditorTable.DEFAULT_COORD_COMPONENT_WIDTH);

        addTextFieldFocusLostHandler(xField);
        xField.setTextFieldListener((field, c) -> {
            float newPoint = getFloatFromTextField(field);

            // Update this text field if enter is pressed
            if (c == TEXTFIELD_ENTER) {
                System.out.println(newPoint);
                field.setText(String.valueOf(newPoint));
            }
            onChangeListener.accept(index, newPoint);
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

    public void updatePoints(float[] points) {
        clearChildren();

        for (int i = 0; i < points.length / 2; i++) {
            int idX = i * 2;
            int idY = i * 2 + 1;
            addPointControl(idX, points[idX]);
            addPointControl(idY, points[idY]);
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