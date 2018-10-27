package com.java.mygame.battledog;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class GameRenderer {
	private GameWorld myWorld;
	private OrthographicCamera cam;
	private BitmapFont font;
	private Texture imgBG, imgEndBG;
	private ShapeRenderer sr;

	private ArrayList<ShotP1> shotsP1;
	private long createShotTimerP1;
	private long createShotDelayP1;

	private ArrayList<ShotP2> shotsP2;
	private long createShotTimerP2;
	private long createShotDelayP2;

	public Sound soundSpacialP1;
	public Sound soundSpacialP2;

	public GameRenderer(GameWorld world) {
		myWorld = world;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, BattleDog.WIDTH, BattleDog.HEIGHT);
		font = new BitmapFont();
		imgBG = new Texture("HomeDog.png");
		imgEndBG = new Texture("BG_end.png");
		sr = new ShapeRenderer();

		shotsP1 = new ArrayList<ShotP1>();
		createShotTimerP1 = System.nanoTime();
		createShotDelayP1 = 1000;

		shotsP2 = new ArrayList<ShotP2>();
		createShotTimerP2 = System.nanoTime();
		createShotDelayP2 = 1000;

		soundSpacialP1 = Gdx.audio.newSound(Gdx.files
				.internal("sounds/specialP1.wav"));
		soundSpacialP2 = Gdx.audio.newSound(Gdx.files
				.internal("sounds/specialP2.wav"));
	}

	public void render(SpriteBatch sb) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (myWorld.inGame) {
			sb.begin();

			sb.draw(imgBG, 0, 0);
			sb.draw(imgBG, 640, 0);

			font.setColor(Color.BLACK);
			font.draw(sb, "Score: " + myWorld.scoreP1, 10,
					BattleDog.HEIGHT - 20);
			font.draw(sb, "Live: " + myWorld.liveP1, 10, BattleDog.HEIGHT - 40);

			font.draw(sb, "Score: " + myWorld.scoreP2,
					BattleDog.MID_SCREEN + 10, BattleDog.HEIGHT - 20);
			font.draw(sb, "Live: " + myWorld.liveP2, BattleDog.MID_SCREEN + 10,
					BattleDog.HEIGHT - 40);

			font.draw(sb, "Left = A, Right = D, Bullet = V", 10, 15);
			font.draw(sb, "Left = Left, Right = Right, Bullet = M",
					BattleDog.MID_SCREEN + 10, 15);

			myWorld.player1.render(sb);
			myWorld.player2.render(sb);

			for (Item itemP1 : myWorld.itemsP1)
				itemP1.render(sb);
			for (Item itemP2 : myWorld.itemsP2)
				itemP2.render(sb);

			for (Enemy enemyP1 : myWorld.enemiesP1)
				enemyP1.render(sb);
			for (Enemy enemyP2 : myWorld.enemiesP2)
				enemyP2.render(sb);

			for (int i = 0; i < shotsP1.size(); i++) {
				shotsP1.get(i).render(sb);
				boolean removeShot = shotsP1.get(i).update();
				if (removeShot)
					shotsP1.remove(i);
			}
			for (int i = 0; i < shotsP2.size(); i++) {
				shotsP2.get(i).render(sb);
				boolean removeShot = shotsP2.get(i).update();
				if (removeShot)
					shotsP2.remove(i);
			}

			sb.end();

			sr.begin(ShapeType.Line);
			sr.setColor(0, 0, 0, 0);
			sr.line(BattleDog.MID_SCREEN, BattleDog.HEIGHT,
					BattleDog.MID_SCREEN, 0);
			sr.end();

			inputHandlerPlayer1();
			inputHandlerPlayer2();

			checkShotEnemyCollisionP1();
			checkShotEnemyCollisionP2();
		} else {
			sb.begin();
			sb.draw(imgEndBG, 780, 0);

			font.setColor(Color.BLACK);
			font.getData().setScale(3);
			font.draw(sb, "Battle Dog", BattleDog.MID_SCREEN - 90, 650);

			font.getData().setScale(2);
			font.draw(sb, "Player1", BattleDog.MID_SCREEN / 2 + 105, 500);
			font.draw(sb, "Player2", BattleDog.MID_SCREEN + 155, 500);

			font.getData().setScale(4);
			font.draw(sb, myWorld.messageP1, BattleDog.MID_SCREEN / 2 + 85, 400);
			font.draw(sb, myWorld.messageP2, BattleDog.MID_SCREEN + 135, 400);

			font.getData().setScale(2);
			if (myWorld.scoreP1 > myWorld.getHiScoreP1())
				myWorld.setHiScoreP1(myWorld.scoreP1);
			font.draw(sb, "High Score: " + myWorld.getHiScoreP1(),
					BattleDog.MID_SCREEN / 2 + 50, 290);
			if (myWorld.scoreP2 > myWorld.getHiScoreP2())
				myWorld.setHiScoreP2(myWorld.scoreP2);
			font.draw(sb, "High Score: " + myWorld.getHiScoreP2(),
					BattleDog.MID_SCREEN + 100, 290);

			font.draw(sb, "Score: " + myWorld.scoreP1,
					BattleDog.MID_SCREEN / 2 + 100, 200);
			font.draw(sb, "Score: " + myWorld.scoreP2,
					BattleDog.MID_SCREEN + 150, 200);

			font.getData().setScale(1);
			font.draw(sb, "Press spacebar / tab to replay",
					BattleDog.MID_SCREEN - 90, 25);

			sb.end();
			inputHandlerGameOver();
		}
	}

	private void inputHandlerPlayer1() {
		int x = myWorld.player1.getX();
		if (Gdx.input.isKeyPressed(Keys.A)) {
			x -= 200 * Gdx.graphics.getDeltaTime();
			myWorld.player1.setX(x);
		}

		if (Gdx.input.isKeyPressed(Keys.D)) {
			x += 200 * Gdx.graphics.getDeltaTime();
			myWorld.player1.setX(x);
		}

		if (Gdx.input.isKeyPressed(Keys.V))
			createShotP1(myWorld.player1.x, myWorld.player1.y);
	}

	private void inputHandlerPlayer2() {
		int x = myWorld.player2.getX();
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			x -= 200 * Gdx.graphics.getDeltaTime();
			myWorld.player2.setX(x);
		}

		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			x += 200 * Gdx.graphics.getDeltaTime();
			myWorld.player2.setX(x);
		}

		if (Gdx.input.isKeyPressed(Keys.M))
			createShotP2(myWorld.player2.x, myWorld.player2.y);
	}

	private void createShotP1(int x, int y) {
		long elapsed = (System.nanoTime() - createShotTimerP1) / 1000000;
		if (elapsed > createShotDelayP1) {
			soundSpacialP1.play();
			shotsP1.add(new ShotP1(x + 24, y));
			createShotTimerP1 = System.nanoTime();
		}
	}

	private void createShotP2(int x, int y) {
		long elapsed = (System.nanoTime() - createShotTimerP2) / 1000000;
		if (elapsed > createShotDelayP2) {
			soundSpacialP2.play();
			shotsP2.add(new ShotP2(x + 14, y));
			createShotTimerP2 = System.nanoTime();
		}
	}

	private void checkShotEnemyCollisionP1() {
		Rectangle shotRect = new Rectangle();
		for (ShotP1 shot : shotsP1)
			shotRect = shot.getBounded();

		for (int i = 0; i < myWorld.enemiesP2.size(); i++) {
			Rectangle enemyRect = myWorld.enemiesP2.get(i).getBounded();
			if (shotRect.overlaps(enemyRect)) {
				for (int j = 0; j < shotsP1.size(); j++)
					shotsP1.remove(j);
				myWorld.enemiesP2.remove(i);
			}
		}
	}

	private void checkShotEnemyCollisionP2() {
		Rectangle shotRect = new Rectangle();
		for (ShotP2 shot : shotsP2)
			shotRect = shot.getBounded();

		for (int i = 0; i < myWorld.enemiesP1.size(); i++) {
			Rectangle enemyRect = myWorld.enemiesP1.get(i).getBounded();
			if (shotRect.overlaps(enemyRect)) {
				for (int j = 0; j < shotsP2.size(); j++)
					shotsP2.remove(j);
				myWorld.enemiesP1.remove(i);
			}
		}
	}

	private void inputHandlerGameOver() {
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			myWorld.player1 = new Player1(BattleDog.MID_SCREEN / 2, 20);
			myWorld.player2 = new Player2(BattleDog.MID_SCREEN / 2
					+ BattleDog.MID_SCREEN, 20);

			myWorld.itemsP1 = new ArrayList<Item>();
			myWorld.createItemTimerP1 = System.nanoTime();
			myWorld.createItemDelayP1 = 1000;
			myWorld.itemsP2 = new ArrayList<Item>();
			myWorld.createItemTimerP2 = System.nanoTime();
			myWorld.createItemDelayP2 = 1000;

			myWorld.enemiesP1 = new ArrayList<Enemy>();
			myWorld.enemiesP2 = new ArrayList<Enemy>();
			myWorld.numItemP1 = 0;
			myWorld.numItemP2 = 0;

			myWorld.scoreP1 = 0;
			myWorld.liveP1 = 3;
			myWorld.scoreP2 = 0;
			myWorld.liveP2 = 3;

			myWorld.inGame = true;
			myWorld.messageP1 = "WIN!";
			myWorld.messageP2 = "WIN!";

			myWorld.musicInGame.play();
		}
	}
}