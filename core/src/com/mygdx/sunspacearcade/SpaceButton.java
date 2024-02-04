package com.mygdx.sunspacearcade;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class SpaceButton {
    String text;
    BitmapFont font;
    float x, y;
    float width, height;

    public SpaceButton(String text, float x, float y, BitmapFont font) {
        this.text = text;
        this.font = font;
        this.x = x;
        this.y = y;
        GlyphLayout layout = new GlyphLayout(font, text);
        width = layout.width;
        height = layout.height;
    }

    boolean hit(float tx, float ty){
        return x < tx & tx < x+width & y-height < ty & ty < y;
    }
}
