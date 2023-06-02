package com.mygdx.game;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.appliance.*;
import com.mygdx.game.holdable.Holdable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DayState {
    final String mapFile;
    final String name;
    final int level;

    final int wantedOrders;
    Array<Order> orders;
    int orderIndex; // current, I think

    private static Array<Appliance> day1apps, day2apps, day3apps, day4apps, day5apps;
    static Array<Appliance> todayApps;

    static final float maxTime = 120;
    float currentTime;

    final Optional<DayState> nextDay;

    private DayState(
            String mapFile,
            String name,
            Optional<DayState> nextDay,
            int wantedOrders,
            Holdable.Type[] wantedIngredients,
            Array<Appliance> apps,
            int level) {
        this.mapFile = mapFile;
        this.name = name;
        this.nextDay = nextDay;
        this.wantedOrders = wantedOrders;
        this.level = level;

        orders = new Array<Order>();
        for (int i = 0; i < wantedOrders; i++) {
            List<Holdable.Type> ingredients = Arrays.asList(wantedIngredients);
            Collections.shuffle(ingredients);
            ingredients = new ArrayList<Holdable.Type>(ingredients.subList(0, 2));
            ingredients.add(Holdable.Type.bread);
            orders.add(new Order(ingredients));
        }

        day1apps = getDay1Apps();
        day2apps = getDay2Apps();
        day3apps = getDay3Apps();
        day4apps = getDay4Apps();
        day5apps = getDay5Apps();
    }

    static DayState createLevels() {
        Optional<DayState> dayState = Optional.empty();
        for (int level = 5; level >= 1; level--) {
            String mapFile = String.format("Maps/day%dMap.tmx", level);
            String name = String.format("Day %d", level);
            Holdable.Type[] ingredients;
            int wantedOrders;
            // right now the only ingredients are ham, cheese, lettuce, and tomato
            // bread is not included since bread is always available
            // for all days
            switch (level) {
                case 1:
                    ingredients = new Holdable.Type[] {
                            Holdable.Type.ham,
                            Holdable.Type.cheese,
                            Holdable.Type.lettuce,
                            Holdable.Type.tomato };
                    wantedOrders = 10;
                    todayApps = day1apps;
                    break;
                // repeat for cases 2 through 5
                case 2:
                    ingredients = new Holdable.Type[] {
                            Holdable.Type.ham,
                            Holdable.Type.cheese,
                            Holdable.Type.lettuce,
                            Holdable.Type.tomato };
                    wantedOrders = 10;
                    todayApps = day2apps;
                    break;
                case 3:
                    ingredients = new Holdable.Type[] {
                            Holdable.Type.ham,
                            Holdable.Type.cheese,
                            Holdable.Type.lettuce,
                            Holdable.Type.tomato };
                    wantedOrders = 10;
                    todayApps = day3apps;
                    break;
                // repeat for cases 4 and 5
                case 4:
                    ingredients = new Holdable.Type[] {
                            Holdable.Type.ham,
                            Holdable.Type.cheese,
                            Holdable.Type.lettuce,
                            Holdable.Type.tomato };
                    wantedOrders = 10;
                    todayApps = day4apps;
                    break;
                case 5:
                    ingredients = new Holdable.Type[] {
                            Holdable.Type.ham,
                            Holdable.Type.cheese,
                            Holdable.Type.lettuce,
                            Holdable.Type.tomato };
                    wantedOrders = 10;
                    todayApps = day5apps;
                    break;
                default:
                    throw new RuntimeException("Invalid level");
            }
            dayState = Optional.of(new DayState(mapFile, name, dayState, wantedOrders, ingredients, todayApps, level));
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

    public void mark(Holdable.Type ing) {
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

    public Array<Holdable.Type> neededIngredients() {
        if (orders.isEmpty()) {
            return new Array<Holdable.Type>();
        }
        return orders.get(orderIndex).neededIngredients();
    }

    public int ordersCnt() {
        return orders.size;
    }

    public int score() {
        return 1 + (int) ((100 + (maxTime - currentTime) / maxTime * 1000) * (1 + (double) (level - 1) / 2));
    }

    // putting this mess down here
    public Array<Appliance> getDay1Apps() {
        int tileWidth = 100;
        int tileHeight = 100;

        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(tileWidth * 3, tileHeight * 6, tileWidth, tileHeight, Appliance.direction.UP)); // left top counter
        apps.add(new Counter(tileWidth * 3, tileHeight * 3, tileWidth, tileHeight, Appliance.direction.UP)); // left bottom counter
        for (int i = 0; i < 5; i++) {                                                                                                    // bottom counters
            apps.add(new Counter(tileWidth * (6 + i), tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT));
        }
        apps.add(new Counter(tileWidth * 8, tileHeight * 8, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top left counter
        apps.add(new Counter(tileWidth * 11, tileHeight * 8, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top right counter

        apps.add(new Crate(tileWidth * 6, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.bread)); // bread container
        apps.add(new Crate(tileWidth * 7, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.ham)); // ham container
        apps.add(new Crate(tileWidth * 8, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.cheese)); // cheese container
        apps.add(new Crate(tileWidth * 9, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.lettuce)); // lettuce container
        apps.add(new Crate(tileWidth * 10, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.tomato)); // tomato container

        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 5, tileWidth, tileHeight)); // top cutting board
        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 4, tileWidth, tileHeight)); // bottom cutting board
        apps.add(new Toaster(tileWidth * 6, tileHeight * 8, tileWidth, tileHeight)); // toaster (left)
        apps.add(new Toaster(tileWidth * 7, tileHeight * 8, tileWidth, tileHeight)); // toaster (right)
        apps.add(new Trash(tileWidth * 11, tileHeight * 2, tileWidth, tileHeight)); // trash
        apps.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight, this)); // serving windows (2x1)

        return apps;
    }

    public Array<Appliance> getDay2Apps() {
        int tileWidth = 100;
        int tileHeight = 100;

        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(tileWidth * 11, tileHeight * 8, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top right counter
        apps.add(new Counter(tileWidth * 3, tileHeight * 5, tileWidth, tileHeight, Appliance.direction.UP)); // mid left counter
        for (int i = 0; i < 5; i++) {                                                        // bottom counters
            apps.add(new Counter(tileWidth * (6 + i), tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT));
        }

        apps.add(new Crate(tileWidth * 5, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.bread)); // bread container
        apps.add(new Crate(tileWidth * 6, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.wheatBread)); // wheat bread container
        apps.add(new Crate(tileWidth * 7, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.ham)); // ham container
        apps.add(new Crate(tileWidth * 8, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.cheese)); // cheese container
        apps.add(new Crate(tileWidth * 9, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.lettuce)); // lettuce container
        apps.add(new Crate(tileWidth * 10, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.tomato)); // tomato container

        apps.add(new Toaster(tileWidth * 7, tileHeight * 8, tileWidth, tileHeight)); // left toaster
        apps.add(new Toaster(tileWidth * 8, tileHeight * 8, tileWidth, tileHeight)); // right toaster
        apps.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight, this)); // serving windows (2x1)
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

        apps.add(new Counter(tileWidth * 11, tileHeight * 8, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top right counter
        apps.add(new Counter(tileWidth * 6, tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT)); // bottom left counter
        for (int i = 0; i < 2; i++) {                                                        // bottom right counters
            apps.add(new Counter(tileWidth * (9 + i), tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT));
        }

        apps.add(new Crate(tileWidth * 4, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.bread)); // bread container
        apps.add(new Crate(tileWidth * 5, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.wheatBread)); // wheat bread container
        apps.add(new Crate(tileWidth * 6, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.sourBread)); // sour bread container
        apps.add(new Crate(tileWidth * 6, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.ham)); // ham container
        apps.add(new Crate(tileWidth * 7, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.cheese)); // cheese container
        apps.add(new Crate(tileWidth * 8, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.lettuce)); // lettuce container
        apps.add(new Crate(tileWidth * 9, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.tomato)); // tomato container

        apps.add(new Toaster(tileWidth * 7, tileHeight * 8, tileWidth, tileHeight)); // left toaster
        apps.add(new Toaster(tileWidth * 8, tileHeight * 8, tileWidth, tileHeight)); // right toaster
        apps.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight, this)); // serving windows (2x1)
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

        apps.add(new Counter(tileWidth * 11, tileHeight * 8, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top right counter
        apps.add(new Counter(tileWidth * 6, tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT)); // bottom left counter
        apps.add(new Counter(tileWidth * 10, tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT)); // bottom right counter

        apps.add(new Crate(tileWidth * 4, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.bread)); // bread container
        apps.add(new Crate(tileWidth * 5, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.wheatBread)); // wheat bread container
        apps.add(new Crate(tileWidth * 6, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.sourBread)); // sour bread container
        apps.add(new Crate(tileWidth * 5, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.ham)); // ham container
        apps.add(new Crate(tileWidth * 6, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.cheese)); // cheese container
        apps.add(new Crate(tileWidth * 7, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.lettuce)); // lettuce container
        apps.add(new Crate(tileWidth * 8, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.tomato)); // tomato container
        apps.add(new Crate(tileWidth * 9, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.minionBread)); // minion bread container

        apps.add(new Toaster(tileWidth * 8, tileHeight * 8, tileWidth, tileHeight)); // right toaster
        apps.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight, this)); // serving windows (2x1)
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

        apps.add(new Counter(tileWidth * 10, tileHeight * 7, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top right counter
        apps.add(new Counter(tileWidth * 10, tileHeight, tileWidth, tileHeight, Appliance.direction.RIGHT)); // bottom right counter

        apps.add(new Crate(tileWidth * 3, tileHeight * 7, tileWidth, tileHeight, Holdable.Type.bread)); // bread container
        apps.add(new Crate(tileWidth * 4, tileHeight * 7, tileWidth, tileHeight, Holdable.Type.wheatBread)); // wheat bread container
        apps.add(new Crate(tileWidth * 5, tileHeight * 7, tileWidth, tileHeight, Holdable.Type.sourBread)); // sour bread container
        apps.add(new Crate(tileWidth * 4, tileHeight * 4, tileWidth, tileHeight, Holdable.Type.ham)); // ham container
        apps.add(new Crate(tileWidth * 5, tileHeight * 4, tileWidth, tileHeight, Holdable.Type.cheese)); // cheese container
        apps.add(new Crate(tileWidth * 6, tileHeight * 4, tileWidth, tileHeight, Holdable.Type.lettuce)); // lettuce container
        apps.add(new Crate(tileWidth * 7, tileHeight * 4, tileWidth, tileHeight, Holdable.Type.tomato)); // tomato container
        apps.add(new Crate(tileWidth * 8, tileHeight * 4, tileWidth, tileHeight, Holdable.Type.minionBread)); // minion bread container

        apps.add(new Toaster(tileWidth * 7, tileHeight * 7, tileWidth, tileHeight)); // right toaster
        apps.add(new ServingWindow(tileWidth * 8, tileHeight * 7, tileWidth * 2, tileHeight, this)); // serving windows (2x1)
        apps.add(new ChoppingBoard(tileWidth, tileHeight * 4, tileWidth, tileHeight)); // bottom cutting board
        apps.add(new FryingPan(tileWidth, tileHeight * 3, tileWidth, tileHeight)); // top frying pan
        apps.add(new KetchupBottle(tileWidth * 7, tileHeight, tileWidth, tileHeight)); // ketchup
        apps.add(new MustardBottle(tileWidth * 8, tileHeight, tileWidth, tileHeight)); // mustard
        apps.add(new Trash(tileWidth * 10, tileHeight, tileWidth, tileHeight)); // trash

        return apps;

        // sorry for creating like 100 objects at the start that don't do anything for half the game
    }

}
