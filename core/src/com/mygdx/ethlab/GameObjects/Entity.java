package com.mygdx.ethlab.GameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Eoin on 04/06/2015.
 */
public class Entity extends GameObject {
    public static final float DEFAULT_MASS = 1;
    public static final float DEFAULT_HEALTH = 1;
    public static final Rectangle DEFAULT_BOUNDING_BOX = new Rectangle(0, 0, 20, 20);

    public Rectangle boundingBox;
    public float mass;
    public float health;
    public AIType ai;

    public Entity() {
        super();
        boundingBox = DEFAULT_BOUNDING_BOX;
        mass = DEFAULT_MASS;
        health = DEFAULT_HEALTH;
        ai = AIType.NONE;
    }
}
