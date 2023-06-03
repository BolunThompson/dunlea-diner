package com.mygdx.game;

import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.holdable.Bread;
import com.mygdx.game.holdable.Ingredient;

import java.util.List;


public class Order {
    // should be all private in principle, but idc to refactor it
    ObjectMap<Ingredient, Boolean> ingredients;
    // seems a bit janky -- is there a better way to handle bread?
    Bread bread;
    int breadCnt;
    // also janky
    boolean shouldBeGrilled;
    boolean grilled = false;

    Order(List<Ingredient> ing, Bread bread, boolean shouldBeGrilled) {
        this.bread = bread;
        this.shouldBeGrilled = shouldBeGrilled;
        ingredients = new ObjectMap<Ingredient, Boolean>();
        for (Ingredient ingredient : ing) {
            ingredients.put(ingredient, false);
        }
    }

    boolean complete() {
        for (Boolean ingredient : ingredients.values()) {
            if (!ingredient) {
                return false;
            }
        }
        return (!shouldBeGrilled || (shouldBeGrilled && grilled)) && breadCnt >= 2;
    }

    void mark(Ingredient ingredient) {
        if (ingredient.edible()) {
            // feels like bad code
            if (ingredient instanceof Bread) {
                if (this.bread.getType() == bread.getType()) {
                    breadCnt++;
                }
            } else if (ingredients.containsKey(ingredient)) {
                ingredients.put(ingredient, true);
            }
        }
    }

    public String toString() {
        String str = "";
        for (Ingredient ingredient : ingredients.keys()) {
            str += ingredient + ":" + ingredients.get(ingredient) + ", ";
        }
        str += " | " + bread + ":" + breadCnt;
        str += "-END";
        return str;
    }
}
