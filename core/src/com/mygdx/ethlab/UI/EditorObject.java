package com.mygdx.ethlab.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.Entity;
import com.mygdx.ethlab.GameObjects.GameObject;
import com.mygdx.ethlab.GameObjects.Item;
import com.mygdx.ethlab.GameObjects.TerrainShape;
import com.mygdx.ethlab.StateManager.EditorState;
import com.mygdx.ethlab.Utils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class EditorObject<T extends GameObject> {
    public T instance;
    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    private String name;
    void setName(String name) {
        this.name = name;
    }
    String getName() {
        return name;
    }

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
            this.name = "entity" + EditorState.peekNextId();
        }
        else if (objectClass == Item.class) {
            this.name = "item" + EditorState.peekNextId();
        }
        else if (objectClass == TerrainShape.class) {
            this.name = "shape" + EditorState.peekNextId();
        } else {
            throw (new NotImplementedException());
        }
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
        this.instance.position = position;
    }

    public void setTexture(String textureName, Config config) {
        this.instance.textureName = textureName;
        if (isAutoBoundingBox) {
            setAutoBoundingBox(config);
        }
    }

    private void setAutoBoundingBox(Config config) {
        if (this.instance.getClass() == Entity.class) {
            Entity entity = ((Entity)this.instance);
            entity.boundingBox = Utils.getBoundingBoxFromTexture(config.getTexture(entity.textureName, Entity.class));
        }
    }
}