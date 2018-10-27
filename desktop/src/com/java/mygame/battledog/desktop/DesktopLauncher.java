package com.java.mygame.battledog.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.java.mygame.battledog.BattleDog;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = BattleDog.WIDTH;
		config.height = BattleDog.HEIGHT;
		config.title = BattleDog.TITLE;
		
		new LwjglApplication(new BattleDog(),config);
	}
}
