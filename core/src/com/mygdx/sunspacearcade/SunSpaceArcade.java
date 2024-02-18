package com.mygdx.sunspacearcade;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class SunSpaceArcade extends Game {
	public static final float SCR_WIDTH = 900, SCR_HEIGHT = 1600;
	public static final int TYPE_SHIP = 0, TYPE_ENEMY1 = 1, TYPE_ENEMY2 = 2, TYPE_ENEMY3 = 3, TYPE_ENEMY4 = 4;

	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;
	BitmapFont fontLarge, fontSmall;

	ScreenMenu screenMenu;
	ScreenSettings screenSettings;
	ScreenAbout screenAbout;
	ScreenGame screenGame;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		touch = new Vector3();
		fontLarge = new BitmapFont(Gdx.files.internal("catorze.fnt"));
		fontSmall = new BitmapFont(Gdx.files.internal("catorze60.fnt"));

		screenMenu = new ScreenMenu(this);
		screenSettings = new ScreenSettings(this);
		screenAbout = new ScreenAbout(this);
		screenGame = new ScreenGame(this);
		setScreen(screenMenu);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		fontLarge.dispose();
	}
}
