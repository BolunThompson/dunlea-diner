package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
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

    public Crate(int x, int y, int width, int height, Ingredient.Type type)
    {
        super(null, x, y, width, height);

        this.interactRegion = new Rectangle(x + width/4, y - height/2, width/2f, height*2f);

        switch(type)
        {
            case bread:
            default:
                this.texture = new Texture(Gdx.files.internal("Crates/PlainBreadCrate.png"));
                this.ingr = new Ingredient(Ingredient.Type.bread);
                break;
            case ham:
                this.texture = new Texture(Gdx.files.internal("Crates/HamCrate.png"));
                this.ingr = new Ingredient(Ingredient.Type.ham);
                break;
            case cheese:
                this.texture = new Texture(Gdx.files.internal("Crates/CheeseCrate.png"));
                this.ingr = new Ingredient(Ingredient.Type.cheese);
                break;
            case lettuce:
                this.texture = new Texture(Gdx.files.internal("Crates/LettuceCrate.png"));
                this.ingr = new Ingredient(Ingredient.Type.lettuce);
                break;
            case tomato:
                this.texture = new Texture(Gdx.files.internal("Crates/TomatoCrate.png"));
                this.ingr = new Ingredient(Ingredient.Type.tomato);
                break;
        }
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
