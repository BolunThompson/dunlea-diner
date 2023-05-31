package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.holdable.Holdable;
import com.mygdx.game.holdable.Ingredient;

/**
 * Toaster class (extends abstract class Appliance)
 *
 * Created: May 25, 2023
 *
 * Notes:
 * Need to add sprites for middle counters & edge counters in tilemap
 */
public class Counter extends Appliance {

    public Counter(int x, int y, int width, int height, Appliance.direction dir)
    {
        super(new Texture(Gdx.files.internal("Appliances/Counter.png")), x, y, width, height);

        switch(dir)
        {
            // horizontal counters (interact from above or below)
            case LEFT:
            case RIGHT:
                interactRegion = new Rectangle(x + width/4f, y - height/2f, width/2f, height*2f);
                break;

            // vertical counters (interact from left or right)
            case UP:
            case DOWN:
                interactRegion = new Rectangle(x - width/2f, y + height/4f, width*2f, height/2f);
                break;
        }
    }

    /**
     * Switches player ingredient & counter ingredient
     *
     * To be added: combine ingredients to make sandwich & add ingredient into sandwich on counter
     *
     * @param item - Item held by the player (null if nothing held)
     * @return Item held by counter (null if nothing held)
     */
    @Override
    public Holdable interact(Holdable item)
    {
        // add code for ingredient combination & sandwich here
        return super.interact(item);
    }

    @Override
    public void draw(Batch batch)
    {
        // draw item held by counter
        if(item != null && item instanceof Ingredient)
            batch.draw(((Ingredient)item).getTexture(), getCollisionRegion().getX(), getCollisionRegion().getY(), getCollisionRegion().getWidth(), getCollisionRegion().getHeight());
    }
}
