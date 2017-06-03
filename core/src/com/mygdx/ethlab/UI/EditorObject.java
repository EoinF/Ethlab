package com.mygdx.ethlab.UI;

import com.mygdx.ethlab.GameObjects.Entity;
import com.mygdx.ethlab.GameObjects.GameObject;
import com.mygdx.ethlab.GameObjects.Item;
import com.mygdx.ethlab.StateManager.EditorState;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class EditorObject<T extends GameObject> {
    public T instance;
    private int id;
    public int getId() {
        return id;
    }

    private String name;
    void setName(String name) {
        this.name = name;
    }
    String getName() {
        return name;
    }

    public EditorObject() {
        this.id = EditorState.getNextIdAndIncrement();
    }

    public EditorObject(T gameObject) {
        this();

        Class objectClass = gameObject.getClass();
        this.instance = gameObject;

        if (objectClass == Entity.class) {
            this.name = "entity" + EditorState.peekNextId();
        }
        else if (objectClass == Item.class) {
            this.name = "item" + EditorState.peekNextId();
        } else {
            throw (new NotImplementedException());
        }
    }

}