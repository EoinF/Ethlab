package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.TerrainShape;
import com.mygdx.ethlab.UI.EditorObject;

public class TerrainEditorTable extends ObjectEditorTable {


    public TerrainEditorTable(Config config, Skin skin, EditorObject s) {
        super(config, skin, s);
        init(s, skin);
    }

    /**
     * Create a control for each property of an entity
     * @param skin The ui texture set to be used
     */
    private void init(EditorObject editorObject, Skin skin) {
        addPointsPicker("Points: ", ((TerrainShape)editorObject.instance).getPoints(), skin);
    }

    private void addPointsPicker(String attrName, float[] points, Skin skin) {
        Label label = new Label(attrName, skin);
        add(label).width(DEFAULT_LABEL_WIDTH);
        row();
        add(new PointsPicker(points, skin));
        row();
    }
}
