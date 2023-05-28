package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Chopping Board class (extends abstract Appliance class)
 *
 * Created: May 25, 2023
 * Last Updated: May 25, 2023
 *
 */
public class ChoppingBoard extends Appliance{

    public ChoppingBoard(int x, int y, int width, int height)
    {
        super(new Texture(Gdx.files.internal("Appliances/Sprite-Empty_CuttingBoard.png")), x, y, width, height, Appliance.direction.RIGHT);
    }

    public void interact()
    {

    }
}
