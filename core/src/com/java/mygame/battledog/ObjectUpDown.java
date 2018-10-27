package com.java.mygame.battledog;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class ObjectUpDown {
	protected TextureRegion pic;
	protected int x;
	protected int y;
	protected int dy;
	protected int width;
	protected int height;
	protected int speed;

	private Rectangle boundedPic;

	public ObjectUpDown(int x, int y) {
		this.x = x;
		this.y = y;
		dy = 0;

		boundedPic = new Rectangle();
	}

	public boolean update() {
		dy = speed;
		y -= dy;

		if (x < 0)
			x = 0;
		if (x > BattleDog.WIDTH - width)
			x = BattleDog.WIDTH - width;

		dy = 0;

		if (y < -height)
			return true;
		return false;
	}

	public void render(SpriteBatch sb) {
		sb.draw(pic, x, y);
	}

	public Rectangle getBounded() {
		return boundedPic.set(x, y, width, height);
	}
}