package com.mygdx.ethlab.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.TerrainShape;

/**
 * Created by Eoin on 04/06/2015.
 */
public class TerrainEditorTable extends ObjectEditorTable {
    public TerrainEditorTable(Config config, Skin skin) {
        super(config, skin, new TerrainShape());
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
        //addPointsPicker("Points: ", myShape.points);
    }

    private void addPointsPicker(String attrName) {

    }

}
