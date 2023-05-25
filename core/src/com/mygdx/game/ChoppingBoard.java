package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import static com.mygdx.game.Appliance.direction;

/**
 * Chopping Board class (extends abstract Appliance class)
 *
 * Created: May 25, 2023
 * Last Updated: May 25, 2023
 *
 * Notes:
 * width = tileWidth
 * height = tileHeight
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
