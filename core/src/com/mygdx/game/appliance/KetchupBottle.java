package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.holdable.Bread;
import com.mygdx.game.holdable.Holdable;
import com.mygdx.game.holdable.Ketchup;
import com.mygdx.game.holdable.Sandwich;

/**
 * KetchupBottle class (extends abstract class Appliance)
 *
 * Created: June 1, 2023
 */
public class KetchupBottle extends Appliance {

    public KetchupBottle(int x, int y) {
        super(null, x, y, true);
        this.sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Ketchup.mp3"));
    }

    /**
     * Add ketchup to sandwich
     * Precondition: canInteract is true
     *
     * @param item - Sandwich held by the player (null if nothing held)
     */
    @Override
    public Holdable interact(Holdable item)
    {
        if(sound != null)
            sound.play(0.6f);

        if(item instanceof Sandwich)
            ((Sandwich)item).addIngr(new Ketchup());
        else if(item instanceof Bread) {
            item = new Sandwich((Bread)item, new Ketchup());
        }
        return item;
    }

    /**
     * Conditions checked:
     * (1) Item held by player is sandwich OR bread
     * (2) If item is sandwich, sandwich is not finished
     *
     * @param item - Item held by the player (null if nothing held)
     */
    @Override
    public boolean canInteract(Holdable item) {
        if (item != null && ((item instanceof Sandwich && !((Sandwich) item).isFinished()) || (item instanceof Bread && ((Bread)item).edible())))
            return true;
        else
            return false;
    }

}
