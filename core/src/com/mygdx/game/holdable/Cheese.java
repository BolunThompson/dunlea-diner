package com.mygdx.game.holdable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Cheese class (extends abstract class Ingredient)
 *
 * Created: May 29, 2023
 */
public class Cheese extends Ingredient {

    public Cheese() {
        super(new Texture(Gdx.files.internal("Ingredients/cheese.png")), 1);
    }

    @Override
    public Type getType() {
        return Ingredient.Type.cheese;
    }

    @Override
    public String toString() {
        return "cheese";
    }

}