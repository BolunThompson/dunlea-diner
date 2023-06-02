package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.holdable.Bread;
import com.mygdx.game.holdable.Holdable;
import com.mygdx.game.holdable.Ingredient;
import com.mygdx.game.holdable.Sandwich;

/**
 * FryingPan class (extends abstract class Appliance)
 *
 * Created: June 1, 2023
 */
public class FryingPan extends Appliance {

    public FryingPan(int x, int y, int width, int height) {
        super(new Texture(Gdx.files.internal("Appliances/FryingPan_SPRITESHEET.png")),
                x, y, width, height, Appliance.direction.RIGHT, 5);

        this.sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Frying.mp3"));
    }

    /**
     * Put sandwich on/take sandwich off of frying pan
     * Precondition: canInteract is true
     *
     * @param item - Sandwich held by the player (null if nothing held)
     */
    @Override
    public Holdable interact(Holdable item)
    {
        Holdable temp = this.item;
        this.item = item;

        if(this.item != null) {
            if(sound != null)
                sound.play(1.0f);

            elapsedTime = 0;
            doAnimation = true;
            ((Sandwich)(this.item)).nextCostume(); // toasts bread
        }

        return temp;
    }

    /**
     * Conditions checked:
     * (1) Item being put on frying pan is sandwich
     * (2) Sandwich is finished
     * (3) Frying pan is not holding an item OR finished frying current item
     *
     * @param item - Item held by the player (null if nothing held)
     */
    @Override
    public boolean canInteract(Holdable item) {
        if ((item == null || (item != null && item instanceof Sandwich && ((Sandwich) item).isFinished()) && !((Sandwich)item).isToasted()) && (!doAnimation || animation.isAnimationFinished(elapsedTime)))
            return true;
        else
            return false;
    }

}
