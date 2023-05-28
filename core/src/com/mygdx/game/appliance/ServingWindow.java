package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Ingredient;


/**
 * Trash class (extends Appliance)
 *
 * Created May 28, 2023
 *
 * When the player interacts with the serving window, player.interact() and ServingWindow.interact() are called
 * The player's ingredients slot is changed to empty, and nothing happens to the serving window's ingredient slot, which is by default empty.
 */
public class ServingWindow extends Appliance{

    public ServingWindow(int x, int y, int width, int height)
    {
        super(new Texture(Gdx.files.internal("Appliances/ServingWindow.png")), x, y, width, height, Appliance.direction.DOWN);
    }

    @Override
    public Ingredient interact(Ingredient ingr)
    {
        // add code for processing sandwich order
        // do not accept sandwich
        return null;
    }
}
