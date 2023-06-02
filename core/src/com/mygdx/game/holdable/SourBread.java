package com.mygdx.game.holdable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * SourBread class (extends class Bread)
 *
 * Created June 1, 2023
 */
public class SourBread extends Bread {

    public SourBread() {
        super(new Texture(Gdx.files.internal("Ingredients/sourBread_sheet.png")));
    }

    @Override
    public Type getType() {
        return Holdable.Type.sourBread;
    }

    @Override
    public String toString() {
        return "sour-bread";
    }
}
