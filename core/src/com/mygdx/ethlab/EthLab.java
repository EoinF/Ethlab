package com.mygdx.ethlab;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.ethlab.UI.SidePanel;

public class EthLab extends ApplicationAdapter {

	private Config config;
	private Stage uiStage;
	private Stage gameStage;
	Map map;
	OrthographicCamera camera;
	static final float DEFAULT_CAMERA_SPEED = 5f;
	public static final float DEFAULT_SIDEPANEL_WIDTH = 250;

	@Override
	public void create() {
		camera = new OrthographicCamera(1280, 720);
		config = Config.loadConfig();

		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		gameStage = new Stage(new StretchViewport(1280, 720, camera));
		uiStage = new Stage(new StretchViewport(1280, 720));

		showUI(skin);
		map = Map.loadMap(Gdx.files.internal("levels/1.txt"));
	}


	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float delta = Gdx.graphics.getDeltaTime();

		updateCameraInput();

		map.draw(camera);
		gameStage.act(delta);
		uiStage.act(delta);

		gameStage.draw();
		uiStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		uiStage.getViewport().setScreenSize(width, height);
		gameStage.getViewport().setScreenSize(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		config.dispose();
	}

	public void showUI(Skin skin) {
		//
		//Side panel
		//
		SidePanel sidePanel = new SidePanel(DEFAULT_SIDEPANEL_WIDTH, uiStage.getHeight(), config, skin);
		uiStage.addActor(sidePanel);
		sidePanel.setPosition(uiStage.getWidth() - sidePanel.getWidth(), 0);

		Gdx.input.setInputProcessor(uiStage);
	}


	public void updateCameraInput() {
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
