package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.holdable.Bread;
import com.mygdx.game.holdable.Holdable;
import com.mygdx.game.holdable.Ingredient;
import com.badlogic.gdx.audio.Sound;

/**
 * Toaster class (extends abstract class Appliance)
 *
 * Created: May 25, 2023
 */
public class Toaster extends Appliance {

    Sound sound2;

    public Toaster(int x, int y, int width, int height)
    {
        super(new Texture(Gdx.files.internal("Appliances/Sprite-Cooking_Toaster_SPRITESHEET.png")),
                x, y, width, height, Appliance.direction.DOWN, 5);


        this.sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Oven.mp3"));
        sound2 = Gdx.audio.newSound(Gdx.files.internal("Sounds/Toaster-pop-up.wav"));
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
            if(sound != null)
                sound.play(0.5f);

            elapsedTime = 0;
            doAnimation = true;
            ((Ingredient)(this.item)).nextCostume(); // toasts bread
        }

        if(sound2 != null)
            sound2.play(1.0f);

        return temp;
    }

    /**
     * Conditions checked:
     * (1) Item being put in oven is bread
     * (2) Bread is raw dough (NOT baked)
     * (2) Oven is empty or finished toasting.
     *
     * @param item - Item held by the player (null if nothing held)
     */
    @Override
    public boolean canInteract(Holdable item)
    {
        if((item == null || (item != null && item instanceof Bread && !((Bread)item).edible())) && (!doAnimation || animation.isAnimationFinished(elapsedTime)))
                return true;
        else
            return false;
    }

    @Override
    public void dispose() {
        super.dispose();
        sound2.dispose();
    }

}
