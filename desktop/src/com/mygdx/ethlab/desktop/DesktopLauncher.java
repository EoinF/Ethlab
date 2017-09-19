package com.mygdx.ethlab.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.mygdx.ethlab.EthLab;

public class DesktopLauncher {
	public static void main (String[] arg) {
		TexturePacker.Settings settings = new TexturePacker.Settings();
		TexturePacker.process(settings, "../../images", "../../core/assets", "game");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "EthLab";
		config.width = 1280;
		config.height = 720;
		config.fullscreen = true;
		new LwjglApplication(new EthLab(), config);
	}
}
