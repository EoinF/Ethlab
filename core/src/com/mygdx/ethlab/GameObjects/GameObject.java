package com.mygdx.ethlab.GameObjects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.StateManager.EditorState;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class GameObject {

    public static final Color DEFAULT_COLOUR = Color.WHITE;

    public String textureName;
    public Color colour;
    public Vector2 position;

    public GameObject() {
        textureName = null;
        colour = DEFAULT_COLOUR;
        position = Vector2.Zero;
    }

    public GameObject(String textureName, Color colour, Vector2 position) {
        this.textureName = textureName;
        this.colour = colour;
        this.position = position;
    }

    public GameObject copy() {
        Class objectClass = this.getClass();

        if (objectClass == Entity.class) {
            Entity entity = (Entity)this;
            return new Entity(entity.textureName, entity.colour, entity.position, entity.boundingBox, entity.mass,
                    entity.health, entity.ai);
        }
        else if (objectClass == Item.class) {
            Item item = (Item)this;
            return new Item();
        }
        else if (objectClass == TerrainShape.class) {
            TerrainShape shape = (TerrainShape) this;
            return new TerrainShape(shape.textureName, shape.getPoints());
        } else {
            throw (new NotImplementedException());
        }
    }
}
