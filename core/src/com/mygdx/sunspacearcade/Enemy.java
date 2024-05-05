package com.mygdx.sunspacearcade;

import static com.mygdx.sunspacearcade.SunSpaceArcade.*;
import com.badlogic.gdx.math.MathUtils;

public class Enemy extends SpaceObject{
    float a, rotation;
    float xB = SCR_WIDTH/2;
    float yB = SCR_HEIGHT/12;
    float v;

    public Enemy() {
        type = MathUtils.random(TYPE_ENEMY1, TYPE_ENEMY4);
        width = height = 200;
        x = MathUtils.random(width/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT+height, SCR_HEIGHT*2);
        vy = MathUtils.random(-7f, -4f)+speedGame;
        spawn();
    }

    @Override
    void move() {
        super.move();
        changePhase();
    }

    boolean outOfScreen() {
        return y < -height/2;
    }

    void spawn() {
        x = MathUtils.random(0, SCR_WIDTH);
        y = MathUtils.random(SCR_HEIGHT/4*3, SCR_HEIGHT);
        v = MathUtils.random(3f, 5f);
        a = MathUtils.atan((xB-x)/(y-yB));
        rotation = a*MathUtils.radiansToDegrees;
        vx = MathUtils.sin(a)*v;
        vy = -MathUtils.cos(a)*v;
    }

    void changeAngle(Ship ship) {
        a = MathUtils.atan((ship.x-x)/(y-ship.y));
        rotation = a*MathUtils.radiansToDegrees;
        vx = MathUtils.sin(a)*v;
        vy = -MathUtils.cos(a)*v;
    }
}
