package com.mygdx.ethlab;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.github.eoinf.ethanolshared.GameObjects.Entity;
import com.github.eoinf.ethanolshared.GameObjects.Item;
import com.github.eoinf.ethanolshared.GameObjects.TerrainShape;
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

        map.entities.forEach(
                e -> map.editorObjectMap.put(e.getId(), e));
        map.items.forEach(
                i -> map.editorObjectMap.put(i.getId(), i));
        map.shapes.forEach(
                s -> map.editorObjectMap.put(s.getId(), s));

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

    public void addObject(EditorObject wrapper) {
        if (wrapper.instance instanceof Entity) {
            addEntity(wrapper);
        } else if(wrapper.instance instanceof TerrainShape) {
            addShape(wrapper);
        }
    }

    /**
     * Add an entity to the map
     * @param wrapper The wrapped entity to add to the map
     */
    public void addEntity(EditorObject wrapper) {
        entities.add(wrapper);
        addObjectToMap(wrapper);
    }

    /**
     * Add a shape to the map
     * @param wrapper The wrapped shape to add to the map
     */
    public void addShape(EditorObject wrapper) {
        shapes.add(wrapper);
        addObjectToMap(wrapper);
    }

    public void removeObject(EditorObject wrapper) {
        if (wrapper.instance instanceof Entity) {
            removeEntity(wrapper);
        } else if(wrapper.instance instanceof TerrainShape) {
            removeShape(wrapper);
        }
    }

    /**
     * Removes an object from the map
     * @param wrapper The wrapped entity to be removed to the map
     */
    public void removeEntity(EditorObject wrapper) {
        entities.remove(wrapper);
        removeObjectFromMap(wrapper);
    }

    /**
     * Removes an object from the map
     * @param wrapper The wrapped entity to be removed to the map
     */
    public void removeShape(EditorObject wrapper) {
        shapes.remove(wrapper);
        removeObjectFromMap(wrapper);
    }

    /**
     * Update an existing object in the map
     * @param wrapper The wrapped entity to be updated
     */
    public void updateEntity(EditorObject wrapper) {
        if (getIdList().contains(wrapper.getId())) {
            if (wrapper.instance instanceof Entity) {
                EditorObject existingEntity = entities.stream().
                        filter(e -> e.getId() == wrapper.getId())
                        .findFirst()
                        .get();

                entities.set(entities.indexOf(existingEntity), wrapper);
                addObjectToMap(wrapper);
            } else if (wrapper.instance instanceof TerrainShape) {
                EditorObject existingShape = shapes.stream().
                        filter(e -> e.getId() == wrapper.getId())
                        .findFirst()
                        .get();

                shapes.set(shapes.indexOf(existingShape), wrapper);
                addObjectToMap(wrapper);
            }
        }
    }

    /**
     * Add an object to the map
     * @param wrapper The wrapped item to add to the map
     */
    public void addItem(EditorObject wrapper) {
        items.add(wrapper);
        addObjectToMap(wrapper);
    }

    private void addObjectToMap(EditorObject wrapper) {
        editorObjectMap.put(wrapper.getId(), wrapper);
    }
    private void removeObjectFromMap(EditorObject wrapper) {
        editorObjectMap.remove(wrapper.getId());
    }
}