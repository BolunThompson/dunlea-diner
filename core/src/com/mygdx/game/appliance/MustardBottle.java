package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.holdable.Bread;
import com.mygdx.game.holdable.Holdable;
import com.mygdx.game.holdable.Mustard;
import com.mygdx.game.holdable.Sandwich;

/**
 * MustardBottle class (extends abstract class Appliance)
 *
 * Created: June 1, 2023
 */
public class MustardBottle extends Appliance{

    public MustardBottle(int x, int y) {
        super(null, x, y, true);
        this.sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Ketchup.mp3"));
    }

    /**
     * Add mustard to sandwich
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
            ((Sandwich)item).addIngr(new Mustard());
        else if(item instanceof Bread) {
            item = new Sandwich((Bread)item, new Mustard());
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
        return item != null && ((item instanceof Sandwich && !((Sandwich) item).isFinished()) || (item instanceof Bread && ((Bread) item).edible()));
    }

}
