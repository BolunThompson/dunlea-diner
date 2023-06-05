package com.mygdx.game.holdable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Ketchup class (extends abstract class Ingredient)
 *
 * Created: June 1, 2023
 *
 * Notes:
 * Objects of this class are not holdable. They are added directly to sandwich from KetchupBottle appliances.
 */
public class Ketchup extends Ingredient {

    public Ketchup() {
        super(new Texture(Gdx.files.internal("Ingredients/ketchupLine.png")), 1);
    }
    @Override
    public TextureRegion getIcon() {
        return new TextureRegion(new Texture(Gdx.files.internal("Ingredients/Ketchup.png")));
    }

}
