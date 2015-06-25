package com.mygdx.ethlab.GameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Eoin on 04/06/2015.
 */
public class GameObject {
    public String textureName;
    public Color colour;
    public Vector2 position;

    public GameObject() {
        textureName = null;
        colour = Color.WHITE;
        position = Vector2.Zero;
    }
}
