package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.Item;
import com.mygdx.ethlab.GameObjects.ItemType;
import com.mygdx.ethlab.GameObjects.Prop;

public class PropEditorTable extends ObjectEditorTable {
    public PropEditorTable(Config config, Skin skin) {
        super(config, skin, new Item(ItemType.PLAYER_SPAWN));
        init(skin);
    }

    public void setProp(Prop p, int id) {
        setObject(p, id);
    }

    /**
     * Create a control for each property of an item
     * @param skin The ui texture set to be used
     */
    private void init(Skin skin) {
    }
}
