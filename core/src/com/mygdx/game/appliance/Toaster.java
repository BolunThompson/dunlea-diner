package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.holdable.Ingredient;

/**
 * Toaster class (extends abstract Appliance class)
 *
 * Created: May 25, 2023
 *
 * Notes:
 * Animation does not reset after first interaction.
 * Interaction with toaster still works while in process of cooking animation
 */
public class Toaster extends Appliance {

    public Toaster(int x, int y, int width, int height)
    {
        super(new Texture(Gdx.files.internal("Appliances/Sprite-Empty_Toaster.png")),
                new Texture(Gdx.files.internal("Appliances/Sprite-Cooking_Toaster_SPRITESHEET.png")),
                x, y, width, height, Appliance.direction.DOWN);
    }

    @Override
    public Ingredient interact(Ingredient ingr) // precondition: canInteract is true
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

    @Override
    public boolean canInteract(Ingredient ingr)
    {
        if((ingr == null || (ingr != null && ingr.getType().equals(Ingredient.Type.bread))) && (!doAnimation || animation.isAnimationFinished(elapsedTime)))
            return true;
        else
            return false;
    }
}
