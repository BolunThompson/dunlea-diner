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
 *
 * No functions yet.
 */
public class ChoppingBoard extends Appliance{

    public ChoppingBoard(int x, int y, int width, int height)
    {
        super(new Texture(Gdx.files.internal("Appliances/Sprite-Cutting_Board_SPRITESHEET.png")),
                x, y, width, height, Appliance.direction.RIGHT, 4);

        this.sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Chopping.wav"));
    }

    @Override
    public Holdable interact(Holdable item)
    {
        Holdable temp = this.item;
        this.item = item;

        if(this.item != null) {
            elapsedTime = 0;
            doAnimation = true;
            ((Ingredient)(this.item)).nextCostume(); // cuts item
        }

        if(sound != null)
            sound.play(1.0f);

        return temp;
    }

    /**
     * Conditions checked:
     * (1) Item being put on cutting board is cheese, lettuce, or tomato
     * (2) Cutting board is empty or finished cutting.
     *
     * @param item - Ingredient held by the player (null if nothing held)
     */
    @Override
    public boolean canInteract(Holdable item)
    {
        if((item == null || (item != null && (item instanceof Ham || item instanceof Cheese || item instanceof Lettuce || item instanceof Tomato))) && (!doAnimation || animation.isAnimationFinished(elapsedTime)))
            return true;
        else
            return false;
    }

    @Override
    public void draw(Batch batch)
    {
        super.draw(batch);
        if(item != null && item instanceof Ingredient)
            batch.draw(((Ingredient)item).getTexture(), getCollisionRegion().getX(), getCollisionRegion().getY(), getCollisionRegion().getWidth(), getCollisionRegion().getHeight());
    }

}
