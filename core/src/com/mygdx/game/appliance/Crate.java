package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Ingredient;

/**
 * Crate class (extends Appliance)
 *
 * Created May 28, 2023
 *
 * Notes:
 * Currently only has bread crate
 * Interactions only work from below crate. May change this later
 */
public class Crate extends Appliance {

    public Crate(int x, int y, int width, int height)
    {
        super(new Texture(Gdx.files.internal("Crates/PlainBreadCrate.png")), x, y, width, height, Appliance.direction.DOWN);

        this.ingr = new Ingredient(Ingredient.Type.bread);
    }

    /**
     * When the player interacts with the crate, player.interact() and crate.interact() are called
     * The player's ingredients slot is changed to the type of ingredient that the crate has,
     * and nothing happens to the crate's ingredient slot (which is set in the constructor based on the type of crate)
     */
    @Override
    public Ingredient interact(Ingredient ingr) {
        return this.ingr;
    }
}
