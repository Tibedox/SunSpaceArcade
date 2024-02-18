package com.mygdx.sunspacearcade;

import com.badlogic.gdx.math.MathUtils;

public class Fragment extends SpaceObject{
    int nFrag;
    float rotation, vRotation;
    float a, v;

    public Fragment(SpaceObject o) {
        x = o.x;
        y = o.y;
        type = o.type;
        nFrag = MathUtils.random(0, 15);
        width = MathUtils.random(o.width/8, o.width/4);
        height = MathUtils.random(o.height/8, o.height/4);
        v = MathUtils.random(1f, 7f);
        a = MathUtils.random(0f, 360f);
        vx = v*MathUtils.sin(a);
        vy = v*MathUtils.cos(a);
        vRotation = MathUtils.random(-5f, 5f);
    }

    @Override
    void move() {
        super.move();
        rotation += vRotation;
    }
}
