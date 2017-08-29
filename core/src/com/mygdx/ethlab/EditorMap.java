package com.mygdx.ethlab;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.mygdx.ethlab.GameObjects.Entity;
import com.mygdx.ethlab.GameObjects.Item;
import com.mygdx.ethlab.GameObjects.TerrainShape;
import com.mygdx.ethlab.StateManager.EditorState;
import com.mygdx.ethlab.UI.EditorObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EditorMap {
    public List<EditorObject<TerrainShape>> shapes;
    public List<EditorObject<Entity>> entities;
    public List<EditorObject<Item>> items;

    private Map<Integer, EditorObject> editorObjectMap;

    public static EditorMap loadMap(FileHandle file) {
        //Load the map from the given file
        Json json = new Json();

        EditorMap map = json.fromJson(EditorMap.class, file);

        map.editorObjectMap = new HashMap<>();
        for (EditorObject editorObject: map.entities) {
            map.editorObjectMap.put(editorObject.getId(), editorObject);
        }
        return map;
    }

    //Default Constructor for json deserialization
    private EditorMap() {
        items = new ArrayList<EditorObject<Item>>();
        entities = new ArrayList<EditorObject<Entity>>();
    }

    public EditorObject getObjectById(int id) {
        return editorObjectMap.get(id);
    }
    public Set<Integer> getIdList() { return editorObjectMap.keySet(); }

    /**
     * Add an object to the map
     * @param wrapper The wrapped entity to add to the map
     * @return The id of the added entity
     */
    public void addEntity(EditorObject wrapper) {
        entities.add(wrapper);
        addObjectToMap(wrapper);
    }

    /**
     * Update an existing object in the map
     * @param wrapper The wrapped entity to be updated
     * @return The id of the updated entity
     */
    public void updateEntity(EditorObject wrapper) {
        EditorObject existingEntity = entities.stream().
                filter(e -> e.getId() == wrapper.getId())
                .findFirst()
                .get();

        entities.set(entities.indexOf(existingEntity), wrapper);
        addObjectToMap(wrapper);
    }

    /**
     * Add an object to the map
     * @param wrapper The wrapped item to add to the map
     * @return The id of the added item
     */
    public void addItem(EditorObject wrapper) {
        items.add(wrapper);
        addObjectToMap(wrapper);
    }

    private void addObjectToMap(EditorObject wrapper) {
        editorObjectMap.put(wrapper.getId(), wrapper);
    }
}