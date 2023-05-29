package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.holdable.Ingredient;

/**
 * Toaster class (extends abstract Appliance class)
 *
 * Created: May 25, 2023
 *
 * Notes:
 * Add sprites for middle counters & edge counters?
 */
public class Counter extends Appliance {

    TextureRegion textureRegion;

    public Counter(int x, int y, int width, int height, Appliance.direction dir)
    {
        super(new Texture(Gdx.files.internal("Appliances/Counter.png")), x, y, width, height);

        //this.texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        //textureRegion = new TextureRegion(this.texture);
        switch(dir)
        {
            case LEFT: // horizontal counters
            case RIGHT:
                //textureRegion.setRegion(0,0, this.texture.getWidth() * length, this.texture.getHeight());
                interactRegion = new Rectangle(x + width/4f, y - height/2f, width/2f, height*2f);
                break;
            case UP: // vertical counters
            case DOWN:
                //textureRegion.setRegion(0, 0, this.texture.getWidth(), this.texture.getHeight() * length);
                interactRegion = new Rectangle(x - width/2f, y + height/4f, width*2f, height/2f);
                break;
        }
    }

    /**
     * When the player interacts with the counter, player.interact() and counter.interact() are called.
     * The player's ingredients slot is switched with the counter's ingredient slot.
     */
    @Override
    public Ingredient interact(Ingredient ingr)
    {
        // add code for ingredient combination to sandwich here
        return super.interact(ingr);
    }

    @Override
    public void draw(Batch batch)
    {
        if(ingr != null)
            batch.draw(ingr.getTexture(), getCollisionRegion().getX(), getCollisionRegion().getY(), getCollisionRegion().getWidth(), getCollisionRegion().getHeight());
    }
}
