package com.mygdx.game.holdable;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Created May 30, 2023
 */
public abstract class Holdable {

    public enum Type {
        sandwich, bread, ham, cheese, lettuce, tomato
    }

    public void draw(Batch batch, float x, float y, float width, float height) {

    }

    public void dispose() {

    }

}
