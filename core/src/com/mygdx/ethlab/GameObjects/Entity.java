package com.mygdx.ethlab.GameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Eoin on 04/06/2015.
 */
public class Entity extends GameObject {
    public Rectangle boundingBox;
    public float mass;
    public float health;

    public Entity() {
        boundingBox = new Rectangle(0, 0, 20, 20);
        mass = 0.1f;
        health = 1;
    }
}
