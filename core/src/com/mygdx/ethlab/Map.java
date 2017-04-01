package com.mygdx.ethlab;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.mygdx.ethlab.GameObjects.GameObject;
import com.mygdx.ethlab.GameObjects.TerrainShape;

public class Map {
    public TerrainShape shapes[];
    public GameObject gameObjects[];

    public static Map loadMap(FileHandle file) {
        //Load the map from the given file
        Json json = new Json();
        return json.fromJson(Map.class, file);
    }

    //Default Constructor for json deserialization
    private Map() {
    }
}