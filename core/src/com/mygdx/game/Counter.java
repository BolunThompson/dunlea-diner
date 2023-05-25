package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Counter extends Appliance {

    public Counter(int x, int y, int width, int height)
    {
        super(new Texture(Gdx.files.internal("Appliances/Counter.png")), x, y, width, height);
    }
}
