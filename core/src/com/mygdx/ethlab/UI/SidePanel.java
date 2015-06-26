package com.mygdx.ethlab.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Align;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.Entity;

/**
 * Created by Eoin on 01/06/2015.
 */
public class SidePanel extends Table {

    public SidePanel(float width, float height, Config config, Skin skin) {
        super();

        setWidth(width);
        setHeight(height);
        align(Align.top);

        //
        //Set the background for the side panel
        //
        setBackgroundColour(this, new Color(0.1f, 0.1f, 0.1f, 1));

        //
        //Toolbar control that is always visible
        //
        final Toolbar toolbar = new Toolbar(config.atlas, skin);
        add(toolbar).expandX().fillX();
        row();
//
        //Scroll table within sidepanel
        //
        Table scrolltable = new Table()
                .align(Align.left | Align.top);

        ScrollPane scroll = new ScrollPane(scrolltable, skin);

        add(scroll)
                .pad(10)
                .expand().fill();

        row();

        Stack modeSelectStack = new Stack();

        //
        //Contents of sidebar (Changes based on which mode is selected)
        //
        final CreateModeTable createModeTable = new CreateModeTable(config, skin);
        final EditModeTable editModeTable = new EditModeTable(config, skin);
        final TriggerModeTable triggerModeTable = new TriggerModeTable(config, skin);

        ChangeListener onClickModeButton = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                createModeTable.setVisible(toolbar.createModeButton.isChecked());
                editModeTable.setVisible(toolbar.editModeButton.isChecked());
                triggerModeTable.setVisible(toolbar.triggerModeButton.isChecked());
            }
        };

        modeSelectStack.add(createModeTable);
        modeSelectStack.add(editModeTable);
        modeSelectStack.add(triggerModeTable);

        toolbar.createModeButton.addListener(onClickModeButton);
        toolbar.editModeButton.addListener(onClickModeButton);
        toolbar.triggerModeButton.addListener(onClickModeButton);

        scrolltable.add(modeSelectStack)
                .expandX()
                .fillX();
    }

    public static void setBackgroundColour(Table table, Color colour) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        pixmap.setColor(colour);
        pixmap.fill();
        Drawable background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        table.setBackground(background);
        pixmap.dispose();
    }

}
