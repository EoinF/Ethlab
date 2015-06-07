package com.mygdx.ethlab.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.EarClippingTriangulator;

/**
 * Created by Eoin on 13/05/2015.
 */
public class TerrainShape {
    public String texture;
    public float points[];

    private PolygonSprite poly;
    public PolygonSprite getSprite() {
        //Generate the sprite if it hasn't been generated yet
        if (poly == null) {
            EarClippingTriangulator triangulator = new EarClippingTriangulator();
            TextureRegion reg = getTexture();
            PolygonRegion polyReg = new PolygonRegion(reg,
                    points,
                    triangulator.computeTriangles(points).toArray()
            );

            poly = new PolygonSprite(polyReg);
            poly.setOrigin(0, 0);
        }
        return poly;
    }

    public TextureRegion getTexture() {
        FileHandle file = Gdx.files.internal("textures/" + texture);
        if (file == null) {
            Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pix.setColor(0xDEADBEFF); // DE is red, AD is green and BE is blue.
            pix.fill();
            return new TextureRegion(new Texture(pix));
        } else {
            Texture shapetex = new Texture(file);
            shapetex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            return new TextureRegion(shapetex);
        }
    }

    //Default constructor for json deserialization
    public TerrainShape() {}

    public TerrainShape(String texname, float points[]) {
        this.texture = texname;
        this.points = points;
    }

}
