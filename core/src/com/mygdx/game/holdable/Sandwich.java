package com.mygdx.game.holdable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

/**
 * Sandwich class (extends abstract class Holdable)
 *
 * Created: May 29, 2023
 */
public class Sandwich extends Holdable{

    private Array<Ingredient> ingredients;
    private boolean finished;

    public Sandwich(Bread ingr1, Ingredient ingr2) {
        ingredients = new Array<Ingredient>();
        ingredients.add(ingr1);
        ingredients.add(ingr2);

        if(ingr2 instanceof Bread)
            finished = true;
    }

    /**
     * Add an ingredient to the sandwich. If bread is added, mark the sandwich as finished.
     *
     * @param ingrToAdd - Ingredient to be added to sandwich
     */
    public void addIngr(Ingredient ingrToAdd) {
        if(!finished) {
            ingredients.add(ingrToAdd);
            if(ingrToAdd instanceof Bread)
                finished = true;
        }
    }

    public boolean isFinished() {
        return finished;
    }

    /**
     * Returns true if the two sandwiches being compared have the same type & quantity of ingredients
     *
     * @param temp - the sandwich being compared to
     */
    public boolean equals(Sandwich temp) {
        return ingredients.containsAll(temp.ingredients, false) && temp.ingredients.containsAll(ingredients, false);
    }

    /**
     *  Unused - was trying to use it in Counter.java and Player.java to draw sandwich, but it didn't work for unknown reasons
     *  Should probably fix this later
     */
    public void draw(Batch batch, int x, int y, int width, int height) {
        System.out.println("too cool for school"); // testing (does not print)


        float i = 0;
        for(Ingredient ingr : ingredients) {
            batch.draw(ingr.getTexture(), x, y + i, width, height);
            i += height/8f;
        }

    }

    public void dispose() {
        for(Ingredient ingr : ingredients) {
            ingr.dispose();
        }
    }

    /**
     * Get methods
     */
    public Array<Ingredient> getIngredients() {
        return ingredients;
    }

}
