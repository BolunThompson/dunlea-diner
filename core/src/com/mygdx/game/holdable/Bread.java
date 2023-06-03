package com.mygdx.game.holdable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Bread class (extends abstract class Ingredient)
 *
 * Created: May 29, 2023
 */
public class Bread extends Ingredient{

    public Bread() {
        super(new Texture(Gdx.files.internal("Ingredients/bread_sheet.png")), 3, false);
    }

    public Bread(Texture texture) {
        super(texture, 2, false);
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
