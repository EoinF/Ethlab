package com.mygdx.ethlab;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.mygdx.ethlab.GameObjects.TerrainShape;

/**
 * Created by Eoin on 13/05/2015.
 */
public class Map {
    public TerrainShape shapes[];
    private PolygonSpriteBatch polyBatch;


    public static Map loadMap(FileHandle file) {
        //Load the map from the given file
        Json json = new Json();
        return json.fromJson(Map.class, file);
    }


    //Default Constructor for json deserialization
    private Map() {
        polyBatch = new PolygonSpriteBatch();
    }

    public void draw(OrthographicCamera camera) {
        polyBatch.setProjectionMatrix(camera.combined);
        for (TerrainShape shape: shapes) {
            polyBatch.begin();
            shape.getSprite().draw(polyBatch);
            polyBatch.end();
        }
    }
}