package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Ingredient {
    // can someone rename this stuff vv I feel like I named it very poorly

    public enum Type {
        bread
    }

    Type name;
    Texture texture;

    public Ingredient(Type name)
    {
        this.name = name;

        switch(name)
        {
            case bread:
                texture = new Texture(Gdx.files.internal("Ingredients/bread.png"));
                break;
        }
    }

    public void dispose()
    {
        texture.dispose();
    }

    public Texture getTexture()
    {
        return texture;
    }

    /**
     * toString method
     *              ,-._
     *            _.-'  '--.
     *          .'      _  -`\_
     *         / .----.`_.'----'
     *         ;/     `
     *        /_;
     *   for testing porpoises
     */
    public String toString()
    {
        switch(name)
        {
            case bread:
                return "bread";
        }
        return "whatever";
    }
}
