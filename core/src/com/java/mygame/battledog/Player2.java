package com.java.mygame.battledog;

public class Player2 extends Player {

	public Player2(int x, int y) {
		super(x, y);
		player = BattleDog.res.getAtlas("pack").findRegion("dogP2");
		width = player.getRegionWidth();
		height = player.getRegionHeight();
	}

	public void update() {
		if (x < BattleDog.MID_SCREEN)
			x = BattleDog.MID_SCREEN;
		if (x > BattleDog.WIDTH - width)
			x = BattleDog.WIDTH - width;
	}
}
