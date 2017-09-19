package com.mygdx.ethlab.GameObjects;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class TerrainShape extends GameObject implements Json.Serializable {
    private float points[];
    private PolygonRegion polyReg;

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
            for (int i = 0; i < this.points.length / 2; i++) {
                this.points[i*2] += diff.x;
                this.points[i*2 + 1] += diff.y;
            }
            this.polyReg = null;
        }
    }

    @Override
    public Vector2 getPosition() {
        if (points.length > 1) {
            return new Vector2(points[0], points[1]);
        } else {
            return super.getPosition();
        }
    }

    public Rectangle getBoundingBox() {
        Vector2 bottomLeft = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
        Vector2 topRight = new Vector2(Float.MIN_VALUE, Float.MIN_VALUE);

        float currentX, currentY;
        for (int i = 0; i < this.points.length / 2; i++) {
            currentX = points[i * 2];
            currentY = points[i * 2 + 1];
            if (currentX < bottomLeft.x) {
                bottomLeft.x = currentX;
            } else if (currentX > topRight.x) {
                topRight.x = currentX;
            }
            if (currentY < bottomLeft.y) {
                bottomLeft.y = currentY;
            } else if (currentY > topRight.y) {
                topRight.y = currentY;
            }
        }
        return new Rectangle(bottomLeft.x - position.x, bottomLeft.y - position.y,
                topRight.x - bottomLeft.x, topRight.y - bottomLeft.y);
    }

    public void setPoint(int index, float value) {
        if (index == 0) {
            this.position.x = value;
        } else if (index == 1) {
            this.position.y = value;
        }

        this.points[index] = value;
        this.polyReg = null;
    }

    public float[] getPoints() {
        return points;
    }

    public void setPoints(float[] points) {
        if (points == null) {
            this.points = new float[0];
        } else {
            this.points = points;
            if (points.length > 1) {
                this.position = new Vector2(points[0], points[1]);
            }
        }
        this.polyReg = null;
    }

    public void addPoint(float x, float y) {
        float[] newPoints = new float[this.points.length + 2];
        System.arraycopy(this.points, 0, newPoints, 0, this.points.length);
        newPoints[newPoints.length - 2] = x;
        newPoints[newPoints.length - 1] = y;
        this.setPoints(newPoints);
    }

    public PolygonRegion getRegion(TextureRegion textureRegion) {
        //Generate the sprite if it hasn't been generated yet
        if (polyReg == null) {
            EarClippingTriangulator triangulator = new EarClippingTriangulator();

            polyReg = new PolygonRegion(textureRegion,
                    points,
                    triangulator.computeTriangles(points).toArray()
            );
        }

        return polyReg;
    }

    //Default constructor for json deserialization
    public TerrainShape() {
        this.points = new float[0];
    }

    public TerrainShape(String textureName) {
        this();
        if (this.points.length > 1) {
            this.position = new Vector2(this.points[0], this.points[1]);
        }
        this.textureName = textureName;
    }

    public TerrainShape(String textureName, float points[]) {
        this.textureName = textureName;
        setPoints(points);
    }

    public TerrainShape copy() {
        return new TerrainShape(this.textureName, this.points);
    }

    public void write(Json json) {
        // Don't write the poly region, because this is derived from the points
        TerrainShape withoutPolySprite = this.copy();
        withoutPolySprite.polyReg = null;

        json.writeFields(withoutPolySprite);
    }

    public void read(Json json, JsonValue jsonMap) {
        json.readFields(this, jsonMap);
    }
}
