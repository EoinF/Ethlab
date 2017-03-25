package com.mygdx.ethlab;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.mygdx.ethlab.GameObjects.TerrainShape;

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

    public void draw(OrthographicCamera camera, Config config) {
        polyBatch.setProjectionMatrix(camera.combined);
        for (TerrainShape shape: shapes) {
            polyBatch.begin();
            TextureRegion reg = config.getTexture(shape.textureName, shape.getClass());
            shape.getSprite(reg).draw(polyBatch);
            polyBatch.end();
        }
    }
}