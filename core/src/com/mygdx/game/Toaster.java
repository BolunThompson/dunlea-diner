package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Toaster class (extends abstract Appliance class)
 *
 * Created: May 25, 2023
 * Last Updated: May 25, 2023
 *
 * Notes:
 * width = tileWidth
 * height = tileHeight
 */
public class Toaster extends Appliance {



    public Toaster(int x, int y, int width, int height)
    {
        super(new Texture(Gdx.files.internal("Misc/Sprite-Empty_Toaster.png")), x, y, width, height, Appliance.direction.DOWN);
    }

    public void interact()
    {
        doAnimation = true;
    }
}
