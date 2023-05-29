package com.mygdx.game.holdable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Tomato extends Ingredient {

    public Tomato() {
        super(new Texture(Gdx.files.internal("Ingredients/tomato.png")), 1);
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
