package com.java.mygame.battledog;

import com.badlogic.gdx.Game;

public class BattleDog extends Game {
	public static final String TITLE = "Battle Dog";
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 800;
	public static final int MID_SCREEN = WIDTH / 2;
	public static Content res;

	public void create() {
		res = new Content();
		res.loadAtlas("BattleDog.pack", "pack");

		setScreen(new GameScreen());
	}
}
