package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.holdable.Bread;
import com.mygdx.game.holdable.Holdable;
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
        super(new Texture(Gdx.files.internal("Appliances/Sprite-Cooking_Toaster_SPRITESHEET.png")),
                x, y, width, height, Appliance.direction.DOWN, 5);
    }

    /**
     * Put bread in / take bread out of oven.
     * Precondition: canInteract is true
     *
     * @param item - Ingredient held by the player (null if nothing held)
     */
    @Override
    public Holdable interact(Holdable item)
    {
        Holdable temp = this.item;
        this.item = item;

        if(this.item != null) {
            elapsedTime = 0;
            doAnimation = true;
            ((Ingredient)(this.item)).nextCostume(); // toasts bread
        }

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
    public boolean canInteract(Holdable item)
    {
        if((item == null || (item != null && item instanceof Bread && !((Bread)item).isToasted())) && (!doAnimation || animation.isAnimationFinished(elapsedTime)))
                return true;
        else
            return false;
    }

}
