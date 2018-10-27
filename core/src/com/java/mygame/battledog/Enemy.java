package com.java.mygame.battledog;

public class Enemy extends ObjectUpDown {

	public Enemy(int x, int y) {
		super(x, y);

		pic = BattleDog.res.getAtlas("pack").findRegion("enemy");
		width = pic.getRegionWidth();
		height = pic.getRegionHeight();

		speed = 5;
	}
}