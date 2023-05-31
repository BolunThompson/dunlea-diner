package com.mygdx.game.holdable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Tomato class (extends abstract class Ingredient)
 *
 * Created: May 29, 2023
 */
public class Tomato extends Ingredient {

    public Tomato() {
        super(new Texture(Gdx.files.internal("Ingredients/tomato_sheet.png")), 2);
    }

    @Override
    public Type getType() {
        return Ingredient.Type.tomato;
    }

    @Override
    public String toString() {
        return "tomato";
    }

}
