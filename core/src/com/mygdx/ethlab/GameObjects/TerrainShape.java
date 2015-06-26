package com.mygdx.ethlab.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.mygdx.ethlab.Config;

/**
 * Created by Eoin on 13/05/2015.
 */
public class TerrainShape extends GameObject {
    public float points[];

    private PolygonSprite poly;
    public PolygonSprite getSprite(TextureRegion textureRegion) {
        //Generate the sprite if it hasn't been generated yet
        if (poly == null) {
            EarClippingTriangulator triangulator = new EarClippingTriangulator();

            PolygonRegion polyReg = new PolygonRegion(textureRegion,
                    points,
                    triangulator.computeTriangles(points).toArray()
            );

            poly = new PolygonSprite(polyReg);
            poly.setOrigin(0, 0);
        }
        return poly;
    }

    //Default constructor for json deserialization
    public TerrainShape() {}

    public TerrainShape(String texname, float points[]) {
        this.textureName = texname;
        this.points = points;
    }

}
