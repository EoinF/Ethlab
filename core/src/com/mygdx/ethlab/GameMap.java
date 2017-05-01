package com.mygdx.ethlab;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.mygdx.ethlab.GameObjects.GameObject;
import com.mygdx.ethlab.GameObjects.TerrainShape;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMap {
    public TerrainShape shapes[];
    public List<GameObject> gameObjects;

    private Map<Integer, GameObject> gameObjectsMap;

    public static GameMap loadMap(FileHandle file) {
        //Load the map from the given file
        Json json = new Json();
        GameMap map = json.fromJson(GameMap.class, file);

        map.gameObjectsMap = new HashMap<>();
        for (GameObject gameObject: map.gameObjects) {
            map.gameObjectsMap.put(gameObject.id, gameObject);
        }
        return map;
    }

    //Default Constructor for json deserialization
    private GameMap() {
    }

    public GameObject getObjectById(int id) {
        return gameObjectsMap.get(id);
    }

    public void addObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        gameObjectsMap.put(gameObject.id, gameObject);
    }
}