package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.eoinf.ethanolshared.Config;
import com.github.eoinf.ethanolshared.GameObjects.Item;
import com.github.eoinf.ethanolshared.GameObjects.ItemType;
import com.github.eoinf.ethanolshared.GameObjects.Prop;

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
