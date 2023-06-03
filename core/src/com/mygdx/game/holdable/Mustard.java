package com.mygdx.game.holdable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Mustard class (extends abstract class Ingredient)
 *
 * Created: June 1, 2023
 *
 * Notes:
 * Objects of this class are not holdable. They are added directly to sandwich from MustardBottle appliances.
 */
public class Mustard extends Ingredient {

    public Mustard() {
        super(new Texture(Gdx.files.internal("Ingredients/MustardLine.png")), 1);
    }

}
