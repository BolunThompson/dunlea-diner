package com.mygdx.game;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.appliance.*;
import com.mygdx.game.holdable.Holdable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class DayState {
    final String mapFile;
    final String name;
    final int level;

    final int wantedOrders;
    static Array<Order> orders; // idk what im doing but text editor said to make this static so i did

    Array<Appliance> apps;

    static final float maxTime = 120;
    float currentTime;

    final Optional<DayState> nextDay;

    private DayState(
            String mapFile,
            String name,
            Optional<DayState> nextDay,
            int wantedOrders,
            Holdable.Type[] wantedIngredients,
            int level,
            Array<Appliance> apps) {
        this.mapFile = mapFile;
        this.name = name;
        this.nextDay = nextDay;
        this.wantedOrders = wantedOrders;
        this.level = level;
        this.apps = apps;
        orders = new Array<Order>();
        for (int i = 0; i < wantedOrders; i++) {
            List<Holdable.Type> ingredients = Arrays.asList(wantedIngredients);
            Collections.shuffle(ingredients);
            ingredients = new ArrayList<Holdable.Type>(ingredients.subList(0, 2));
            ingredients.add(Holdable.Type.bread);
            orders.add(new Order(ingredients));
        }
    }

    static DayState createLevels() {
        Optional<DayState> dayState = Optional.empty();
        for (int level = 5; level >= 1; level--) {
            String mapFile = String.format("Maps/day%dMap.tmx", level);
            String name = String.format("Day %d", level);
            Holdable.Type[] ingredients;
            int wantedOrders;
            Array<Appliance> todayApps;
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
                    todayApps = getDay1Apps();
                    break;
                // repeat for cases 2 through 5
                case 2:
                    ingredients = new Holdable.Type[] {
                            Holdable.Type.ham,
                            Holdable.Type.cheese,
                            Holdable.Type.lettuce,
                            Holdable.Type.tomato };
                    wantedOrders = 10;
                    todayApps = getDay2Apps();
                    break;
                case 3:
                    ingredients = new Holdable.Type[] {
                            Holdable.Type.ham,
                            Holdable.Type.cheese,
                            Holdable.Type.lettuce,
                            Holdable.Type.tomato };
                    wantedOrders = 10;
                    todayApps = getDay3Apps();
                    break;
                // repeat for cases 4 and 5
                case 4:
                    ingredients = new Holdable.Type[] {
                            Holdable.Type.ham,
                            Holdable.Type.cheese,
                            Holdable.Type.lettuce,
                            Holdable.Type.tomato };
                    wantedOrders = 10;
                    todayApps = getDay4Apps();
                    break;
                case 5:
                    ingredients = new Holdable.Type[] {
                            Holdable.Type.ham,
                            Holdable.Type.cheese,
                            Holdable.Type.lettuce,
                            Holdable.Type.tomato };
                    wantedOrders = 10;
                    todayApps = getDay5Apps();
                    break;
                default:
                    throw new RuntimeException("Invalid level");
            }
            dayState = Optional.of(new DayState(mapFile, name, dayState, wantedOrders, ingredients, level, todayApps));
        }
        return dayState.get();
    }

    void reset() {
        this.currentTime = 0;
    }

    boolean isOver() {
        return orders.isEmpty() || currentTime >= maxTime;
    }

    boolean isWon() {
        return orders.isEmpty();
    }

    void mark(Holdable.Type ing) {
        if (orders.isEmpty()) {
            return;
        }
        orders.peek().mark(ing);
        if (orders.peek().complete()) {
            orders.pop();
        }
    }

    Array<Holdable.Type> neededIngredients() {
        if (orders.isEmpty()) {
            return new Array<Holdable.Type>();
        }
        return orders.peek().neededIngredients();
    }

    int ordersCnt() {
        return orders.size;
    }

    int score() {
        return 1 + (int) ((100 + (maxTime - currentTime) / maxTime * 1000) * (1 + (double) (level - 1) / 2));
    }




    // putting this mess down here
    public static Array<Appliance> getDay1Apps() {
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
        apps.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight)); // serving windows (2x1)

        return apps;
    }

    public static Array<Appliance> getDay2Apps() {
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
        apps.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight)); // serving windows (2x1)
        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 7, tileWidth, tileHeight)); // top cutting board
        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 6, tileWidth, tileHeight)); // top cutting board
        apps.add(new FryingPan(tileWidth * 3, tileHeight * 4, tileWidth, tileHeight)); // top frying pan
        apps.add(new FryingPan(tileWidth * 3, tileHeight * 3, tileWidth, tileHeight)); // bottom frying pan
        apps.add(new Trash(tileWidth * 11, tileHeight * 2, tileWidth, tileHeight)); // trash

        return apps;
    }

    public static Array<Appliance> getDay3Apps() {
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
        apps.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight)); // serving windows (2x1)
        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 6, tileWidth, tileHeight)); // top cutting board
        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 5, tileWidth, tileHeight)); // top cutting board
        apps.add(new FryingPan(tileWidth * 3, tileHeight * 4, tileWidth, tileHeight)); // top frying pan
        apps.add(new FryingPan(tileWidth * 3, tileHeight * 3, tileWidth, tileHeight)); // bottom frying pan
        apps.add(new KetchupBottle(tileWidth * 7, tileHeight * 2, tileWidth, tileHeight)); // ketchup
        apps.add(new MustardBottle(tileWidth * 8, tileHeight * 2, tileWidth, tileHeight)); // mustard
        apps.add(new Trash(tileWidth * 11, tileHeight * 2, tileWidth, tileHeight)); // trash

        return apps;
    }

    public static Array<Appliance> getDay4Apps() {
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
        apps.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight)); // serving windows (2x1)
        apps.add(new ChoppingBoard(tileWidth * 2, tileHeight * 6, tileWidth, tileHeight)); // top cutting board
        apps.add(new ChoppingBoard(tileWidth * 2, tileHeight * 5, tileWidth, tileHeight)); // top cutting board
        apps.add(new FryingPan(tileWidth * 2, tileHeight * 4, tileWidth, tileHeight)); // top frying pan
        apps.add(new KetchupBottle(tileWidth * 7, tileHeight * 2, tileWidth, tileHeight)); // ketchup
        apps.add(new MustardBottle(tileWidth * 8, tileHeight * 2, tileWidth, tileHeight)); // mustard
        apps.add(new Trash(tileWidth * 11, tileHeight * 2, tileWidth, tileHeight)); // trash

        return apps;
    }

    public static Array<Appliance> getDay5Apps() {
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
        apps.add(new ServingWindow(tileWidth * 8, tileHeight * 7, tileWidth * 2, tileHeight)); // serving windows (2x1)
        apps.add(new ChoppingBoard(tileWidth, tileHeight * 4, tileWidth, tileHeight)); // bottom cutting board
        apps.add(new FryingPan(tileWidth, tileHeight * 3, tileWidth, tileHeight)); // top frying pan
        apps.add(new KetchupBottle(tileWidth * 7, tileHeight, tileWidth, tileHeight)); // ketchup
        apps.add(new MustardBottle(tileWidth * 8, tileHeight, tileWidth, tileHeight)); // mustard
        apps.add(new Trash(tileWidth * 10, tileHeight, tileWidth, tileHeight)); // trash

        return apps;

        // sorry for creating like 100 objects I know it's probably like super inefficient
    }
}
