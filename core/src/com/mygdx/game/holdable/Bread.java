package com.mygdx.game.holdable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Bread class (extends abstract class Ingredient)
 *
 * Created: May 29, 2023
 */
public class Bread extends Ingredient{

    private boolean toasted;

    public Bread() {
        super(new Texture(Gdx.files.internal("Ingredients/bread_sheet.png")), 2);
        toasted = false;
    }

    public boolean isToasted() {
        return toasted;
    }

    @Override
    public void nextCostume() {
        super.nextCostume();
        toasted = true;
    }

    @Override
    public Type getType() {
        return Holdable.Type.bread;
    }

    @Override
    public String toString() {
        return "bread";
    }

}
