package com.mygdx.ethlab.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.*;
import com.mygdx.ethlab.StateManager.EditorState;
import com.mygdx.ethlab.Utils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class EditorObject<T extends GameObject> {
    public T instance;
    private int id;
    public int getId() {
        return id;
    }

    private String name;

    private boolean isAutoBoundingBox;

    public EditorObject() {
        this.id = EditorState.getNextIdAndIncrement();
        this.isAutoBoundingBox = false;
    }

    public EditorObject(T gameObject) {
        this();

        Class objectClass = gameObject.getClass();
        this.instance = gameObject;

        if (objectClass == Entity.class) {
            this.name = "entity" + this.id;
        }
        else if (objectClass == Item.class) {
            this.name = "item" + this.id;
        }
        else if (objectClass == TerrainShape.class) {
            this.name = "shape" + this.id;
            this.isAutoBoundingBox = true;
        } else if (objectClass == Prop.class) {
            this.name = "prop" + this.id;
        } else {
            throw (new NotImplementedException());
        }
    }

    public EditorObject(EditorObject<T> objectToCopy, Config config) {
        this((T)objectToCopy.instance.copy(), objectToCopy.isAutoBoundingBox, config);
    }

    public EditorObject(T gameObject, boolean isAutoBoundingBox, Config config) {
        this(gameObject);

        this.isAutoBoundingBox = isAutoBoundingBox;
        if (this.isAutoBoundingBox) {
            setAutoBoundingBox(config);
        }
    }

    public void setColour(Color newColour) {
        this.instance.colour = newColour;
    }

    public void setPosition(Vector2 position) {
        this.instance.setPosition(position);
    }

    public void setTexture(String textureName, Config config) {
        this.instance.textureName = textureName;
        if (isAutoBoundingBox) {
            setAutoBoundingBox(config);
        }
    }

    private void setAutoBoundingBox(Config config) {
        if (this.instance instanceof Entity) {
            Entity entity = ((Entity)this.instance);
            entity.boundingBox = Utils.getBoundingBoxFromTexture(config.getTexture(entity.textureName, Entity.class));
        }
    }
}