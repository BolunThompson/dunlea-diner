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
            elapsedTime = 0;
            doAnimation = true;
            ((Sandwich)(this.item)).nextCostume(); // toasts bread
        }

        if(sound != null)
            sound.play(1.0f);

        return temp;
    }

    /**
     * Conditions checked:
     * (1) Ingredient being put in oven is bread
     * (2) Oven is empty or finished toasting.
     *
     * @param item - Ingredient held by the player (null if nothing held)
     */
    @Override
    public boolean canInteract(Holdable item) {
        if ((item == null || (item != null && item instanceof Sandwich && ((Sandwich) item).isFinished())) && (!doAnimation || animation.isAnimationFinished(elapsedTime)))
        {
            System.out.println("but like");
            return true;
        }
        else {
            System.out.println("no way");
            return false;
        }
    }
}
