package com.mygdx.ethlab.UI.MainView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.GameObject;
import com.mygdx.ethlab.GameObjects.TerrainShape;
import com.mygdx.ethlab.Map;
import com.mygdx.ethlab.StateManager.EditorState;

/**
 The main view is the part of the screen which holds the game world and objects/entities (i.e. the game stage)
 */
public class MainView extends Actor {

    static final float DEFAULT_CAMERA_SPEED = 5f;
    private PolygonSpriteBatch polyBatch;
    private SpriteBatch spriteBatch;

    Map map;

    public boolean isFocused;
    private OrthographicCamera camera;
    private Config config;


    public MainView(OrthographicCamera camera, Map map, Config config) {
        super();
        this.camera = camera;
        this.map = map;
        this.config = config;

        this.polyBatch = new PolygonSpriteBatch();
        this.spriteBatch = new SpriteBatch();
    }

    public void update() {
        updateCameraInput();
        draw(camera, config);
    }

    public void draw(OrthographicCamera camera, Config config) {
        GameObject focusedObject = EditorState.getFocusedObject();

        polyBatch.setProjectionMatrix(camera.combined);
        for (TerrainShape shape: map.shapes) {
            polyBatch.begin();
            TextureRegion reg = config.getTexture(shape.textureName, shape.getClass());
            shape.getSprite(reg).draw(polyBatch);
            polyBatch.end();
        }

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (GameObject gameObject: map.gameObjects) {
            // We draw the focused object separately
            if (gameObject != focusedObject) {
                TextureRegion reg = config.getTexture(gameObject.textureName, gameObject.getClass());
                spriteBatch.draw(reg, gameObject.position.x, gameObject.position.y);
            }
        }

        // We have a special case for drawing the focused object because we want to retain its original position
        // if the user cancels movement and wants to restore it back to its previous state
        if (focusedObject != null) {
            Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePosition);
            TextureRegion reg = config.getTexture(focusedObject.textureName, focusedObject.getClass());
            spriteBatch.draw(reg, mousePosition.x - reg.getRegionWidth() / 2, mousePosition.y - reg.getRegionHeight() / 2);
        }

        spriteBatch.end();
    }


    public void updateCameraInput() {
        if (isFocused) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                camera.translate(-DEFAULT_CAMERA_SPEED, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                camera.translate(DEFAULT_CAMERA_SPEED, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                camera.translate(0, DEFAULT_CAMERA_SPEED);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                camera.translate(0, -DEFAULT_CAMERA_SPEED);
            }
            camera.update();
        }
    }
}
