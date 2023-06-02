package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.holdable.*;
import com.badlogic.gdx.audio.Sound;

/**
 * Crate class (extends abstract class Appliance)
 *
 * Created May 28, 2023
 */
public class Crate extends Appliance {
    
    public Crate(int x, int y, int width, int height, Holdable.Type type)
    {
        super(null, x, y, width, height);

        this.interactRegion = new Rectangle(x + width/4f, y - height/2f, width/2f, height*2f);
        this.sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Crate.mp3"));

        switch(type)
        {
            case bread:
            default:
                //this.texture = new Texture(Gdx.files.internal("Crates/PlainBreadCrate.png"));
                this.item = new Bread();
                break;
            case wheatBread:
                this.item = new WheatBread();
                break;
            case sourBread:
                this.item = new SourBread();
                break;
            case minionBread:
                this.item = new MinionBread();
                break;

            case ham:
                //this.texture = new Texture(Gdx.files.internal("Crates/HamCrate.png"));
                this.item = new Ham();
                break;
            case cheese:
                //this.texture = new Texture(Gdx.files.internal("Crates/CheeseCrate.png"));
                this.item = new Cheese();
                break;
            case lettuce:
                //this.texture = new Texture(Gdx.files.internal("Crates/LettuceCrate.png"));
                this.item = new Lettuce();
                break;
            case tomato:
                //this.texture = new Texture(Gdx.files.internal("Crates/TomatoCrate.png"));
                this.item = new Tomato();
                break;
        }
    }

    /**
     * Gives the player an ingredient if not holding anything
     *
     * Precondition: Item dispensed by crate is an Ingredient
     *
     * @param item - Item held by the player (null if nothing held)
     * @return copy of ingredient held by crate
     */
    @Override
    public Holdable interact(Holdable item) {
        if(item == null) {
            sound.play(1.0f);
            return ((Ingredient) (this.item)).copy((Ingredient) this.item);
        }
        else
            return item;
    }
}
