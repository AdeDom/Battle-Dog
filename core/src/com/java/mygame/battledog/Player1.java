package com.java.mygame.battledog;

public class Player1 extends Player {

	public Player1(int x, int y) {
		super(x, y);
		player = BattleDog.res.getAtlas("pack").findRegion("dogP1");
		width = player.getRegionWidth();
		height = player.getRegionHeight();
	}

	public void update() {
		if (x < 0)
			x = 0;
		if (x > BattleDog.MID_SCREEN - width)
			x = BattleDog.MID_SCREEN - width;
	}
}
