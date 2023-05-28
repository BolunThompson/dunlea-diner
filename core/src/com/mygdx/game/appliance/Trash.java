package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Ingredient;

/**
 * Trash class (extends Appliance)
 *
 * Created May 28, 2023
 *
 * When the player interacts with the trash, player.interact() and trash.interact() are called
 * The player's ingredients slot is changed to empty, and nothing happens to the trash's ingredient slot, which is by default empty.
 */
public class Trash extends Appliance{

    public Trash(int x, int y, int width, int height)
    {
        super(new Texture(Gdx.files.internal("Appliances/Trash.png")), x, y, width, height, Appliance.direction.UP);
    }

    @Override
    public void interact(Ingredient ingr)
    {
        //ingr = null;
    }
}
