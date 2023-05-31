package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.holdable.*;
import com.badlogic.gdx.audio.Sound;

/**
 * Crate class (extends abstract class Appliance)
 *
 * Created May 28, 2023
 */
public class Crate extends Appliance {

    Sound sound;

    public Crate(int x, int y, int width, int height, Ingredient.Type type)
    {
        super(null, x, y, width, height);

        sound = Gdx.audio.newSound(gdx.files.internal("Sounds/Crate.mp4"));

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

    public void dispose() {
        sound.dispose();
    }

    /**
     * Gives the player an ingredient if not holding anything
     *
     * @param ingr - Ingredient held by the player (null if nothing held)
     * @return copy of ingredient held by crate
     */
    @Override
    public Ingredient interact(Ingredient ingr) {
        if(ingr == null)
            return this.ingr.copy(this.ingr);
        else
            return ingr;
    }
}
