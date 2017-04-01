package com.mygdx.ethlab;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.ethlab.StateManager.EditorState;
import com.mygdx.ethlab.UI.MainView.MainView;
import com.mygdx.ethlab.UI.SidePanel.SidePanel;

public class EthLab extends ApplicationAdapter {

	public static final float DEFAULT_SIDEPANEL_WIDTH = 250;

	private Config config;
	private Stage uiStage;
	private Stage gameStage;
	MainView mainView;

	@Override
	public void create() {
		OrthographicCamera camera = new OrthographicCamera(1280, 720);
		config = Config.loadConfig();

		mainView = new MainView(camera, Map.loadMap(Gdx.files.internal("levels/1.txt")), config);

		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		gameStage = new Stage(new StretchViewport(1280, 720, camera));
		uiStage = new Stage(new StretchViewport(1280, 720));

		initUIStage(skin);
	}


	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float delta = Gdx.graphics.getDeltaTime();

		mainView.update();

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

	private void initUIStage(Skin skin) {
		//
		// Side panel
		//
		SidePanel sidePanel = new SidePanel(DEFAULT_SIDEPANEL_WIDTH, uiStage.getHeight(), config, skin);
		uiStage.addActor(sidePanel);
		sidePanel.setPosition(uiStage.getWidth() - sidePanel.getWidth(), 0);

		//
		// Main view
		// (This component is only needed to manage what is being focused. gameStage manages what's drawn inside the main view)
		//
		mainView.setBounds(0, 0, gameStage.getWidth() - sidePanel.getWidth(), gameStage.getHeight());
		mainView.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent e, float x, float y) {
				// Lose focus of all text fields, etc.
				uiStage.unfocusAll();
				uiStage.setKeyboardFocus(mainView);
				mainView.isFocused = true;
			}
		});
		mainView.addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				// This reverts focus to the main view if we aren't focusing on anything
				// therefore we are always focused on something
				if (!focused) {
					mainView.isFocused = true;
				}
			}
		});
		uiStage.addActor(mainView);

		// Main view is focused by default
		mainView.isFocused = true;
		uiStage.setKeyboardFocus(mainView);

		Gdx.input.setInputProcessor(uiStage);
	}
}
