package com.mygdx.game.holdable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Lettuce class (extends abstract class Ingredient)
 *
 * Created: May 29, 2023
 */
public class Lettuce extends Ingredient {

    public Lettuce() {
        super(new Texture(Gdx.files.internal("Ingredients/lettuce_sheet.png")), 2);
    }

    @Override
    public Ingredient.Type getType() {
        return Ingredient.Type.lettuce;
    }

    @Override
    public String toString() {
        return "lettuce";
    }

}
