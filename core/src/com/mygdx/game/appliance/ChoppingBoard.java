package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.holdable.*;
import com.mygdx.game.holdable.Ingredient;

/**
 * Chopping Board class (extends abstract class Appliance)
 *
 * Created: May 25, 2023
 */
public class ChoppingBoard extends Appliance{

    public ChoppingBoard(int x, int y)
    {
        super(new Texture(Gdx.files.internal("Appliances/Sprite-Cutting_Board_SPRITESHEET2.png")), x, y, true,4);
        this.sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Chopping.wav"));
    }

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
        }

        return temp;
    }

    /**
     * Conditions checked:
     * (1) Cutting board is empty or finished cutting.
     * (2) Item being put on cutting board is ham, cheese, lettuce, or tomato
     * (3) Item being put on cutting board is not already cut
     *
     * @param item - Ingredient held by the player (null if nothing held)
     */
    @Override
    public boolean canInteract(Holdable item)
    {
        return ((item == null
                || ((item instanceof Ham || item instanceof Cheese || item instanceof Lettuce || item instanceof Tomato) && ((Ingredient) item).getCostumeIndex() == 0)))
                && (!doAnimation || animation.isAnimationFinished(elapsedTime));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(animation.isAnimationFinished(elapsedTime))
            ((Ingredient)(this.item)).nextCostume(); // change ingredient to sliced sprite
    }

    @Override
    public void draw(Batch batch)
    {
        if(item != null && item instanceof Ingredient)
            batch.draw(((Ingredient)item).getTexture(), collisionRegion.x + collisionRegion.width*0.2f, collisionRegion.y + collisionRegion.height * 0.1f, collisionRegion.width, collisionRegion.height);
        super.draw(batch);
    }

}
