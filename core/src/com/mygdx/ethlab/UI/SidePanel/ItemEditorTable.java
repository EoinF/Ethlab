package com.mygdx.ethlab.UI.SidePanel;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.github.eoinf.ethanolshared.Config;
import com.github.eoinf.ethanolshared.GameObjects.Item;
import com.github.eoinf.ethanolshared.GameObjects.ItemType;

public class ItemEditorTable extends ObjectEditorTable {
    private SelectBox<String> typeField;

    public ItemEditorTable(Config config, Skin skin) {
        super(config, skin, new Item(ItemType.PLAYER_SPAWN));
        init(skin);
    }

    public void setItem(Item e, int id) {
        setObject(e, id);
        setType(String.valueOf(e.type.name()));
    }

    public void setType(String type) {
        typeField.setSelected(String.valueOf(type));
    }

    /**
     * Create a control for each property of an item
     * @param skin The ui texture set to be used
     */
    private void init(Skin skin) {
        Item item = new Item(ItemType.PLAYER_SPAWN);
        typeField = addSelectBox("Type: ", item.type.name(),
                com.github.eoinf.ethanolshared.GameObjects.utils.getEnumNames(ItemType.class), skin);
    }
}
