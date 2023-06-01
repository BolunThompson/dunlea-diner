package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
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
        // add ingredient held by player into sandwich on counter
        if(this.item instanceof Sandwich && item instanceof Ingredient) {
            ((Sandwich)this.item).addIngr((Ingredient)item);
            return null;
        }

        // combine two sandwich halves
        else if(this.item instanceof Sandwich && item instanceof Sandwich && !((Sandwich)this.item).isFinished() && !((Sandwich)item).isFinished()) {
            Array<Ingredient> temp = ((Sandwich) item).getIngredients();
            temp.reverse();

            ArrayIterator<Ingredient> iter = new ArrayIterator<Ingredient>(temp);
            while(iter.hasNext()) {
                ((Sandwich)this.item).addIngr(iter.next());
            }
            return null;
        }

        // add ingredient on counter into sandwich held by player
        // (to be implemented here & in Player.java class)

        // create sandwich from bread on counter & ingredient held by player
        else if(this.item instanceof Bread && item instanceof Ingredient) {
            this.item =  new Sandwich((Bread) this.item, (Ingredient) item);
            return null;
        }

        // create sandwich from bread held by player & ingredient on counter
        else if(item instanceof Bread && this.item instanceof Ingredient) {
            this.item = new Sandwich((Bread) item, (Ingredient) this.item);
            return null;
        }

        // switch items with player
        return super.interact(item);
    }

    @Override
    public void draw(Batch batch)
    {
        // draw item held by counter
        if(item != null) {
            item.draw(batch, collisionRegion.x, collisionRegion.y, collisionRegion.width, collisionRegion.height);
        }
    }

}
