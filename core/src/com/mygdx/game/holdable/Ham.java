package com.mygdx.game.holdable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Ham extends Ingredient {

    public Ham() {
        super(new Texture(Gdx.files.internal("Ingredients/ham.png")), 1);
    }

    @Override
    public Type getType() {
        return Ingredient.Type.ham;
    }

    @Override
    public String toString() {
        return "ham";
    }

}
