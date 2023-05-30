package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.holdable.Ingredient;
import com.badlogic.gdx.audio.Sound;

/**
 * Trash class (extends abstract class Appliance)
 *
 * Created May 28, 2023
 */
public class Trash extends Appliance{

    Sound sound;

    public Trash(int x, int y, int width, int height)
    {
        super(new Texture(Gdx.files.internal("Appliances/Trash.png")), x, y, width, height, Appliance.direction.UP);
        sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Trash.mp4"));
    }

    public void dispose() {
        sound.dispose();
    }

    /**
     * Remove ingredient held by player
     *
     * @param ingr - Ingredient held by the player (null if nothing held)
     */
    @Override
    public Ingredient interact(Ingredient ingr)
    {
        return null;
    }
}
