package com.mygdx.ethlab;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
	private static final FocusListener.FocusEvent FOCUS_LOST_EVENT = new FocusListener.FocusEvent();

	private Config config;
	private Stage uiStage;
	MainView mainView;
	EditorMap map;

	private static final float MAX_ZOOM = 4;
	private static final float MIN_ZOOM = 0.1f;
	private static final float ZOOM_RATE = 0.1f;

	static {
		FOCUS_LOST_EVENT.setFocused(false);
		FOCUS_LOST_EVENT.setType(FocusListener.FocusEvent.Type.keyboard);
	}


	@Override
	public void create() {
		OrthographicCamera camera = new OrthographicCamera(1280, 720);
		config = Config.loadConfig();

		map = EditorMap.loadMap(Gdx.files.internal("levels/1.txt"));

		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

		Stage gameStage = new Stage(new StretchViewport(1280, 720, camera));

		uiStage = new Stage(new StretchViewport(1280, 720));

		mainView = new MainView(camera, gameStage, config);
		SidePanel sidePanel = initUIStage(skin, camera);

		EditorState.init(sidePanel, mainView, map, config);
	}


	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float delta = Gdx.graphics.getDeltaTime();

		mainView.act();
		uiStage.act(delta);

		try {
			mainView.draw();
			uiStage.draw();
		} catch(Exception ex) {
			System.out.println("Fatal: Exception while rendering: " + ex.getMessage());
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void resize(int width, int height) {
		uiStage.getViewport().setScreenSize(width, height);
		mainView.getStage().getViewport().setScreenSize(width, height);
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

	/**
	 * Initializes the ui stage and returns the SidePanel
	 * @param skin ...
	 * @return side panel
     */
	private SidePanel initUIStage(Skin skin, Camera camera) {
		//
		// Side panel
		//
		SidePanel sidePanel = new SidePanel(DEFAULT_SIDEPANEL_WIDTH, uiStage.getHeight(), config, skin);
		uiStage.addActor(sidePanel);
		sidePanel.setPosition(uiStage.getWidth() - sidePanel.getWidth(), 0);

		//
		// Main view
		// (This component is needed to manage what is being focused and drawn inside the game view
		//
		Actor mainViewActor = mainView.getRootActor();
		Stage gameStage = mainView.getStage();
		mainViewActor.setBounds(0, 0, gameStage.getWidth() - sidePanel.getWidth(), gameStage.getHeight());
		mainViewActor.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent e, float x, float y) {
				// Trigger the focus changed listener of the focused actor
				Actor focusedActor = uiStage.getKeyboardFocus();
				if (focusedActor != null) {
					focusedActor.fire(FOCUS_LOST_EVENT);
				}
				// Lose focus of all text fields, etc.
				uiStage.unfocusAll();
				uiStage.setKeyboardFocus(mainViewActor);
				mainView.setIsFocused(true);
			}
		});
		sidePanel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent e, float x, float y) {
				// Lose focus of the main view
				mainView.setIsFocused(false);
			}
		});
		mainViewActor.addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				// This reverts focus to the main view if we aren't focusing on anything
				// therefore we are always focused on something
				if (!focused) {
					mainView.setIsFocused(true);
				}
			}
		});

		uiStage.addActor(mainViewActor);

		// Main view is focused by default
		mainView.setIsFocused(true);
		uiStage.setKeyboardFocus(mainViewActor);

		Gdx.input.setInputProcessor(uiStage);
		uiStage.addListener(new InputListener() {
			public boolean scrolled(InputEvent event, float x, float y, int scrollDirection) {
                if (mainView.getIsFocused()) {
                    OrthographicCamera orthographicCamera = (OrthographicCamera) camera;
                    orthographicCamera.zoom += scrollDirection * ZOOM_RATE;
                    orthographicCamera.zoom = Math.max(MIN_ZOOM, orthographicCamera.zoom);
                    orthographicCamera.zoom = Math.min(MAX_ZOOM, orthographicCamera.zoom);
                    return true;
                }
                return false;
			}
		});

		return sidePanel;
	}
}
