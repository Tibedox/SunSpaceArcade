package com.mygdx.sunspacearcade;

import static com.mygdx.sunspacearcade.SunSpaceArcade.SCR_HEIGHT;
import static com.mygdx.sunspacearcade.SunSpaceArcade.SCR_WIDTH;
import static com.mygdx.sunspacearcade.SunSpaceArcade.TYPE_SHIP;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class ScreenGame implements Screen {
    SunSpaceArcade sunSpaceArcade;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont fontLarge, fontSmall;

    Texture imgBackGround;
    Texture imgShipsAtlas;
    //TextureRegion[] imgShip = new TextureRegion[12];
    TextureRegion[][] imgShip = new TextureRegion[5][12];
    TextureRegion[][] imgFragment = new TextureRegion[5][16];
    Texture imgShot;
    Sound sndShot;
    Sound sndExplosion;

    SpaceButton btnBack;

    Stars[] stars = new Stars[2];
    Ship ship;
    int nShipLives = 3;
    long timeShipKilled, timeShipRespawn = 3000;
    Array<Shot> shots = new Array<>();
    long timeLastShot, timeShotInterval = 700;
    Array<Enemy> enemies = new Array<>();
    long timeLastEnemy, timeEnemyInterval = 1500;
    Array<Fragment> fragments = new Array<>();
    int nFragments = 55;

    Player[] players = new Player[11];
    boolean isGameOver;
    int kills;

    public ScreenGame(SunSpaceArcade sunSpaceArcade) {
        this.sunSpaceArcade = sunSpaceArcade;
        batch = sunSpaceArcade.batch;
        camera = sunSpaceArcade.camera;
        touch = sunSpaceArcade.touch;
        fontLarge = sunSpaceArcade.fontLarge;
        fontSmall = sunSpaceArcade.fontSmall;

        sndShot = Gdx.audio.newSound(Gdx.files.internal("blaster.wav"));
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));

        imgBackGround = new Texture("space1.png");
        imgShipsAtlas = new Texture("ships_atlas3.png");
        imgShot = new Texture("shoot_blaster_red.png");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 12; j++) {
                if (j < 7) {
                    imgShip[i][j] = new TextureRegion(imgShipsAtlas, j * 400, i*400, 400, 400);
                } else {
                    imgShip[i][j] = new TextureRegion(imgShipsAtlas, (12 - j) * 400, i*400, 400, 400);
                }
            }
        }

        for (int i = 0; i < imgFragment.length; i++) {
            for (int j = 0; j < imgFragment[i].length; j++) {
                imgFragment[i][j] = new TextureRegion(imgShipsAtlas, j%4*100, j/4*100+i*400, 100, 100);
            }
        }

        btnBack = new SpaceButton("back to menu", SCR_HEIGHT/10, fontSmall);

        stars[0] = new Stars(0);
        stars[1] = new Stars(SCR_HEIGHT);

        ship = new Ship();

        for (int i = 0; i < players.length; i++) {
            players[i] = new Player("Noname", 0);
        }
        loadRecords();
    }

    @Override
    public void show() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        touch.set(0, 0, 0);
        gameStart();
    }

    @Override
    public void render(float delta) {
        // касания
        if(Gdx.input.isTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(isGameOver & btnBack.hit(touch.x, touch.y)){
                sunSpaceArcade.setScreen(sunSpaceArcade.screenMenu);
            }

            ship.touchScreen(touch.x);
        }

        // события
        for (int i = 0; i < stars.length; i++) {
            stars[i].move();
        }
        if(ship.isAlive) {
            spawnEnemies();
            spawnShots();
            ship.move();
        } else {
            respawnShip();
        }
        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i).move();
            if (enemies.get(i).outOfScreen()){
                enemies.removeIndex(i);
                killShip();
                continue;
            }
            if(enemies.get(i).overlap(ship) & ship.isAlive){
                spawnFragments(enemies.get(i));
                enemies.removeIndex(i);
                killShip();
            }
        }
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
                    kills++;
                    break;
                }
            }
        }
        for (int i = 0; i < fragments.size; i++) {
            fragments.get(i).move();
            if(fragments.get(i).outOfScreen()){
                fragments.removeIndex(i);
            }
        }

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int i = 0; i < stars.length; i++) {
            batch.draw(imgBackGround, stars[i].x, stars[i].y, stars[i].width, stars[i].height);
        }
        for(Fragment f: fragments){
            batch.draw(imgFragment[f.type][f.nFrag], f.getX(), f.getY(), f.width/2, f.height/2, f.width, f.height, 1, 1, f.rotation);
        }
        for (Enemy s: enemies) {
            batch.draw(imgShip[s.type][s.phase], s.getX(), s.getY(), s.width, s.height);
        }
        for (Shot s: shots) {
            batch.draw(imgShot, s.getX(), s.getY(), s.width, s.height);
        }
        if(ship.isAlive) {
            batch.draw(imgShip[TYPE_SHIP][ship.phase], ship.getX(), ship.getY(), ship.width, ship.height);
        }
        for (int i = 0; i < ship.lives; i++) {
            batch.draw(imgShip[TYPE_SHIP][0], SCR_WIDTH-90*(i+1), SCR_HEIGHT-90, 70, 70);
        }
        fontSmall.draw(batch,"Kills: "+kills, 20, SCR_HEIGHT-20);
        if(isGameOver) {
            fontLarge.draw(batch, "Game Over", 0, SCR_HEIGHT / 4 * 3, SCR_WIDTH, Align.center, true);
            for (int i = 0; i < players.length-1; i++) {
                fontSmall.draw(batch, i+1+" "+players[i].name, 200, 1050-i*80);
                String nPoints = amountPoints(fontSmall, i+1+" "+players[i].name, ""+players[i].score, SCR_WIDTH-400);
                fontSmall.draw(batch, nPoints+players[i].score, 200, 1050-i*80, SCR_WIDTH-400, Align.right, true);
            }
            btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        }
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

    private void spawnShots(){
        if(TimeUtils.millis() > timeLastShot+timeShotInterval) {
            shots.add(new Shot(ship));
            timeLastShot = TimeUtils.millis();
            if(sunSpaceArcade.isSoundOn) sndShot.play(0.05f);
        }
    }

    private void spawnEnemies(){
        if(TimeUtils.millis() > timeLastEnemy+timeEnemyInterval) {
            enemies.add(new Enemy());
            timeLastEnemy = TimeUtils.millis();
        }
    }

    private void spawnFragments(SpaceObject object){
        if(sunSpaceArcade.isSoundOn) sndExplosion.play();
        for (int i = 0; i < nFragments; i++) {
            fragments.add(new Fragment(object));
        }
    }

    private void killShip(){
        if(ship.isAlive) {
            timeShipKilled = TimeUtils.millis();
            spawnFragments(ship);
            ship.isAlive = false;
            ship.lives--;
            if(ship.lives == 0){
                gameOver();
            }
        }
    }

    private void respawnShip(){
        if(TimeUtils.millis()>timeShipKilled+timeShipRespawn && enemies.size==0 && !isGameOver){
            ship.isAlive = true;
            ship.x = SCR_WIDTH/2;
            ship.y = SCR_HEIGHT/12;
            ship.vx = 0;
            ship.vy = 0;
        }
    }

    private void gameOver(){
        isGameOver = true;
        players[players.length-1].name = sunSpaceArcade.playerName;
        players[players.length-1].score = kills;
        sortRecords();
        saveRecords();
    }

    private void gameStart(){
        isGameOver = false;
        kills = 0;
        enemies.clear();
        shots.clear();
        fragments.clear();
        respawnShip();
        ship.lives = nShipLives;
    }

    private String amountPoints(BitmapFont font, String text1, String text2, float width) {
        GlyphLayout layout1 = new GlyphLayout(font, text1);
        GlyphLayout layout2 = new GlyphLayout(font, text2);
        float pointsWidth = width-layout1.width-layout2.width;
        GlyphLayout layoutPoint = new GlyphLayout(font, ".");
        int amountPoints = (int) (pointsWidth/layoutPoint.width)/3;
        String s = "";
        for (int i = 0; i < amountPoints; i++) s +=".";
        return s;
    }

    private void sortRecords(){
        boolean flag = true;
        while (flag){
            flag = false;
            for (int i = 0; i < players.length-1; i++) {
                if(players[i].score<players[i+1].score){
                    Player c = players[i];
                    players[i] = players[i+1];
                    players[i+1] = c;
                    flag = true;
                }
            }
        }
    }

    private void saveRecords(){
        Preferences prefs = Gdx.app.getPreferences("SunArcadeRecords");
        for (int i = 0; i < players.length; i++) {
            prefs.putString("name"+i, players[i].name);
            prefs.putInteger("score"+i, players[i].score);
        }
        prefs.flush();
    }

    private void loadRecords(){
        Preferences prefs = Gdx.app.getPreferences("SunArcadeRecords");
        for (int i = 0; i < players.length; i++) {
            if(prefs.contains("name"+i)) players[i].name = prefs.getString("name"+i);
            if(prefs.contains("score"+i)) players[i].score = prefs.getInteger("score"+i);
        }
    }

    public void clearRecords(){
        for (int i = 0; i < players.length; i++) {
            players[i].name = "Noname";
            players[i].score = 0;
        }
    }
}
