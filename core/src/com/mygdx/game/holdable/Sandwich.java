package com.mygdx.game.holdable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

/**
 * Sandwich class
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
    }

    public void addIngr(Ingredient ingrToAdd) {
        if(!finished) {
            if(ingrToAdd.getType().equals(Ingredient.Type.bread))
                finished = true;
            ingredients.add(ingrToAdd);
        }
    }

    public boolean equals(Sandwich temp) {
        return ingredients.containsAll(temp.ingredients, false) && temp.ingredients.containsAll(ingredients, false);
    }

    public Array<Ingredient> getIngredients() {
        return ingredients;
    }

    public void draw(Batch batch, int x, int y, int width, int height) {
        float i = 0;
        for(Ingredient ingr : ingredients) {
            batch.draw(ingr.getTexture(), x, y + i, width, height);
            i += height/8;
        }
    }
}
