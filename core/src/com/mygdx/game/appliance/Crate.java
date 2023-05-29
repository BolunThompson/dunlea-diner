package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.holdable.*;

/**
 * Crate class (extends Appliance)
 *
 * Created May 28, 2023
 */
public class Crate extends Appliance {

    public Crate(int x, int y, int width, int height, Ingredient.Type type)
    {
        super(null, x, y, width, height);

        this.interactRegion = new Rectangle(x + width/4f, y - height/2f, width/2f, height*2f);

        switch(type)
        {
            case bread:
            default:
                this.texture = new Texture(Gdx.files.internal("Crates/PlainBreadCrate.png"));
                this.ingr = new Bread();
                break;
            case ham:
                this.texture = new Texture(Gdx.files.internal("Crates/HamCrate.png"));
                this.ingr = new Ham();
                break;
            case cheese:
                this.texture = new Texture(Gdx.files.internal("Crates/CheeseCrate.png"));
                this.ingr = new Cheese();
                break;
            case lettuce:
                this.texture = new Texture(Gdx.files.internal("Crates/LettuceCrate.png"));
                this.ingr = new Lettuce();
                break;
            case tomato:
                this.texture = new Texture(Gdx.files.internal("Crates/TomatoCrate.png"));
                this.ingr = new Tomato();
                break;
        }
    }

    /**
     * if player is holding item, return same item
     * if player is not holding item, return crate item
     */
    @Override
    public Ingredient interact(Ingredient ingr) {
        if(ingr == null)
            return this.ingr.copy(this.ingr);
        else
            return ingr;
    }
}
