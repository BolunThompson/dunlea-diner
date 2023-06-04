package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.holdable.Bread;
import com.mygdx.game.holdable.Cheese;
import com.mygdx.game.holdable.Holdable;
import com.mygdx.game.holdable.Ingredient;
import com.mygdx.game.holdable.Sandwich;
import com.mygdx.game.DayState;

/**
 * ServingWindow class (extends abstract class Appliance)
 *
 * Created May 28, 2023
 *
 * The serving window is a 2x1 tile
 */
public class ServingWindow extends Appliance {

    private final DayState day;

    public ServingWindow(int x, int y, final DayState day) {
        super(new Texture(Gdx.files.internal("Appliances/ServingWindow.png")), x, y, true);
        this.day = day;

        this.collisionRegion = new Rectangle(x * width, y * height, width * 2, height);
        this.interactRegion = new Rectangle(x * width + width/4f, y * height - height/2f, width*2f, height*2f); // vert

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
    public Holdable interact(Holdable item) {
        if (sound != null)
            sound.play(1.0f);

        // pseudocode: if DayScreen.currentOrder.equals((Sandwich)item)
        // increment number of correct orders
        // currentOrder = (generate a new order)
        // feels like a hack
        if (item instanceof Sandwich) {
                        Sandwich sandwich = (Sandwich) item;
            for (Ingredient ingredient : sandwich.getIngredients()) {
                day.mark(ingredient);
            }
                                    if (day.orderIsComplete()) {
                day.nextOrder();
                            }
            return null;
        }

        return item;
    }

    /**
     * Returns whether an item can be submitted at the serving window.
     * TRUE if item is a sandwich. FALSE otherwise. (not functional yet)
     *
     * @param item - Item held by the player (null if nothing held)
     */
    @Override
    public boolean canInteract(Holdable item) {
        if (item instanceof Sandwich)
            return true;
        else {
            return false;
        }
    }
}
