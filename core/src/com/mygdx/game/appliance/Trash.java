package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.holdable.Holdable;
import com.mygdx.game.holdable.Ingredient;

/**
 * Trash class (extends abstract class Appliance)
 *
 * Created May 28, 2023
 */
public class Trash extends Appliance{

    public Trash(int x, int y, int width, int height)
    {
        super(new Texture(Gdx.files.internal("Appliances/Trash.png")), x, y, width, height, Appliance.direction.UP);
    }

    /**
     * Remove ingredient held by player
     *
     * @param ingr - Item held by the player (null if nothing held)
     */
    @Override
    public Holdable interact(Holdable ingr)
    {
        return null;
    }
}
