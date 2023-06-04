package com.mygdx.game;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.DayScreen.*;
import com.mygdx.game.appliance.*;
import com.mygdx.game.holdable.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DayState {
    final String mapFile;
    final String name;
    final int level;

    final int wantedOrders;
    public Array<Order> orders;
    public int orderIndex; // current, I think

    static final float maxTime = 120;
    float currentTime;

    final Optional<DayState> nextDay;

    private DayState(
            String mapFile,
            String name,
            Optional<DayState> nextDay,
            int wantedOrders,
            List<Ingredient> wantedIngredients,
            List<Bread> breads,
            int level) {
        this.mapFile = mapFile;
        this.name = name;
        this.nextDay = nextDay;
        this.wantedOrders = wantedOrders;
        this.level = level;

        orders = new Array<Order>();
        for (int i = 0; i < wantedOrders; i++) {
            int numIngredients = (int) (Math.random() * 4);
            // inefficient, but it works
            Collections.shuffle(wantedIngredients);
            ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>(
                    wantedIngredients.subList(0, numIngredients));
            Bread bread = breads.get((int) (Math.random() * breads.size()));
            orders.add(new Order(ingredients, bread));
        }
    }

    static DayState createLevels() {
        Optional<DayState> dayState = Optional.empty();
        for (int level = 5; level >= 1; level--) {
            String mapFile = String.format("Maps/day%dMap.tmx", level);
            String name = String.format("Day %d", level);
            ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
            ArrayList<Bread> breads = new ArrayList<Bread>();
            int wantedOrders;
            // might be bad code
            // but two switches: the first is fallthrough on purpose
            switch (level) {
                case 5:
                case 4:
                    breads.add(new MinionBread());
                case 3:
                    ingredients.add(new Mustard());
                    ingredients.add(new Ketchup());
                    breads.add(new SourBread());
                case 2:
                    breads.add(new WheatBread());
                case 1:
                    ingredients.add(new Ham());
                    ingredients.add(new Cheese());
                    ingredients.add(new Lettuce());
                    ingredients.add(new Tomato());
                    breads.add(new Bread());
            }
            switch (level) {
                case 5:
                    wantedOrders = 6;
                    break;
                case 4:
                    wantedOrders = 5;
                    break;
                case 3:
                    wantedOrders = 5;
                    break;
                case 2:
                    wantedOrders = 5;
                    break;
                case 1:
                    wantedOrders = 4;
                    break;
                default:
                    throw new RuntimeException("Invalid level");
            }
            dayState = Optional
                    .of(new DayState(mapFile, name, dayState, wantedOrders, ingredients, breads, level));
        }
        return dayState.get();
    }

    void reset() {
        this.currentTime = 0;
    }

    public boolean isOver() {
        return orders.isEmpty() || currentTime >= maxTime;
    }

    public boolean isWon() {
        return orders.isEmpty();
    }

    public void mark(Ingredient ing) {
        if (orders.isEmpty()) {
            return;
        }
        orders.get(orderIndex).mark(ing);
    }

    public boolean orderIsComplete() {
        if (orders.isEmpty()) {
            return true;
        }
        return orders.get(orderIndex).complete();
    }

    public void nextOrder() {
        if (orders.isEmpty()) {
            return;
        }
        orderIndex++;
    }

    public int ordersLeft() {
        return orders.size - orderIndex;
    }

    public int ordersCnt() {
        return orders.size;
    }

    public int score() {
        return 1 + (int) ((100 + (maxTime - currentTime) / maxTime * 1000) * (1 + (double) (level - 1) / 2));
    }

}
