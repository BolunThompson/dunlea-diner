package com.mygdx.game;

import com.badlogic.gdx.utils.Array;
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

    static final float maxTime = 120;
    float currentTime;

    final Optional<DayState> nextDay;

    private DayState(
            String mapFile,
            String name,
            Optional<DayState> nextDay,
            int wantedOrders,
            Holdable.Type[] wantedIngredients,
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
                    break;
                // repeat for cases 2 through 5
                case 2:
                    ingredients = new Holdable.Type[] {
                            Holdable.Type.ham,
                            Holdable.Type.cheese,
                            Holdable.Type.lettuce,
                            Holdable.Type.tomato };
                    wantedOrders = 10;
                    break;
                case 3:
                    ingredients = new Holdable.Type[] {
                            Holdable.Type.ham,
                            Holdable.Type.cheese,
                            Holdable.Type.lettuce,
                            Holdable.Type.tomato };
                    wantedOrders = 10;
                    break;
                // repeat for cases 4 and 5
                case 4:
                    ingredients = new Holdable.Type[] {
                            Holdable.Type.ham,
                            Holdable.Type.cheese,
                            Holdable.Type.lettuce,
                            Holdable.Type.tomato };
                    wantedOrders = 10;
                    break;
                case 5:
                    ingredients = new Holdable.Type[] {
                            Holdable.Type.ham,
                            Holdable.Type.cheese,
                            Holdable.Type.lettuce,
                            Holdable.Type.tomato };
                    wantedOrders = 10;
                    break;
                default:
                    throw new RuntimeException("Invalid level");
            }
            dayState = Optional.of(new DayState(mapFile, name, dayState, wantedOrders, ingredients, level));
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
}
