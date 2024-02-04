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
import com.badlogic.gdx.utils.ScreenUtils;

public class ScreenSettings implements Screen {
    SunSpaceArcade sunSpaceArcade;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont font;

    Texture imgBackGround;

    SpaceButton btnName;
    SpaceButton btnSound;
    SpaceButton btnMusic;
    SpaceButton btnBack;

    public ScreenSettings(SunSpaceArcade sunSpaceArcade) {
        this.sunSpaceArcade = sunSpaceArcade;
        batch = sunSpaceArcade.batch;
        camera = sunSpaceArcade.camera;
        touch = sunSpaceArcade.touch;
        font = sunSpaceArcade.font;

        imgBackGround = new Texture("space1.png");

        btnName = new SpaceButton("Name", 300, 1000, font);
        btnSound = new SpaceButton("Sound ON", 300, 850, font);
        btnMusic = new SpaceButton("Music ON", 300, 700, font);
        btnBack = new SpaceButton("Back", 300, 550, font);
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
                //sunSpaceArcade.setScreen(sunSpaceArcade.screenSettings);
            }
            if(btnMusic.hit(touch.x, touch.y)){
                //sunSpaceArcade.setScreen(sunSpaceArcade.screenSettings);
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
        font.draw(batch, btnMusic.text, btnMusic.x, btnMusic.y);
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

    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
    }
}
