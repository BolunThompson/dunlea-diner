package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Ingredient {
    // can someone rename this stuff vv I feel like I named it very poorly

    public enum Type {
        bread, ham, cheese, lettuce, tomato
    }

    private Type name;
    private Texture texture;

    public Ingredient(Type name)
    {
        this.name = name;

        switch(name)
        {
            case bread:
            default:
                texture = new Texture(Gdx.files.internal("Ingredients/bread.png"));
                break;
            case ham:
                texture = new Texture(Gdx.files.internal("Ingredients/ham.png"));
                break;
            case cheese:
                texture = new Texture(Gdx.files.internal("Ingredients/cheese.png"));
                break;
            case lettuce:
                texture = new Texture(Gdx.files.internal("Ingredients/lettuce.png"));
                break;
            case tomato:
                texture = new Texture(Gdx.files.internal("Ingredients/tomato.png"));
                break;
        }
    }

    public Type getName() {
        return name;
    }
    public Texture getTexture()
    {
        return texture;
    }

    public void dispose()
    {
        texture.dispose();
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
            case ham:
                return "ham";
            case cheese:
                return "cheese";
            case lettuce:
                return "lettuce";
            case tomato:
                return "tomato";
            default:
                return "ingredient";
        }
    }
}
