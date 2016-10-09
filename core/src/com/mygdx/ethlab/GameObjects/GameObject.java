package com.mygdx.ethlab.GameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.ethlab.Config;

public abstract class GameObject {
    public String textureName;
    public Color colour;
    public Vector2 position;

    public GameObject() {
        textureName = null;
        colour = Color.WHITE;
        position = Vector2.Zero;
    }
}
