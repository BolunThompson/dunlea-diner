package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.holdable.Holdable;
import com.mygdx.game.holdable.Mustard;
import com.mygdx.game.holdable.Sandwich;

/**
 * MustardBottle class (extends abstract class Appliance)
 *
 * Created: June 1, 2023
 */
public class MustardBottle extends Appliance{

    public MustardBottle(int x, int y, int width, int height) {
        super(null, x, y, width, height, Appliance.direction.UP);

        this.sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Ketchup.wav"));
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
            sound.play(1.0f);

        ((Sandwich)item).addIngr(new Mustard());
        return item;
    }

    /**
     * Conditions checked:
     * (1) Ingredient held by player is sandwich
     * (2) Sandwich is not finished
     *
     * @param item - Item held by the player (null if nothing held)
     */
    @Override
    public boolean canInteract(Holdable item) {
        if ((item == null || (item != null && item instanceof Sandwich && !((Sandwich) item).isFinished())))
            return true;
        else
            return false;
    }

}
