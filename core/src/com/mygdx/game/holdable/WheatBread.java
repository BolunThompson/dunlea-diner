package com.mygdx.game.holdable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * WheatBread class (extends class Bread)
 *
 * Created June 1, 2023
 */
public class WheatBread extends Bread {

    public WheatBread() {
        super(new Texture(Gdx.files.internal("Ingredients/wheatBread_sheet.png")));
    }

    @Override
    public Type getType() {
        return Holdable.Type.wheatBread;
    }

    @Override
    public String toString() {
        return "wheat-bread";
    }
}
