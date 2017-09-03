package com.mygdx.ethlab.GameObjects;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.ethlab.UI.SidePanel.IShape2D;

public class TerrainShape extends GameObject implements IShape2D, Json.Serializable {
    private float points[];
    private PolygonSprite poly;

    /**
     * Set the position of the shape ( Always based on the first vertex )
     * This also offsets the position of every other point by the change in the first vertex
     * @param newPosition The new starting vertex position of the shape
     */
    @Override
    public void setPosition(Vector2 newPosition) {
        super.setPosition(newPosition);

        if (points.length == 0) {
            setPoints(new float[] { newPosition.x, newPosition.y });
        } else {
            Vector2 diff = newPosition.sub(new Vector2(this.points[0], this.points[1]));
            for (int i = 0; i < points.length / 2; i++) {
                points[i*2] += diff.x;
                points[i*2 + 1] += diff.y;
            }
        }
    }

    public float[] getPoints() {
        return points;
    }

    public void setPoints(float[] points) {
        this.points = points;
        this.poly = null;
    }

    public void addPoint(float x, float y) {
        float[] newPoints = new float[this.points.length + 2];
        System.arraycopy(this.points, 0, newPoints, 0, this.points.length);
        newPoints[newPoints.length - 2] = x;
        newPoints[newPoints.length - 1] = y;
        this.setPoints(newPoints);
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

    public TerrainShape copy() {
        return new TerrainShape(this.textureName, this.points);
    }

    public void write(Json json) {
        // Don't write the poly sprite, because this is derived from the points
        TerrainShape withoutPolySprite = this.copy();
        withoutPolySprite.poly = null;

        json.writeFields(withoutPolySprite);
    }

    public void read(Json json, JsonValue jsonMap) {
        json.readFields(this, jsonMap);
    }
}
