package com.mygdx.sunspacearcade;

import com.badlogic.gdx.utils.TimeUtils;

public class SpaceObject {
    int type;
    float x, y;
    float width, height;
    float vx, vy;

    int phase, nPhases = 12;
    long timeLastPhase, timePhaseInterval = 50;

    void move(){
        x += vx;
        y += vy;
    }

    float getX(){
        return x-width/2;
    }

    float getY(){
        return y-height/2;
    }

    void changePhase(){
        if(TimeUtils.millis() > timeLastPhase+timePhaseInterval) {
            if (++phase == nPhases) phase = 0;
            timeLastPhase = TimeUtils.millis();
        }
    }

    boolean overlap(SpaceObject o){
        return Math.abs(x-o.x) < width/3+o.width/3 & Math.abs(y-o.y) < height/3+o.height/3;
    }
}
