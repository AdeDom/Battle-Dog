package com.java.mygame.battledog;

public class ShotP2 extends ObjectUpDown {
	public ShotP2(int x, int y) {
		super(x, y);

		pic = BattleDog.res.getAtlas("pack").findRegion("shotP2");
		width = pic.getRegionWidth();
		height = pic.getRegionHeight();

		speed = 10;
	}

	public boolean update() {
		dy = speed;
		y += dy;
		dy = 0;

		if (y > BattleDog.HEIGHT)
			return true;
		return false;
	}
}
