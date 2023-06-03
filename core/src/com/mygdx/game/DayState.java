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

    Array<Appliance> apps;

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
        this.apps = getApps(level);
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
                    wantedOrders = 10;
                    break;
                case 4:
                    wantedOrders = 10;
                    break;
                case 3:
                    wantedOrders = 10;
                    break;
                case 2:
                    wantedOrders = 10;
                    break;
                case 1:
                    wantedOrders = 10;
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

    public Array<Appliance> getDay1Apps() {
        int tileWidth = 100;
        int tileHeight = 100;

        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(tileWidth * 3, tileHeight * 6, tileWidth, tileHeight, Appliance.direction.UP)); // left top
                                                                                                             // counter
        apps.add(new Counter(tileWidth * 3, tileHeight * 3, tileWidth, tileHeight, Appliance.direction.UP)); // left
                                                                                                             // bottom
                                                                                                             // counter
        for (int i = 0; i < 5; i++) { // bottom counters
            apps.add(
                    new Counter(tileWidth * (6 + i), tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT));
        }
        apps.add(new Counter(tileWidth * 8, tileHeight * 8, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top
                                                                                                                // left
                                                                                                                // counter
        apps.add(new Counter(tileWidth * 11, tileHeight * 8, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top
                                                                                                                 // right
                                                                                                                 // counter

        apps.add(new Crate(tileWidth * 6, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.bread)); // bread
                                                                                                        // container
        apps.add(new Crate(tileWidth * 7, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.ham)); // ham container
        apps.add(new Crate(tileWidth * 8, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.cheese)); // cheese
                                                                                                         // container
        apps.add(new Crate(tileWidth * 9, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.lettuce)); // lettuce
                                                                                                          // container
        apps.add(new Crate(tileWidth * 10, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.tomato)); // tomato
                                                                                                          // container

        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 5, tileWidth, tileHeight)); // top cutting board
        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 4, tileWidth, tileHeight)); // bottom cutting board
        apps.add(new Toaster(tileWidth * 6, tileHeight * 8, tileWidth, tileHeight)); // toaster (left)
        apps.add(new Toaster(tileWidth * 7, tileHeight * 8, tileWidth, tileHeight)); // toaster (right)
        apps.add(new Trash(tileWidth * 11, tileHeight * 2, tileWidth, tileHeight)); // trash
        apps.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight, this)); // serving windows
                                                                                                     // (2x1)

        return apps;
    }

    public Array<Appliance> getDay2Apps() {
        int tileWidth = 100;
        int tileHeight = 100;

        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(tileWidth * 11, tileHeight * 8, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top
                                                                                                                 // right
                                                                                                                 // counter
        apps.add(new Counter(tileWidth * 3, tileHeight * 5, tileWidth, tileHeight, Appliance.direction.UP)); // mid left
                                                                                                             // counter
        for (int i = 0; i < 5; i++) { // bottom counters
            apps.add(
                    new Counter(tileWidth * (6 + i), tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT));
        }

        apps.add(new Crate(tileWidth * 5, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.bread)); // bread
                                                                                                        // container
        apps.add(new Crate(tileWidth * 6, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.wheatBread)); // wheat
                                                                                                             // bread
                                                                                                             // container
        apps.add(new Crate(tileWidth * 7, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.ham)); // ham container
        apps.add(new Crate(tileWidth * 8, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.cheese)); // cheese
                                                                                                         // container
        apps.add(new Crate(tileWidth * 9, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.lettuce)); // lettuce
                                                                                                          // container
        apps.add(new Crate(tileWidth * 10, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.tomato)); // tomato
                                                                                                          // container

        apps.add(new Toaster(tileWidth * 7, tileHeight * 8, tileWidth, tileHeight)); // left toaster
        apps.add(new Toaster(tileWidth * 8, tileHeight * 8, tileWidth, tileHeight)); // right toaster
        apps.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight, this)); // serving windows
                                                                                                     // (2x1)
        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 7, tileWidth, tileHeight)); // top cutting board
        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 6, tileWidth, tileHeight)); // top cutting board
        apps.add(new FryingPan(tileWidth * 3, tileHeight * 4, tileWidth, tileHeight)); // top frying pan
        apps.add(new FryingPan(tileWidth * 3, tileHeight * 3, tileWidth, tileHeight)); // bottom frying pan
        apps.add(new Trash(tileWidth * 11, tileHeight * 2, tileWidth, tileHeight)); // trash

        return apps;
    }

    public Array<Appliance> getDay3Apps() {
        int tileWidth = 100;
        int tileHeight = 100;

        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(tileWidth * 11, tileHeight * 8, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top
                                                                                                                 // right
                                                                                                                 // counter
        apps.add(new Counter(tileWidth * 6, tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT)); // bottom
                                                                                                                // left
                                                                                                                // counter
        for (int i = 0; i < 2; i++) { // bottom right counters
            apps.add(
                    new Counter(tileWidth * (9 + i), tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT));
        }

        apps.add(new Crate(tileWidth * 4, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.bread)); // bread
                                                                                                        // container
        apps.add(new Crate(tileWidth * 5, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.wheatBread)); // wheat
                                                                                                             // bread
                                                                                                             // container
        apps.add(new Crate(tileWidth * 6, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.sourBread)); // sour
                                                                                                            // bread
                                                                                                            // container
        apps.add(new Crate(tileWidth * 6, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.ham)); // ham container
        apps.add(new Crate(tileWidth * 7, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.cheese)); // cheese
                                                                                                         // container
        apps.add(new Crate(tileWidth * 8, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.lettuce)); // lettuce
                                                                                                          // container
        apps.add(new Crate(tileWidth * 9, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.tomato)); // tomato
                                                                                                         // container

        apps.add(new Toaster(tileWidth * 7, tileHeight * 8, tileWidth, tileHeight)); // left toaster
        apps.add(new Toaster(tileWidth * 8, tileHeight * 8, tileWidth, tileHeight)); // right toaster
        apps.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight, this)); // serving windows
                                                                                                     // (2x1)
        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 6, tileWidth, tileHeight)); // top cutting board
        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 5, tileWidth, tileHeight)); // top cutting board
        apps.add(new FryingPan(tileWidth * 3, tileHeight * 4, tileWidth, tileHeight)); // top frying pan
        apps.add(new FryingPan(tileWidth * 3, tileHeight * 3, tileWidth, tileHeight)); // bottom frying pan
        apps.add(new KetchupBottle(tileWidth * 7, tileHeight * 2, tileWidth, tileHeight)); // ketchup
        apps.add(new MustardBottle(tileWidth * 8, tileHeight * 2, tileWidth, tileHeight)); // mustard
        apps.add(new Trash(tileWidth * 11, tileHeight * 2, tileWidth, tileHeight)); // trash

        return apps;
    }

    public Array<Appliance> getDay4Apps() {
        int tileWidth = 100;
        int tileHeight = 100;

        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(tileWidth * 11, tileHeight * 8, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top
                                                                                                                 // right
                                                                                                                 // counter
        apps.add(new Counter(tileWidth * 6, tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT)); // bottom
                                                                                                                // left
                                                                                                                // counter
        apps.add(new Counter(tileWidth * 10, tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT)); // bottom
                                                                                                                 // right
                                                                                                                 // counter

        apps.add(new Crate(tileWidth * 4, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.bread)); // bread
                                                                                                        // container
        apps.add(new Crate(tileWidth * 5, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.wheatBread)); // wheat
                                                                                                             // bread
                                                                                                             // container
        apps.add(new Crate(tileWidth * 6, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.sourBread)); // sour
                                                                                                            // bread
                                                                                                            // container
        apps.add(new Crate(tileWidth * 5, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.ham)); // ham container
        apps.add(new Crate(tileWidth * 6, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.cheese)); // cheese
                                                                                                         // container
        apps.add(new Crate(tileWidth * 7, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.lettuce)); // lettuce
                                                                                                          // container
        apps.add(new Crate(tileWidth * 8, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.tomato)); // tomato
                                                                                                         // container
        apps.add(new Crate(tileWidth * 9, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.minionBread)); // minion
                                                                                                              // bread
                                                                                                              // container

        apps.add(new Toaster(tileWidth * 8, tileHeight * 8, tileWidth, tileHeight)); // right toaster
        apps.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight, this)); // serving windows
                                                                                                     // (2x1)
        apps.add(new ChoppingBoard(tileWidth * 2, tileHeight * 6, tileWidth, tileHeight)); // top cutting board
        apps.add(new ChoppingBoard(tileWidth * 2, tileHeight * 5, tileWidth, tileHeight)); // top cutting board
        apps.add(new FryingPan(tileWidth * 2, tileHeight * 4, tileWidth, tileHeight)); // top frying pan
        apps.add(new KetchupBottle(tileWidth * 7, tileHeight * 2, tileWidth, tileHeight)); // ketchup
        apps.add(new MustardBottle(tileWidth * 8, tileHeight * 2, tileWidth, tileHeight)); // mustard
        apps.add(new Trash(tileWidth * 11, tileHeight * 2, tileWidth, tileHeight)); // trash

        return apps;
    }

    public Array<Appliance> getDay5Apps() {
        int tileWidth = 100;
        int tileHeight = 100;

        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(tileWidth * 10, tileHeight * 7, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top
                                                                                                                 // right
                                                                                                                 // counter
        apps.add(new Counter(tileWidth * 10, tileHeight, tileWidth, tileHeight, Appliance.direction.RIGHT)); // bottom
                                                                                                             // right
                                                                                                             // counter

        apps.add(new Crate(tileWidth * 3, tileHeight * 7, tileWidth, tileHeight, Holdable.Type.bread)); // bread
                                                                                                        // container
        apps.add(new Crate(tileWidth * 4, tileHeight * 7, tileWidth, tileHeight, Holdable.Type.wheatBread)); // wheat
                                                                                                             // bread
                                                                                                             // container
        apps.add(new Crate(tileWidth * 5, tileHeight * 7, tileWidth, tileHeight, Holdable.Type.sourBread)); // sour
                                                                                                            // bread
                                                                                                            // container
        apps.add(new Crate(tileWidth * 4, tileHeight * 4, tileWidth, tileHeight, Holdable.Type.ham)); // ham container
        apps.add(new Crate(tileWidth * 5, tileHeight * 4, tileWidth, tileHeight, Holdable.Type.cheese)); // cheese
                                                                                                         // container
        apps.add(new Crate(tileWidth * 6, tileHeight * 4, tileWidth, tileHeight, Holdable.Type.lettuce)); // lettuce
                                                                                                          // container
        apps.add(new Crate(tileWidth * 7, tileHeight * 4, tileWidth, tileHeight, Holdable.Type.tomato)); // tomato
                                                                                                         // container
        apps.add(new Crate(tileWidth * 8, tileHeight * 4, tileWidth, tileHeight, Holdable.Type.minionBread)); // minion
                                                                                                              // bread
                                                                                                              // container

        apps.add(new Toaster(tileWidth * 7, tileHeight * 7, tileWidth, tileHeight)); // right toaster
        apps.add(new ServingWindow(tileWidth * 8, tileHeight * 7, tileWidth * 2, tileHeight, this)); // serving windows
                                                                                                     // (2x1)
        apps.add(new ChoppingBoard(tileWidth, tileHeight * 4, tileWidth, tileHeight)); // bottom cutting board
        apps.add(new FryingPan(tileWidth, tileHeight * 3, tileWidth, tileHeight)); // top frying pan
        apps.add(new KetchupBottle(tileWidth * 7, tileHeight, tileWidth, tileHeight)); // ketchup
        apps.add(new MustardBottle(tileWidth * 8, tileHeight, tileWidth, tileHeight)); // mustard
        apps.add(new Trash(tileWidth * 10, tileHeight, tileWidth, tileHeight)); // trash

        return apps;

        // sorry for creating like 100 objects at the start that don't do anything for
        // half the game
    }

    // quick and dirty way to get the apps for the day
    public Array<Appliance> getApps(int level) {
        switch (level) {
            case 1:
                return getDay1Apps();
            case 2:
                return getDay2Apps();
            case 3:
                return getDay3Apps();
            case 4:
                return getDay4Apps();
            case 5:
                return getDay5Apps();
            default:
                throw new IllegalArgumentException("Invalid level number");
        }
    }
}
