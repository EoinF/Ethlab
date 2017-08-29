package com.mygdx.ethlab.UI.MainView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.ethlab.Config;
import com.mygdx.ethlab.GameObjects.Entity;
import com.mygdx.ethlab.GameObjects.GameObject;
import com.mygdx.ethlab.GameObjects.Item;
import com.mygdx.ethlab.GameObjects.TerrainShape;
import com.mygdx.ethlab.StateManager.CommandFactory;
import com.mygdx.ethlab.StateManager.EditorState;
import com.mygdx.ethlab.StateManager.ModeType;
import com.mygdx.ethlab.UI.EditorObject;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.omg.CORBA.CODESET_INCOMPATIBLE;

/**
 The main view is the part of the screen which holds the game world and objects/entities (i.e. the game stage)
 */
public class MainView {

    static final float DEFAULT_CAMERA_SPEED = 5f;
    private PolygonSpriteBatch polyBatch;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    private boolean isFocused;
    public void setIsFocused(boolean isFocused) {
        if (this.isFocused != isFocused) {
            this.isFocused = isFocused;

            Color tint = isFocused ? Color.WHITE : Color.BLACK;

            polyBatch.setColor(tint);
            spriteBatch.setColor(tint);
            for (Integer id : gameObjectMap.keySet()) {
                Color interpolatedColour = spriteColourMap.get(id).cpy().lerp(tint, 0.5f);
                gameObjectMap.get(id).setColor(interpolatedColour);
            }
        }
    }

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
    private Map<Integer, PolygonSprite> gameShapeMap;
    private Map<Integer, Color> spriteColourMap;

    private Sprite focusedObjectSprite;
    private Rectangle focusedObjectBoundingBox;
    private Integer focusedObjectID;


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
        this.spriteColourMap = new HashMap<>();


        rootActor.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (EditorState.isMode(ModeType.CREATE)) {
                    actCreateMode(x, y);
                } else if (EditorState.isMode(ModeType.EDIT)) {
                    actEditMode(x, y);
                }
                return true;
            }
        });
    }

    public void act() {
        gameStage.act();
        updateCameraInput();
    }

    /**
     * Perform actions when the mouse is clicked in create mode
     * @param x Mouse x coordinate
     * @param y Mouse y coordinate
     */
    private void actCreateMode(float x, float y) {
        if (isFocused) {
            EditorObject focusedObject = EditorState.getObjectById(focusedObjectID);
            Entity focusedEntity = ((Entity) focusedObject.instance);

            Vector3 mousePositionInWorld = camera.unproject(new Vector3(x, rootActor.getHeight() - y, 0));

            focusedObject.instance.position = new Vector2(
                    mousePositionInWorld.x - focusedEntity.boundingBox.getWidth() / 2,
                    mousePositionInWorld.y - focusedEntity.boundingBox.getHeight() / 2
            );
            EditorState.performAction(
                    CommandFactory.addNewObject(focusedObjectID, focusedObject, false)
            );
            EditorObject updatedWrapper = EditorState.incrementFocusedObject();
            setFocusedObject(updatedWrapper);
        }
    }

    /**
     * Perform actions when the mouse is clicked in edit mode
     * @param x Mouse x coordinate
     * @param y Mouse y coordinate
     */
    private void actEditMode(float x, float y) {
        Vector3 mousePositionInWorld = camera.unproject(new Vector3(x, rootActor.getHeight() - y, 0));
        Actor selectedActor = gameStage.hit(mousePositionInWorld.x, mousePositionInWorld.y, false);

        if (selectedActor != null) {
            for (Integer id : gameObjectMap.keySet()) {
                if (gameObjectMap.get(id).equals(selectedActor)) {
                    EditorState.setFocusedObject(id);
                    EditorState.setEditModeObject(id);
                    break;
                }
            }
        } else {
            EditorState.setFocusedObject(null);
            EditorState.setEditModeObject(null);
        }
    }

    public void setFocusedObject(EditorObject wrapper) {
        if (wrapper != null) {
            GameObject gameObject = wrapper.instance;
            TextureRegion reg = config.getTexture(gameObject.textureName, gameObject.getClass());

            focusedObjectSprite = new Sprite(reg);
            focusedObjectSprite.setPosition(gameObject.position.x, gameObject.position.y);
            focusedObjectSprite.setColor(gameObject.colour);
            focusedObjectID = wrapper.getId();

            if (gameObject.getClass() == Entity.class) {
                focusedObjectBoundingBox = ((Entity) gameObject).boundingBox;
            } else {
                float width = focusedObjectSprite.getWidth();
                float height = focusedObjectSprite.getHeight();
                focusedObjectBoundingBox = new Rectangle(0, 0, width, height);
            }
        } else {
            focusedObjectSprite = null;
            focusedObjectID = -1;
        }
    }

    private Image createGameObjectImage(GameObject gameObject) {
        TextureRegion reg = config.getTexture(gameObject.textureName, gameObject.getClass());
        Image image = new Image(new Sprite(reg));
        image.setPosition(gameObject.position.x, gameObject.position.y);
        image.setColor(gameObject.colour);
        return image;
    }

    public void setEntities(List<EditorObject<Entity>> entities) {
        entities.forEach(this::addGameObject);
    }

    public void setItems(List<EditorObject<Item>> items) {
        items.forEach(this::addGameObject);
    }

    public void addGameObject(EditorObject wrapper) {
        Image gameObjectImage = createGameObjectImage(wrapper.instance);
        gameObjectMap.put(wrapper.getId(), gameObjectImage);
        gameStage.addActor(gameObjectImage);
        spriteColourMap.put(wrapper.getId(), wrapper.instance.colour);
    }

    public void updateGameObject(EditorObject wrapper) {
        // Remove the actor from the stage so it's no longer rendered
        gameObjectMap.get(wrapper.getId()).remove();

        Image updatedImage = createGameObjectImage(wrapper.instance);
        gameObjectMap.replace(wrapper.getId(), updatedImage);
        gameStage.addActor(updatedImage);
    }

    public void removeGameObject(EditorObject wrapper) {
        // Remove the object from the map and then from the game stage
        gameObjectMap.remove(wrapper.getId()).remove();
    }

    public void setShapes(List<EditorObject<TerrainShape>> gameShapes) {
        gameShapes.forEach(this::addShape);
    }

    private void addShape(EditorObject wrapper) {
        TerrainShape shape = (TerrainShape)wrapper.instance;
        TextureRegion reg = config.getTexture(shape.textureName, shape.getClass());
        gameShapeMap.put(wrapper.getId(), shape.getSprite(reg));
        spriteColourMap.put(wrapper.getId(), wrapper.instance.colour);
    }

    public void draw() throws Exception {
        gameStage.draw();

        polyBatch.setProjectionMatrix(camera.combined);
        polyBatch.begin();
        for (PolygonSprite shapeSprite: gameShapeMap.values()) {
            shapeSprite.draw(polyBatch);
        }
        polyBatch.end();


        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        Vector2 focusedObjectPosition = null;
        // We have a special case for drawing the focused object because we want to retain its original position
        // if the user cancels movement and wants to restore it back to its previous state

        if (focusedObjectSprite != null) {
            if (EditorState.isMode(ModeType.CREATE)) {
                Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

                Vector2 originalPosition = new Vector2(focusedObjectSprite.getX(), focusedObjectSprite.getY());
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
            } else if (EditorState.isMode(ModeType.EDIT)) {
                focusedObjectPosition = new Vector2(focusedObjectSprite.getX(), focusedObjectSprite.getY());
            }
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
            float zoom = camera.zoom * camera.zoom * 2;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                camera.translate(-(DEFAULT_CAMERA_SPEED + zoom), 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                camera.translate(DEFAULT_CAMERA_SPEED + zoom, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                camera.translate(0, DEFAULT_CAMERA_SPEED + zoom);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                camera.translate(0, -(DEFAULT_CAMERA_SPEED + zoom));
            }
            camera.update();
        }
    }
}
