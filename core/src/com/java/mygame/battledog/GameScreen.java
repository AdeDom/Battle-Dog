package com.java.mygame.battledog;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {
	private GameWorld world;
	private GameRenderer renderer;
	private SpriteBatch sb;

	public GameScreen() {
		world = new GameWorld();
		renderer = new GameRenderer(world);
		sb = new SpriteBatch();
	}

	public void render(float delta) {
		world.update(delta);
		renderer.render(sb);
	}

	public void resize(int width, int hieght) {}
	public void show() {}
	public void hide() {}
	public void pause() {}
	public void resume() {}
	public void dispose() {}
}
