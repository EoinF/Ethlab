package com.mygdx.ethlab.GameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.StateManager.EditorState;

public abstract class GameObject {

    public static final Color DEFAULT_COLOUR = Color.WHITE;

    public String textureName;
    public Color colour;
    public Vector2 position;
    public int id;

    public GameObject() {
        textureName = null;
        colour = DEFAULT_COLOUR;
        position = Vector2.Zero;
        id = EditorState.getNextId();
    }

    public GameObject(String textureName, Color colour, Vector2 position) {
        this.textureName = textureName;
        this.colour = colour;
        this.position = position;
        id = EditorState.getNextId();
    }
}
