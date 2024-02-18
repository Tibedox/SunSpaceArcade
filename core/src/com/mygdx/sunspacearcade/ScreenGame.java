package com.mygdx.sunspacearcade;

import static com.mygdx.sunspacearcade.SunSpaceArcade.SCR_HEIGHT;
import static com.mygdx.sunspacearcade.SunSpaceArcade.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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
    TextureRegion[] imgEnemy = new TextureRegion[12];
    TextureRegion[][] imgFragment = new TextureRegion[5][16];
    Texture imgShot;
    Sound sndShot;
    Sound sndExplosion;

    SpaceButton btnBack;

    Stars[] stars = new Stars[2];
    Ship ship;
    Array<Shot> shots = new Array<>();
    long timeLastShot, timeShotInterval = 700;
    Array<Enemy> enemies = new Array<>();
    long timeLastEnemy, timeEnemyInterval = 1500;
    Array<Fragment> fragments = new Array<>();
    int nFragments = 55;

    public ScreenGame(SunSpaceArcade sunSpaceArcade) {
        this.sunSpaceArcade = sunSpaceArcade;
        batch = sunSpaceArcade.batch;
        camera = sunSpaceArcade.camera;
        touch = sunSpaceArcade.touch;
        font = sunSpaceArcade.font;

        sndShot = Gdx.audio.newSound(Gdx.files.internal("blaster.wav"));
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));

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
        for (int i = 0; i < 12; i++) {
            if(i<7) {
                imgEnemy[i] = new TextureRegion(imgShipsAtlas, i * 400, 1600, 400, 400);
            } else {
                imgEnemy[i] = new TextureRegion(imgShipsAtlas, (12-i) * 400, 1600, 400, 400);
            }
        }
        for (int i = 0; i < imgFragment.length; i++) {
            for (int j = 0; j < imgFragment[i].length; j++) {
                imgFragment[i][j] = new TextureRegion(imgShipsAtlas, j%4*100, j/4*100+i*400, 100, 100);
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
            Thread.sleep(500);
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
        spawnEnemies();
        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i).move();
            if (enemies.get(i).outOfScreen()){
                enemies.removeIndex(i);
            }
        }
        spawnShots();
        for (int i = 0; i < shots.size; i++) {
            shots.get(i).move();
            if (shots.get(i).outOfScreen()){
                shots.removeIndex(i);
                continue;
            }
            for (int j = 0; j < enemies.size; j++) {
                if (shots.get(i).overlap(enemies.get(j))){
                    spawnFragments(enemies.get(j));
                    shots.removeIndex(i);
                    enemies.removeIndex(j);
                    sndExplosion.play();
                    break;
                }
            }
        }
        for (int i = 0; i < fragments.size; i++) {
            fragments.get(i).move();
        }
        ship.move();

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int i = 0; i < stars.length; i++) {
            batch.draw(imgBackGround, stars[i].x, stars[i].y, stars[i].width, stars[i].height);
        }
        font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        for(Fragment f: fragments){
            batch.draw(imgFragment[f.type][f.nFrag], f.getX(), f.getY(), f.width/2, f.height/2, f.width, f.height, 1, 1, f.rotation);
        }
        for (Enemy s: enemies) {
            batch.draw(imgEnemy[s.phase], s.getX(), s.getY(), s.width, s.height);
        }
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
            sndShot.play(0.2f);
        }
    }

    void spawnEnemies(){
        if(TimeUtils.millis() > timeLastEnemy+timeEnemyInterval) {
            enemies.add(new Enemy());
            timeLastEnemy = TimeUtils.millis();
        }
    }

    void spawnFragments(SpaceObject object){
        for (int i = 0; i < nFragments; i++) {
            fragments.add(new Fragment(object));
        }
    }
}
