package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.holdable.Holdable;

/**
 * Trash class (extends abstract class Appliance)
 *
 * Created May 28, 2023
 */
public class Trash extends Appliance{

    public Trash(int x, int y)
    {
        super(new Texture(Gdx.files.internal("Appliances/Trash.png")), x, y, true);
        this.sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Trash.mp3"));
    }

    /**
     * Remove item held by player
     *
     * @param ingr - Item held by the player (null if nothing held)
     */
    @Override
    public Holdable interact(Holdable ingr)
    {
        if(sound != null)
            sound.play(1.0f);
        return null;
    }
}
