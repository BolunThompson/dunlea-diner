package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.holdable.*;

/**
 * Crate class (extends abstract class Appliance)
 *
 * Created May 28, 2023
 */
public class Crate extends Appliance {
    
    public Crate(int x, int y, Holdable.Type type)
    {
        super(null, x, y, true);
        this.sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Crate.mp3"));

        switch(type)
        {
            case bread:
            default:
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
                this.item = new Ham();
                break;
            case cheese:
                this.item = new Cheese();
                break;
            case lettuce:
                this.item = new Lettuce();
                break;
            case tomato:
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
