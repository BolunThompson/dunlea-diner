package com.mygdx.game.holdable;

import com.badlogic.gdx.utils.Array;

/**
 * Sandwich class
 *
 * Created: May 29, 2023
 */
public class Sandwich {

    private Array<Ingredient> ingredients;

    public Sandwich(Ingredient ingr1, Ingredient ingr2) {
        ingredients = new Array<Ingredient>();
        ingredients.add(ingr1);
        ingredients.add(ingr2);
    }

    public void addIngr(Ingredient ingrToAdd) {
        ingredients.add(ingrToAdd);
    }

    public boolean equals(Sandwich temp) {
        return ingredients.containsAll(temp.ingredients, false) && temp.ingredients.containsAll(ingredients, false);
    }

    public Array<Ingredient> getIngredients() {
        return ingredients;
    }
}
