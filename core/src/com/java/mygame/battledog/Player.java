package com.java.mygame.battledog;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class Player {
	protected TextureRegion player;
	protected int x;
	protected int y;
	protected int width;
	protected int height;

	protected Rectangle boundedPlayer;

	public Player() {}

	public Player(int x, int y) {
		this.x = x;
		this.y = y;

		boundedPlayer = new Rectangle();
	}

	public abstract void update();

	public void render(SpriteBatch sb) {
		sb.draw(player, x, y);
	}

	public Rectangle getBounded() {
		return boundedPlayer.set(x, y, width, height);
	}

	public int getX() {return this.x;}
	public void setX(int x) {this.x = x;}
	public int getY() {return y;}
	public void setY(int y) {this.y = y;}
}