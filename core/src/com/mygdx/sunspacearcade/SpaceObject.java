package com.mygdx.sunspacearcade;

public class SpaceObject {
    float x, y;
    float width, height;
    float vx, vy;

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
}
