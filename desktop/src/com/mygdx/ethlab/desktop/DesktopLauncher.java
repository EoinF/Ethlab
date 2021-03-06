package com.mygdx.ethlab.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.ethlab.EthLab;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "EthLab";
		config.width = 1280;
		config.height = 720;
		config.fullscreen = true;
		new LwjglApplication(new EthLab(), config);
	}
}
