package com.mygdx.sunspacearcade;

import static com.mygdx.sunspacearcade.SunSpaceArcade.SCR_HEIGHT;
import static com.mygdx.sunspacearcade.SunSpaceArcade.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class ScreenGame implements Screen {
    SunSpaceArcade sunSpaceArcade;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont font;

    Texture imgBackGround;
    Texture imgShipsAtlas;
    TextureRegion imgShip;

    SpaceButton btnBack;

    Stars[] stars = new Stars[2];
    Ship ship;

    public ScreenGame(SunSpaceArcade sunSpaceArcade) {
        this.sunSpaceArcade = sunSpaceArcade;
        batch = sunSpaceArcade.batch;
        camera = sunSpaceArcade.camera;
        touch = sunSpaceArcade.touch;
        font = sunSpaceArcade.font;

        imgBackGround = new Texture("space1.png");
        imgShipsAtlas = new Texture("ships_atlas3.png");
        imgShip = new TextureRegion(imgShipsAtlas, 0, 0, 400, 400);

        btnBack = new SpaceButton("x", SCR_WIDTH-50, SCR_HEIGHT, font);

        stars[0] = new Stars(0);
        stars[1] = new Stars(SCR_HEIGHT);

        ship = new Ship();
    }

    @Override
    public void show() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        touch.set(0, 0, 0);
    }

    @Override
    public void render(float delta) {
        // касания
        if(Gdx.input.isTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnBack.hit(touch.x, touch.y)){
                sunSpaceArcade.setScreen(sunSpaceArcade.screenMenu);
            }

            ship.hit(touch.x);
        }

        // события
        for (int i = 0; i < stars.length; i++) {
            stars[i].move();
        }
        ship.move();

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int i = 0; i < stars.length; i++) {
            batch.draw(imgBackGround, stars[i].x, stars[i].y, stars[i].width, stars[i].height);
        }
        font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        batch.draw(imgShip, ship.getX(), ship.getY(), ship.width, ship.height);
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
        imgShipsAtlas.dispose();
    }
}
