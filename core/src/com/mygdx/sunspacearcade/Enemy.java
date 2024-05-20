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
        x = MathUtils.random(0+width/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT*1.5f, SCR_HEIGHT*2);
        /*v = MathUtils.random(5f, 8f);
        a = MathUtils.atan((xB-x)/(y-yB));
        rotation = a*MathUtils.radiansToDegrees;
        vx = MathUtils.sin(a)*v;
        vy = -MathUtils.cos(a)*v;*/
    }

    void changeAngle(Ship ship) {
        if(y<SCR_HEIGHT/3) {
            rotation = MathUtils.atan2(ship.x-x, y-ship.y) * MathUtils.radiansToDegrees;
            vx = MathUtils.sin(a) * v;
            vy = -MathUtils.cos(a) * v;
        }
    }
}
