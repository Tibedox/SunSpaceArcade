package com.mygdx.sunspacearcade;

import static com.mygdx.sunspacearcade.SunSpaceArcade.SCR_HEIGHT;
import static com.mygdx.sunspacearcade.SunSpaceArcade.SCR_WIDTH;
import static com.mygdx.sunspacearcade.SunSpaceArcade.TYPE_SHIP;

public class Ship extends SpaceObject{
    int lives;
    boolean isAlive = true;

    public Ship() {
        type = TYPE_SHIP;
        width = height = 200;
        x = SCR_WIDTH/2;
        y = SCR_HEIGHT/12;
    }

    @Override
    void move() {
        super.move();
        outOfScreen();
        changePhase();
    }

    void outOfScreen() {
        if (x < width/2) {
            x = width/2;
            vx = 0;
        }
        if (x > SCR_WIDTH - width/2) {
            x = SCR_WIDTH - width/2;
            vx = 0;
        }
    }

    void touchScreen(float tx) {
        vx = (tx-x)/20;
    }
}
