package com.java.mygame.battledog;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;

public class GameWorld {
	public Player1 player1;
	public Player2 player2;

	public ArrayList<Item> itemsP1;
	public long createItemTimerP1;
	public long createItemDelayP1;
	public ArrayList<Item> itemsP2;
	public long createItemTimerP2;
	public long createItemDelayP2;

	public ArrayList<Enemy> enemiesP1;
	public ArrayList<Enemy> enemiesP2;
	public int numItemP1;
	public int numItemP2;

	public int scoreP1;
	public int scoreP2;
	public int liveP1;
	public int liveP2;

	public boolean inGame;
	public String messageP1;
	public String messageP2;
	private static Preferences prefsP1;
	private static Preferences prefsP2;

	public Music musicInGame;
	public Sound soundItemP1;
	public Sound soundItemP2;
	public Sound soundEnemyP1;
	public Sound soundEnemyP2;

	public GameWorld() {
		player1 = new Player1(BattleDog.MID_SCREEN / 2, 20);
		player2 = new Player2(BattleDog.MID_SCREEN / 2 + BattleDog.MID_SCREEN,
				20);

		itemsP1 = new ArrayList<Item>();
		createItemTimerP1 = System.nanoTime();
		createItemDelayP1 = 1000;
		itemsP2 = new ArrayList<Item>();
		createItemTimerP2 = System.nanoTime();
		createItemDelayP2 = 1000;

		enemiesP1 = new ArrayList<Enemy>();// enemy of player1
		enemiesP2 = new ArrayList<Enemy>();
		numItemP1 = 0;
		numItemP2 = 0;

		scoreP1 = 0;
		scoreP2 = 0;
		liveP1 = 3;
		liveP2 = 3;

		inGame = true;
		messageP1 = "WIN!";
		messageP2 = "WIN!";

		prefsP1 = Gdx.app.getPreferences("BattleDogP1");
		if (!prefsP1.contains("hiScore"))
			prefsP1.putInteger("hiScore", 0);
		prefsP2 = Gdx.app.getPreferences("BattleDogP2");
		if (!prefsP2.contains("hiScore"))
			prefsP2.putInteger("hiScore", 0);

		musicInGame = Gdx.audio.newMusic(Gdx.files
				.internal("sounds/mainGame.mp3"));
		musicInGame.setLooping(true);
		musicInGame.play();
		soundItemP1 = Gdx.audio.newSound(Gdx.files
				.internal("sounds/itemP1.wav"));
		soundItemP2 = Gdx.audio.newSound(Gdx.files
				.internal("sounds/itemP2.wav"));
		soundEnemyP1 = Gdx.audio.newSound(Gdx.files
				.internal("sounds/YelpP1.wav"));
		soundEnemyP2 = Gdx.audio.newSound(Gdx.files
				.internal("sounds/YelpP2.wav"));
	}

	public void update(float delta) {
		if (inGame) {
			player1.update();
			player2.update();

			createItemP1();
			for (int i = 0; i < itemsP1.size(); i++) {
				boolean removeItem = itemsP1.get(i).update();
				if (removeItem)
					itemsP1.remove(i);
			}
			checkPlayerItemCollisionP1();

			createItemP2();
			for (int i = 0; i < itemsP2.size(); i++) {
				boolean removeItemP2 = itemsP2.get(i).update();
				if (removeItemP2)
					itemsP2.remove(i);
			}
			checkPlayerItemCollisionP2();

			for (int i = 0; i < enemiesP1.size(); i++) {
				boolean removeEnemy = enemiesP1.get(i).update();
				if (removeEnemy)
					enemiesP1.remove(i);
			}
			checkPlayerEnemyCollisionP1();

			for (int i = 0; i < enemiesP2.size(); i++) {
				boolean removeEnemy = enemiesP2.get(i).update();
				if (removeEnemy)
					enemiesP2.remove(i);
			}
			checkPlayerEnemyCollisionP2();
		}
	}

	private void createItemP1() {
		int x = (int) (Math.random() * BattleDog.MID_SCREEN - 100);
		long elapsed = (System.nanoTime() - createItemTimerP1) / 1000000;
		if (elapsed > createItemDelayP1) {
			itemsP1.add(new Item(x, BattleDog.HEIGHT));
			createItemTimerP1 = System.nanoTime();
		}
	}

	public void checkPlayerItemCollisionP1() {
		Rectangle pRect = player1.getBounded();
		for (int i = 0; i < itemsP1.size(); i++) {
			Rectangle iRect = itemsP1.get(i).getBounded();
			if (pRect.overlaps(iRect)) {
				soundItemP1.play();
				scoreP1++;
				itemsP1.remove(i);
				i--;
				if (createItemDelayP1 > 200)
					createItemDelayP1 -= 5;
				createEnemyP1();
			}
		}
	}

	private void createItemP2() {
		int x = (int) (Math.random() * BattleDog.MID_SCREEN);
		x += BattleDog.MID_SCREEN;
		long elapsed = (System.nanoTime() - createItemTimerP2) / 1000000;
		if (elapsed > createItemDelayP2) {
			itemsP2.add(new Item(x, BattleDog.HEIGHT));
			createItemTimerP2 = System.nanoTime();
		}
	}

	public void checkPlayerItemCollisionP2() {
		Rectangle pRect = player2.getBounded();
		for (int i = 0; i < itemsP2.size(); i++) {
			Rectangle iRect = itemsP2.get(i).getBounded();
			if (pRect.overlaps(iRect)) {
				soundItemP2.play();
				scoreP2++;
				itemsP2.remove(i);
				i--;
				if (createItemDelayP2 > 200)
					createItemDelayP2 -= 5;
				createEnemyP2();
			}
		}
	}

	private void createEnemyP1() {//EnemyP1 Give P2
		numItemP1++;
		if (numItemP1 % 2 == 0) {
			int x = (int) (Math.random() * BattleDog.MID_SCREEN);
			x += BattleDog.MID_SCREEN;
			enemiesP1.add(new Enemy(x, BattleDog.HEIGHT));
		}
		if (numItemP1 % 100 == 0)
			liveP1++;
	}

	private void checkPlayerEnemyCollisionP1() {
		Rectangle pRect = player2.getBounded();
		for (int i = 0; i < enemiesP1.size(); i++) {
			Rectangle eRect = enemiesP1.get(i).getBounded();
			if (pRect.overlaps(eRect)) {
				liveP2--;
				enemiesP1.remove(i);
				i--;
				soundEnemyP2.play();
				if (liveP2 <= 0) {
					musicInGame.stop();
					messageP2 = "LOSE";
					inGame = false;
					itemsP1.clear();
					itemsP2.clear();
					enemiesP1.clear();
					enemiesP2.clear();
				}
			}
		}
	}

	private void createEnemyP2() {//create EnemyP2 Give P1
		numItemP2++;
		if (numItemP2 % 2 == 0) {
			int x = (int) (Math.random() * BattleDog.MID_SCREEN - 100);
			enemiesP2.add(new Enemy(x, BattleDog.HEIGHT));
		}
		if (numItemP2 % 100 == 0)
			liveP2++;
	}

	private void checkPlayerEnemyCollisionP2() {
		Rectangle pRect = player1.getBounded();
		for (int i = 0; i < enemiesP2.size(); i++) {
			Rectangle eRect = enemiesP2.get(i).getBounded();
			if (pRect.overlaps(eRect)) {
				liveP1--;
				enemiesP2.remove(i);
				i--;
				soundEnemyP1.play();
				if (liveP1 <= 0) {
					musicInGame.stop();
					messageP1 = "LOSE";
					inGame = false;
					itemsP1.clear();
					itemsP2.clear();
					enemiesP1.clear();
					enemiesP2.clear();
				}
			}
		}
	}

	public static void setHiScoreP1(int val) {
		prefsP1.putInteger("hiScore", val);
		prefsP1.flush();
	}

	public static int getHiScoreP1() {
		return prefsP1.getInteger("hiScore");
	}

	public static void setHiScoreP2(int val) {
		prefsP2.putInteger("hiScore", val);
		prefsP2.flush();
	}

	public static int getHiScoreP2() {
		return prefsP2.getInteger("hiScore");
	}
}
