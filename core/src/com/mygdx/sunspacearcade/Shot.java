package com.mygdx.sunspacearcade;

import static com.mygdx.sunspacearcade.SunSpaceArcade.SCR_HEIGHT;
import static com.mygdx.sunspacearcade.SunSpaceArcade.SCR_WIDTH;

public class Shot extends SpaceObject{
    public Shot(Ship ship) {
        width = height = 200;
        x = ship.x;
        y = ship.y;
        vy = 12;
    }

    boolean outOfScreen() {
        return y > SCR_HEIGHT+height/2;
    }
}
