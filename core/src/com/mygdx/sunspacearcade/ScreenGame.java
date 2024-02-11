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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class ScreenGame implements Screen {
    SunSpaceArcade sunSpaceArcade;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont font;

    Texture imgBackGround;
    Texture imgShipsAtlas;
    TextureRegion[] imgShip = new TextureRegion[12];
    Texture imgShot;

    SpaceButton btnBack;

    Stars[] stars = new Stars[2];
    Ship ship;
    Array<Shot> shots = new Array<>();
    long timeLastShot, timeShotInterval = 700;

    public ScreenGame(SunSpaceArcade sunSpaceArcade) {
        this.sunSpaceArcade = sunSpaceArcade;
        batch = sunSpaceArcade.batch;
        camera = sunSpaceArcade.camera;
        touch = sunSpaceArcade.touch;
        font = sunSpaceArcade.font;

        imgBackGround = new Texture("space1.png");
        imgShipsAtlas = new Texture("ships_atlas3.png");
        imgShot = new Texture("shoot_blaster_red.png");
        for (int i = 0; i < 12; i++) {
            if(i<7) {
                imgShip[i] = new TextureRegion(imgShipsAtlas, i * 400, 0, 400, 400);
            } else {
                imgShip[i] = new TextureRegion(imgShipsAtlas, (12-i) * 400, 0, 400, 400);
            }
        }

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

            ship.touchScreen(touch.x);
        }

        // события
        for (int i = 0; i < stars.length; i++) {
            stars[i].move();
        }
        spawnShots();
        for (int i = 0; i < shots.size; i++) {
            shots.get(i).move();
            if (shots.get(i).outOfScreen()){
                shots.removeIndex(i);
            }
        }
        ship.move();

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int i = 0; i < stars.length; i++) {
            batch.draw(imgBackGround, stars[i].x, stars[i].y, stars[i].width, stars[i].height);
        }
        font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        for (Shot s: shots) {
            batch.draw(imgShot, s.getX(), s.getY(), s.width, s.height);
        }
        batch.draw(imgShip[ship.phase], ship.getX(), ship.getY(), ship.width, ship.height);
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
        imgShot.dispose();
    }

    void spawnShots(){
        if(TimeUtils.millis() > timeLastShot+timeShotInterval) {
            shots.add(new Shot(ship));
            timeLastShot = TimeUtils.millis();
        }
    }
}
