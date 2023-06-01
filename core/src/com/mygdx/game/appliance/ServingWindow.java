package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.holdable.Holdable;
import com.mygdx.game.holdable.Ingredient;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.holdable.Sandwich;


/**
 * ServingWindow class (extends abstract class Appliance)
 *
 * Created May 28, 2023
 *
 * The serving window is a 2x1 tile
 * NOT FUNCTIONAL YET
 */
public class ServingWindow extends Appliance{

    public ServingWindow(int x, int y, int width, int height)
    {
        super(new Texture(Gdx.files.internal("Appliances/ServingWindow.png")), x, y, width, height, Appliance.direction.DOWN);

        this.texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        this.sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Ring.mp3"));
    }

    /**
     * Processes a submitted sandwich order by comparing to sandwich in orders list
     *
     * Precondition: item is a Sandwich
     *
     * @param item - Sandwich held by the player (null if nothing held)
     */
    @Override
    public Holdable interact(Holdable item)
    {
        if(sound != null)
            sound.play(1.0f);

        // add code for processing sandwich order
        // pseudocode: if DayScreen.currentOrder.equals((Sandwich)item)
        //                  increment number of correct orders
        //                  currentOrder = (generate a new order)

        return null;
    }

    /**
     * Returns whether an item can be submitted at the serving window.
     * TRUE if item is a sandwich. FALSE otherwise. (not functional yet)
     *
     * @param item - Item held by the player (null if nothing held)
     */
    @Override
    public boolean canInteract(Holdable item)
    {
        if(item instanceof Sandwich)
            return true;
        else
            return false;
    }

    /**public void draw(Batch batch)
    {
        if(ingr != null)
            batch.draw(ingr.getTexture(), getCollisionRegion().getX(), getCollisionRegion().getY(), getCollisionRegion().getWidth(), getCollisionRegion().getHeight());
    }**/
}
