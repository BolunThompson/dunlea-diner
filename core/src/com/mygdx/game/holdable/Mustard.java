package com.mygdx.game.holdable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Ketchup class (extends abstract class Ingredient)
 *
 * Created: June 1, 2023
 *
 * Notes:
 * Objects of this class are not holdable. They are added directly to sandwich from KetchupBottle appliances.
 */
public class Mustard extends Ingredient {

    public Mustard() {
        super(new Texture(Gdx.files.internal("Ingredients/MustardSquiggle.png")), 1);
    }

}
