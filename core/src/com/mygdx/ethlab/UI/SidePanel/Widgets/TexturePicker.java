package com.mygdx.ethlab.UI.SidePanel.Widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.UI.SidePanel.ObjectEditorTable;
import com.mygdx.ethlab.UI.SidePanel.SidePanel;

import java.util.function.Consumer;


public class TexturePicker extends Table {
    private SelectBox<String> textureField;

    private Sprite textureSprite;
    private Image textureDisplay;
    private Consumer<String> onChangeListener;

    public TexturePicker(String attrName, String textureName, String[] textureList, Class<?> objectType, Config config,
                         Skin skin) {

        TextureRegion tex = config.getTexture(textureName, objectType);
        textureSprite = new Sprite(tex);
        textureDisplay = new Image(textureSprite);

        textureField = createSelectBox(attrName, textureName, textureList, skin);

        textureField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                //Update the texture being displayed, when a new one is selected
                String selectedTexture = ((SelectBox<String>) actor).getSelected();
                setTexture(selectedTexture, config, objectType);
                onChangeListener.accept(selectedTexture);
            }
        });

        row();

        Table textureDisplayTable = new Table(skin);

        SidePanel.setBackgroundColour(textureDisplayTable, Color.GRAY);
        textureDisplayTable.add(textureDisplay)
                .padTop(10)
                .padBottom(10);

        add(textureDisplayTable)
                .colspan(2)
                .fillX()
                .expandX()
                .pad(10);

        onChangeListener = (_textureName) -> {};
    }

    public void addChangeListener(Consumer<String> consumer) {
        this.onChangeListener = this.onChangeListener.andThen(consumer);
    }

    public void setTexture(String textureName, Config config, Class<?> objectType) {
        textureField.setSelected(textureName);
        TextureRegion tex = config.getTexture(textureName, objectType);
        textureSprite = new Sprite(tex);
        textureDisplay.setDrawable(new SpriteDrawable(textureSprite));
    }


    public void setColour(Color colour) {
        Color newColour = new Color(colour.r, colour.g, colour.b, textureSprite.getColor().a);
        textureSprite.setColor(newColour);
        textureDisplay.setDrawable(new SpriteDrawable(textureSprite));
    }

    private SelectBox<String> createSelectBox(String selectTitle, String selectedOption, String[] selectOptions, Skin skin) {
        Table selectionRow = new Table();

        Label label = new Label(selectTitle, skin);

        SelectBox<String> selectWidget = new SelectBox<>(skin);
        selectWidget.setItems(selectOptions);

        selectWidget.setSelected(selectedOption);

        selectionRow.add(label).width(ObjectEditorTable.DEFAULT_LABEL_WIDTH);
        selectionRow.add(selectWidget);

        add(selectionRow)
                .align(Align.left);

        return selectWidget;
    }
}
