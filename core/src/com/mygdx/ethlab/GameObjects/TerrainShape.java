package com.mygdx.ethlab.GameObjects;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.mygdx.ethlab.UI.SidePanel.IShape2D;

public class TerrainShape extends GameObject implements IShape2D {
    private float points[];
    private PolygonSprite poly;

    public float[] getPoints() {
        return points;
    }

    public void setPoints(float[] points) {
        this.points = points;
        this.poly = null;
    }

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
    public TerrainShape() {
        this.points = new float[0];
    }

    public TerrainShape(String textureName, float points[]) {
        this.textureName = textureName;
        this.points = points;
    }

    public TerrainShape clone() {
        try {
            super.clone();
        } catch(CloneNotSupportedException e){}

        return new TerrainShape(this.textureName, this.points);
    }
}
