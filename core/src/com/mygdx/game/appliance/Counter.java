package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.holdable.Bread;
import com.mygdx.game.holdable.Holdable;
import com.mygdx.game.holdable.Ingredient;
import com.mygdx.game.holdable.Sandwich;

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
     * Switches player ingredient & counter ingredient OR creates/adds ingredients to sandwich
     *
     * @param item - Item held by the player (null if nothing held)
     * @return Item held by counter (null if nothing held)
     */
    @Override
    public Holdable interact(Holdable item)
    {
        // switch items with player
        if((this.item instanceof Sandwich && ((Sandwich)this.item).isFinished()) || (item instanceof Sandwich && ((Sandwich)item).isFinished()))
            return super.interact(item);

        // add ingredient held by player into sandwich on counter
        if(this.item instanceof Sandwich && item instanceof Ingredient) {
            ((Sandwich)this.item).addIngr((Ingredient)item);
            return null;
        }

        // add ingredient on counter into sandwich held by player
        // (to be implemented here & in Player.java class)

        // create sandwich from bread on counter & ingredient held by player
        if(this.item instanceof Bread && item instanceof Ingredient) {
            this.item =  new Sandwich((Bread) this.item, (Ingredient) item);
            return null;
        }

        // create sandwich from bread held by player & ingredient on counter
        else if(item instanceof Bread && this.item instanceof Ingredient) {
            this.item = new Sandwich((Bread) item, (Ingredient) this.item);
            return null;
        }

        return super.interact(item);
    }

    @Override
    public void draw(Batch batch)
    {
        // draw item held by counter
        if(item != null) {
            // draw ingredient
            if(item instanceof Ingredient)
                batch.draw(((Ingredient)item).getTexture(), collisionRegion.x, collisionRegion.y, collisionRegion.width, collisionRegion.height);

            // draw sandwich
            if(item instanceof Sandwich) {
                float i = 0;
                for(Ingredient ingr : ((Sandwich)item).getIngredients()) {
                    batch.draw(ingr.getTexture(), collisionRegion.x, collisionRegion.y + i, collisionRegion.width, collisionRegion.height);
                    i += collisionRegion.height/8f;
                }
            }
        }
    }

}
