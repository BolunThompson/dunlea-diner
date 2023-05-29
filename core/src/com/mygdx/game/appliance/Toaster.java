package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.holdable.Ingredient;

/**
 * Toaster class (extends abstract class Appliance)
 *
 * Created: May 25, 2023
 *
 * Notes:
 * Currently can only remove item from toasting after waiting 1 second after "DONE" shows up.
 * Need to alter so that bread can be removed from toaster as soon as "DONE" shows up.
 */
public class Toaster extends Appliance {

    public Toaster(int x, int y, int width, int height)
    {
        super(new Texture(Gdx.files.internal("Appliances/Sprite-Empty_Toaster.png")),
                new Texture(Gdx.files.internal("Appliances/Sprite-Cooking_Toaster_SPRITESHEET.png")),
                x, y, width, height, Appliance.direction.DOWN);
    }

    /**
     * Put bread in / take bread out of oven.
     * Precondition: canInteract is true
     *
     * @param ingr - Ingredient held by the player (null if nothing held)
     */
    @Override
    public Ingredient interact(Ingredient ingr)
    {
        Ingredient temp = this.ingr;
        this.ingr = ingr;

        if(this.ingr != null) {
            elapsedTime = 0;
            doAnimation = true;
            this.ingr.nextCostume(); // toasts bread
        }

        return temp;
    }

    /**
     * Conditions checked:
     * (1) Ingredient being put in oven is bread
     * (2) Oven is empty or finished toasting.
     *
     * @param ingr - Ingredient held by the player (null if nothing held)
     */
    @Override
    public boolean canInteract(Ingredient ingr)
    {
        if((ingr == null || (ingr != null && ingr.getType().equals(Ingredient.Type.bread))) && (!doAnimation || animation.isAnimationFinished(elapsedTime)))
            return true;
        else
            return false;
    }
}
