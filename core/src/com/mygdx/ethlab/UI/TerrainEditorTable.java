package com.mygdx.ethlab.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.TerrainShape;

public class TerrainEditorTable extends ObjectEditorTable {

    private TerrainShape myShape() {
        return (TerrainShape)myObject;
    }

    public TerrainEditorTable(Config config, Skin skin) {
        super(config, skin, new TerrainShape(config.getDefaultTerrainTexture(), new float[] {0, 0}));
        init(skin);
    }

    public TerrainEditorTable(Config config, Skin skin, TerrainShape s) {
        super(config, skin, s);
        init(skin);
    }

    /**
     * Create a control for each property of an entity
     * @param skin The ui texture set to be used
     */
    private void init(Skin skin) {
        addPointsPicker("Points: ", skin);
    }

    private void addPointsPicker(String attrName, Skin skin) {


        Label label = new Label(attrName, skin);
        add(label).width(DEFAULT_LABEL_WIDTH);
        row();
        add(new PointsPicker(myShape(), skin));
        row();
    }
}
