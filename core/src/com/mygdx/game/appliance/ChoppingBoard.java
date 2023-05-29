package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
        super(new Texture(Gdx.files.internal("Appliances/Sprite-Empty_CuttingBoard.png")), x, y, width, height, Appliance.direction.RIGHT);
    }

    @Override
    public Ingredient interact(Ingredient ingr)
    {
        return null;
    }
}
