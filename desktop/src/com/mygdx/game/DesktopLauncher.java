package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.InfiniteGolf;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(1024, 683); //2:3 aspect ratio instead of his 3:4
		config.useVsync(true);
		config.setTitle("InfiniteGolf");
		config.setResizable(false);
		new Lwjgl3Application(new InfiniteGolf(), config);
	}
}
