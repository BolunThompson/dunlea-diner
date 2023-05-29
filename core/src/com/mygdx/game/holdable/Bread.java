package com.mygdx.game.holdable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Bread extends Ingredient{

    private boolean toasted;

    public Bread() {
        super(new Texture(Gdx.files.internal("Ingredients/bread_sheet.png")), 2);
        toasted = false;
    }

    public boolean isToasted() {
        return toasted;
    }

    public void nextCostume() {
        super.nextCostume();
        toasted = true;
    }

    @Override
    public Type getType() {
        return Ingredient.Type.bread;
    }

    @Override
    public String toString() {
        return "bread";
    }

}
