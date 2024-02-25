package com.mygdx.sunspacearcade;

import static com.mygdx.sunspacearcade.SunSpaceArcade.SCR_HEIGHT;
import static com.mygdx.sunspacearcade.SunSpaceArcade.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class ScreenSettings implements Screen {
    SunSpaceArcade sunSpaceArcade;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont font;

    Texture imgBackGround;

    SpaceButton btnName;
    SpaceButton btnSound;
    SpaceButton btnClearRecords;
    SpaceButton btnBack;

    public ScreenSettings(SunSpaceArcade sunSpaceArcade) {
        this.sunSpaceArcade = sunSpaceArcade;
        batch = sunSpaceArcade.batch;
        camera = sunSpaceArcade.camera;
        touch = sunSpaceArcade.touch;
        font = sunSpaceArcade.fontLarge;

        imgBackGround = new Texture("space3.png");

        loadSettings();
        btnName = new SpaceButton("Name: "+sunSpaceArcade.playerName, 100, 1000, font);
        btnSound = new SpaceButton(sunSpaceArcade.isSoundOn ? "Sound ON" : "Sound OFF", 100, 850, font);
        btnClearRecords = new SpaceButton("Clear Records", 100, 700, font);
        btnBack = new SpaceButton("Back", 100, 550, font);
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

            if(btnName.hit(touch.x, touch.y)){
                //sunSpaceArcade.setScreen(sunSpaceArcade.screenSettings);
            }
            if(btnSound.hit(touch.x, touch.y)){
                sunSpaceArcade.isSoundOn = !sunSpaceArcade.isSoundOn;
                btnSound.setText(sunSpaceArcade.isSoundOn ? "Sound ON" : "Sound OFF");
            }
            if(btnClearRecords.hit(touch.x, touch.y)){
                sunSpaceArcade.screenGame.clearRecords();
                btnClearRecords.setText("Records Cleared");
            }
            if(btnBack.hit(touch.x, touch.y)){
                sunSpaceArcade.setScreen(sunSpaceArcade.screenMenu);
            }
        }

        // события

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        font.draw(batch, btnName.text, btnName.x, btnName.y);
        font.draw(batch, btnSound.text, btnSound.x, btnSound.y);
        font.draw(batch, btnClearRecords.text, btnClearRecords.x, btnClearRecords.y);
        font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
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
        btnClearRecords.setText("Clear Records");
        saveSettings();
    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
    }

    private void saveSettings(){
        Preferences prefs = Gdx.app.getPreferences("SunArcadeSettings");
        prefs.putBoolean("sound", sunSpaceArcade.isSoundOn);
        prefs.putString("name", sunSpaceArcade.playerName);
        prefs.flush();
    }

    private void loadSettings(){
        Preferences prefs = Gdx.app.getPreferences("SunArcadeSettings");
        if(prefs.contains("sound")) sunSpaceArcade.isSoundOn = prefs.getBoolean("sound");
        if(prefs.contains("name")) sunSpaceArcade.playerName = prefs.getString("name");
    }
}
