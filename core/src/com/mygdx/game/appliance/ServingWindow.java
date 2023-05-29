package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.Ingredient;


/**
 * Trash class (extends Appliance)
 *
 * Created May 28, 2023
 *
 * The serving window is a 2x1 tile
 *
 * When the player interacts with the serving window, player.interact() and ServingWindow.interact() are called
 * The player's ingredients slot is changed to empty, and nothing happens to the serving window's ingredient slot, which is by default empty.
 */
public class ServingWindow extends Appliance{

    public ServingWindow(int x, int y, int width, int height)
    {
        super(new Texture(Gdx.files.internal("Appliances/ServingWindow.png")), x, y, width, height, Appliance.direction.DOWN);

        this.texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    @Override
    public Ingredient interact(Ingredient ingr)
    {
        // add code for processing sandwich order
        // do not accept sandwich
        return null;
    }

    public void draw(Batch batch)
    {
        if(ingr != null)
            batch.draw(ingr.getTexture(), getCollisionRegion().getX(), getCollisionRegion().getY(), getCollisionRegion().getWidth(), getCollisionRegion().getHeight());
    }
}
