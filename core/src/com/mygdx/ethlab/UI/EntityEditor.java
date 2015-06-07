package com.mygdx.ethlab.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.Entity;

/**
 * Created by Eoin on 04/06/2015.
 */
public class EntityEditor extends ObjectEditor {

    public EntityEditor(Config config, Skin skin) {
        super(config, skin);
        //
        //Create a label and control for each property of an entity
        //
    }
    public EntityEditor(Config config, Skin skin, Entity e) {
        super(config, skin, e);
        //
        //Create a label and control for each property of an entity
        //
    }


}
