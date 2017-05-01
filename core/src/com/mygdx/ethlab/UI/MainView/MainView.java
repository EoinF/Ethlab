package com.mygdx.ethlab.UI.MainView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.Entity;
import com.mygdx.ethlab.GameObjects.GameObject;
import com.mygdx.ethlab.GameObjects.TerrainShape;
import com.mygdx.ethlab.StateManager.CommandFactory;
import com.mygdx.ethlab.StateManager.EditorState;
import com.mygdx.ethlab.StateManager.ModeType;

/**
 The main view is the part of the screen which holds the game world and objects/entities (i.e. the game stage)
 */
public class MainView {

    static final float DEFAULT_CAMERA_SPEED = 5f;
    private PolygonSpriteBatch polyBatch;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    public boolean isFocused;
    private OrthographicCamera camera;
    private Config config;

    private Stage gameStage;
    private Actor rootActor;
    public Actor getRootActor() {
        return rootActor;
    }

    public Stage getStage() {
        return gameStage;
    }

    private Map<Integer, Actor> gameObjectMap;
    private Map<Integer, Actor> gameShapeMap;

    private Sprite focusedObjectSprite;
    private Rectangle focusedObjectBoundingBox;
    private int focusedObjectID;


    public MainView(OrthographicCamera camera, Stage gameStage, Config config) {
        super();
        this.camera = camera;
        this.config = config;

        this.shapeRenderer = new ShapeRenderer();
        this.polyBatch = new PolygonSpriteBatch();
        this.spriteBatch = new SpriteBatch();

        this.gameStage = gameStage;
        this.rootActor = new Actor();

        this.gameObjectMap = new HashMap<>();
        this.gameShapeMap = new HashMap<>();
    }

    public void act() {
        gameStage.act();
        if (EditorState.isMode(ModeType.CREATE)) {
            actCreateMode();
        }

        updateCameraInput();
    }

    private void actCreateMode() {
        Vector2 mousePosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        if (Gdx.input.isTouched() && rootActor.hit(mousePosition.x, mousePosition.y, true) != null) {
            GameObject focusedObject = EditorState.getObjectById(focusedObjectID);
            focusedObject.position = mousePosition;
            EditorState.performAction(
                    CommandFactory.addNewObject(focusedObjectID, focusedObject, false)
            );
            EditorState.setMode(ModeType.CREATED);
        }
    }

    public void setFocusedObject(GameObject gameObject) {
        TextureRegion reg = config.getTexture(gameObject.textureName, gameObject.getClass());
        focusedObjectSprite = new Sprite(reg);
        focusedObjectSprite.setPosition(gameObject.position.x, gameObject.position.y);
        focusedObjectID = gameObject.id;

        if (gameObject.getClass() == Entity.class) {
            focusedObjectBoundingBox = ((Entity)gameObject).boundingBox;
        } else {
            float width = focusedObjectSprite.getWidth();
            float height = focusedObjectSprite.getHeight();
            focusedObjectBoundingBox = new Rectangle(0, 0, width, height);
        }
    }

    private Actor createGameObjectActor(GameObject gameObject) {
        TextureRegion reg = config.getTexture(gameObject.textureName, gameObject.getClass());
        return new Image(reg);
    }

    public void setGameObjects(List<GameObject> gameObjects) {
        for (GameObject gameObject: gameObjects) {
            Actor gameObjectActor = createGameObjectActor(gameObject);
            gameObjectMap.put(gameObject.id, gameObjectActor);
            Table table = new Table();
            table.setWidth(100);
            table.setHeight(200);
            table.debug();
            table.setPosition(0, 0);
            table.add(gameObjectActor);
            gameStage.addActor(table);
        }
        gameStage.setDebugAll(true);
    }

    public void updateGameObject(GameObject gameObject) {
        gameObjectMap.replace(gameObject.id, createGameObjectActor(gameObject));
    }

    public void removeGameObject(GameObject gameObject) {

    }

    public void setShapes(TerrainShape[] gameShapes) {
        for (TerrainShape shape: gameShapes) {
            polyBatch.begin();
            TextureRegion reg = config.getTexture(shape.textureName, shape.getClass());
            shape.getSprite(reg).draw(polyBatch);
            polyBatch.end();
        }
    }

    public void draw() throws Exception {
        gameStage.draw();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        Vector2 focusedObjectPosition = null;
        // We have a special case for drawing the focused object because we want to retain its original position
        // if the user cancels movement and wants to restore it back to its previous state
        if (focusedObjectSprite != null) {
            Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

            Vector2 originalPosition = new Vector2(focusedObjectSprite.getX(), focusedObjectSprite.getY() );
            focusedObjectPosition = originalPosition;
            if (this.rootActor.hit(mousePosition.x, mousePosition.y, false) != null) {
                camera.unproject(mousePosition);
                focusedObjectPosition = new Vector2(
                        mousePosition.x - focusedObjectSprite.getRegionWidth() / 2,
                        mousePosition.y - focusedObjectSprite.getRegionHeight() / 2
                );
                focusedObjectSprite.setPosition(focusedObjectPosition.x, focusedObjectPosition.y);
            }

            focusedObjectSprite.draw(spriteBatch);

            // Restore it back to its original position after drawing
            focusedObjectSprite.setPosition(originalPosition.x, originalPosition.y);
        }
        spriteBatch.end();

        if (focusedObjectPosition != null) {
            drawBoundingBox(focusedObjectPosition.x, focusedObjectPosition.y, focusedObjectBoundingBox);
        }
    }

    private void drawBoundingBox(float positionX, float positionY, Rectangle boundingBox) throws Exception {
        if (spriteBatch.isDrawing()) {
            spriteBatch.end();
            throw new Exception("Attempted to interrupt drawing of sprite batch. " +
                    "Make sure to call end() before starting a new batch");
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setColor(generateColour());
        Vector2 rectStart = Vector2.Zero;
        boundingBox.getPosition(rectStart);

        Vector2 bottomLeft = new Vector2(positionX + rectStart.x, positionY + rectStart.y);
        Vector2 bottomRight = new Vector2(bottomLeft.x + boundingBox.getWidth(), bottomLeft.y);
        Vector2 topLeft = new Vector2(bottomLeft.x, bottomLeft.y + boundingBox.getHeight());
        Vector2 topRight = new Vector2(bottomRight.x, topLeft.y);

        shapeRenderer.line(topLeft.x, topLeft.y, topRight.x, topRight.y);
        shapeRenderer.line(topRight.x, topRight.y, bottomRight.x, bottomRight.y);
        shapeRenderer.line(bottomRight.x, bottomRight.y, bottomLeft.x, bottomLeft.y);
        shapeRenderer.line(bottomLeft.x, bottomLeft.y, topLeft.x, topLeft.y);

        shapeRenderer.end();
    }

    private float colourGenerator = 10;
    private int direction = 1;
    private Color generateColour() {
        colourGenerator += direction;
        if (colourGenerator >= 60 || colourGenerator <= 10) {
            direction = -direction;
        }
        float frame = colourGenerator / 60f;

        return new Color(0.5f + frame, 0.5f + (0.5f - frame), 0.5f + frame, 0.9f);
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
