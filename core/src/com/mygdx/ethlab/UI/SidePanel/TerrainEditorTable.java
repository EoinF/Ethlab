package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.TerrainShape;
import com.mygdx.ethlab.StateManager.EditorState;
import com.mygdx.ethlab.UI.EditorObject;
import com.sun.scenario.effect.impl.prism.ps.PPSOneSamplerPeer;

public class TerrainEditorTable extends ObjectEditorTable {

    private PointsPicker pointsPicker;

    public TerrainEditorTable(Config config, Skin skin) {
        super(config, skin, new TerrainShape(config.getDefaultTerrainTexture()));
        init(config, skin);
    }

    public void setShape(TerrainShape shape, int id) {
        setObject(shape, id);
        setPoints(shape.getPoints());
    }

    public void setPoints(float[] points) {
        pointsPicker.updatePoints(points);
    }

    /**
     * Create a control for each property of an entity
     * @param skin The ui texture set to be used
     * @param config The config containing all texture assets
     */
    private void init(Config config, Skin skin) {
        TerrainShape shape = new TerrainShape(config.getDefaultTerrainTexture());
        pointsPicker = addPointsPicker("Points: ", shape.getPoints(), skin);

        pointsPicker.addChangeListener((index, value) -> {
            EditorObject focusedObject = EditorState.getFocusedObject();
            ((TerrainShape)focusedObject.instance).setPoint(index, value);
            EditorState.setFocusedObject(focusedObject);

            if (index == 0 || index == 1) {
                this.setPosition(focusedObject.instance.getPosition());
            }
        });
    }

    private PointsPicker addPointsPicker(String attrName, float[] points, Skin skin) {
        Label label = new Label(attrName, skin);
        add(label).width(DEFAULT_LABEL_WIDTH);
        row();

        PointsPicker picker = new PointsPicker(points, skin);
        add(picker);

        row();
        return picker;
    }
}
