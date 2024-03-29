package com.mygdx.sunspacearcade;

import static com.mygdx.sunspacearcade.SunSpaceArcade.*;
import com.badlogic.gdx.math.MathUtils;

public class Enemy extends SpaceObject{
    public Enemy() {
        type = MathUtils.random(TYPE_ENEMY1, TYPE_ENEMY4);
        width = height = 200;
        x = MathUtils.random(width/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT+height, SCR_HEIGHT*2);
        vy = MathUtils.random(-7f, -4f)+speedGame;
    }

    @Override
    void move() {
        super.move();
        changePhase();
    }

    boolean outOfScreen() {
        return y < -height/2;
    }
}
