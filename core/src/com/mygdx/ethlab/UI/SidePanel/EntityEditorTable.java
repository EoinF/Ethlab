package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.AIType;
import com.mygdx.ethlab.GameObjects.Entity;
import com.mygdx.ethlab.UI.SidePanel.Widgets.BoundingBoxPicker;

public class EntityEditorTable extends ObjectEditorTable {
    private BoundingBoxPicker boundingBoxPicker;
    private TextField massField;
    private TextField healthField;
    private SelectBox<String> aiField;

    public EntityEditorTable(Config config, Skin skin) {
        super(config, skin, new Entity());
        init(skin);
    }

    public void setEntity(Entity e, int id) {
        setObject(e, id);
        setBounds(e.boundingBox);
        setHealth(String.valueOf(e.health));
        setMass(String.valueOf(e.mass));
        setAi(String.valueOf(e.ai.name()));
    }

    public void setBounds(Rectangle boundingBox) {
        boundingBoxPicker.setValues(boundingBox);
    }

    public void setMass(String mass) {
        massField.setText(String.valueOf(mass));
    }

    public void setHealth(String health) {
        healthField.setText(String.valueOf(health));
    }

    public void setAi(String ai) {
        aiField.setSelected(String.valueOf(ai));
    }

    /**
     * Create a control for each property of an entity
     * @param skin The ui texture set to be used
     */
    private void init(Skin skin) {
        Entity entity = new Entity();
        boundingBoxPicker = new BoundingBoxPicker("Bounds: ", entity.boundingBox, skin);
        massField = addFloatNumberPicker("Mass: ", entity.mass, skin);
        healthField = addFloatNumberPicker("Health: ", entity.health, skin);
        aiField = addSelectBox("AI: ", entity.ai.name(),
                com.mygdx.ethlab.GameObjects.utils.getEnumNames(AIType.class), skin);
    }




}
