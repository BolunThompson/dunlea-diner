package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Ingredient;

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

    public Counter(int x, int y, int width, int height, int length, Appliance.direction dir)
    {
        super(new Texture(Gdx.files.internal("Appliances/Counter.png")), x, y, width, height);

        this.texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        textureRegion = new TextureRegion(this.texture);
        switch(dir)
        {
            case LEFT:
            case RIGHT:
                textureRegion.setRegion(0,0, this.texture.getWidth() * length, this.texture.getHeight());
                break;
            case UP:
            case DOWN:
                textureRegion.setRegion(0, 0, this.texture.getWidth(), this.texture.getHeight() * length);
                break;
        }
    }

    /**
     * When the player interacts with the counter, player.interact() and counter.interact() are called.
     * The player's ingredients slot is switched with the counter's ingredient slot.
     */
    @Override
    public void interact(Ingredient ingr)
    {
        // to be implemented
    }

    @Override
    public void draw(Batch batch)
    {
        batch.draw(textureRegion, getCollisionRegion().getX(), getCollisionRegion().getY(), getCollisionRegion().getWidth(), getCollisionRegion().getHeight());
        if(ingr != null)
            batch.draw(ingr.getTexture(), getCollisionRegion().getX(), getCollisionRegion().getY(), getCollisionRegion().getWidth(), getCollisionRegion().getHeight());
    }
}
