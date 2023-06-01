package com.mygdx.game.holdable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * MinionBread class (extends class Bread)
 *
 * Created June 1, 2023
 */
public class MinionBread extends Bread {

    public MinionBread() {
        super(new Texture(Gdx.files.internal("Ingredients/.png")));
    }

    @Override
    public Type getType() {
        return Holdable.Type.minionBread;
    }

    @Override
    public String toString() {
        return "minion-bread";
    }
}
