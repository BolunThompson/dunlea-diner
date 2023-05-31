package com.mygdx.game;

import com.mygdx.game.holdable.Ingredient;

import java.util.List;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IdentityMap;

class Order {
    IdentityMap<Ingredient.Type, Boolean> ingredients;

    Order(List<Ingredient.Type> ing) {
        ingredients = new IdentityMap<Ingredient.Type, Boolean>();
        for (Ingredient.Type ingredient : ing) {
            ingredients.put(ingredient, false);
        }
    }

    boolean complete() {
        for (Boolean ingredient : ingredients.values()) {
            if (!ingredient) {
                return false;
            }
        }
        return true;
    }

    void mark(Ingredient.Type ingredient) {
        if (ingredients.containsKey(ingredient)) {
            ingredients.put(ingredient, true);
        }
    }
    Array<Ingredient.Type> neededIngredients() {
        Array<Ingredient.Type> needed = new Array<Ingredient.Type>();
        for (Ingredient.Type ingredient : ingredients.keys()) {
            if (!ingredients.get(ingredient)) {
                needed.add(ingredient);
            }
        }
        return needed;
    }
}
