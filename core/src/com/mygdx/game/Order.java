package com.mygdx.game;

import com.mygdx.game.holdable.Holdable;

import java.util.List;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IdentityMap;

public class Order {
    IdentityMap<Holdable.Type, Boolean> ingredients;

    Order(List<Holdable.Type> ing) {
        ingredients = new IdentityMap<Holdable.Type, Boolean>();
        for (Holdable.Type ingredient : ing) {
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

    void mark(Holdable.Type ingredient) {
        if (ingredients.containsKey(ingredient)) {
            ingredients.put(ingredient, true);
        }
    }
    Array<Holdable.Type> neededIngredients() {
        Array<Holdable.Type> needed = new Array<Holdable.Type>();
        for (Holdable.Type ingredient : ingredients.keys()) {
            if (!ingredients.get(ingredient)) {
                needed.add(ingredient);
            }
        }
        return needed;
    }
}
