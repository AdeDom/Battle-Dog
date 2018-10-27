package com.java.mygame.battledog;

public class Item extends ObjectUpDown {

	public Item(int x, int y) {
		super(x, y);

		pic = BattleDog.res.getAtlas("pack").findRegion("item");
		width = pic.getRegionWidth();
		height = pic.getRegionHeight();

		speed = 10;
	}
}
