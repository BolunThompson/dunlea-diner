package com.mygdx.game.holdable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Bread class (extends abstract class Ingredient)
 *
 * Created: May 29, 2023
 */
public class Bread extends Ingredient{

    private boolean baked;

    public Bread() {
        super(new Texture(Gdx.files.internal("Ingredients/bread_sheet.png")), 3);
        baked = false;
    }

    public Bread(Texture texture) {
        super(texture, 2);
        baked = false;
    }

    public boolean isBaked() {
        return baked;
    }

    @Override
    public void nextCostume() {
        super.nextCostume();
        if(!baked)
            baked = true;
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
