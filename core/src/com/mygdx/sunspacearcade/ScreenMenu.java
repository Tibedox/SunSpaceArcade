package com.mygdx.sunspacearcade;

import static com.mygdx.sunspacearcade.SunSpaceArcade.SCR_HEIGHT;
import static com.mygdx.sunspacearcade.SunSpaceArcade.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class ScreenMenu implements Screen {
    SunSpaceArcade sunSpaceArcade;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont font;

    Texture imgBackGround;

    SpaceButton btnPlay;
    SpaceButton btnSettings;
    SpaceButton btnAbout;
    SpaceButton btnExit;

    public ScreenMenu(SunSpaceArcade sunSpaceArcade) {
        this.sunSpaceArcade = sunSpaceArcade;
        batch = sunSpaceArcade.batch;
        camera = sunSpaceArcade.camera;
        touch = sunSpaceArcade.touch;
        font = sunSpaceArcade.fontLarge;

        imgBackGround = new Texture("space0.png");

        btnPlay = new SpaceButton("Play", 100, 1100, font);
        btnSettings = new SpaceButton("Settings", 100, 950, font);
        btnAbout = new SpaceButton("About Game", 100, 800, font);
        btnExit = new SpaceButton("Exit", 100, 650, font);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // касания
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnPlay.hit(touch.x, touch.y)){
                sunSpaceArcade.setScreen(sunSpaceArcade.screenGame);
            }
            if(btnSettings.hit(touch.x, touch.y)){
                sunSpaceArcade.setScreen(sunSpaceArcade.screenSettings);
            }
            if(btnAbout.hit(touch.x, touch.y)){
                sunSpaceArcade.setScreen(sunSpaceArcade.screenAbout);
            }
            if(btnExit.hit(touch.x, touch.y)){
                Gdx.app.exit();
            }
        }

        // события

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        font.draw(batch, btnPlay.text, btnPlay.x, btnPlay.y);
        font.draw(batch, btnSettings.text, btnSettings.x, btnSettings.y);
        font.draw(batch, btnAbout.text, btnAbout.x, btnAbout.y);
        font.draw(batch, btnExit.text, btnExit.x, btnExit.y);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
    }
}
