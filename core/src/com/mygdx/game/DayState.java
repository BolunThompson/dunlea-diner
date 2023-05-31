package com.mygdx.game;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.holdable.Ingredient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class DayState {
    final String mapFile;
    final String name;

    private Array<Order> orders;

    static final float maxTime = 180;
    float currentTime;

    final Optional<DayState> nextDay;

    private DayState(
            String mapFile,
            String name,
            Optional<DayState> nextDay,
            int wantedOrders,
            Ingredient.Type[] wantedIngredients) {
        this.mapFile = mapFile;
        this.name = name;
        this.nextDay = nextDay;
        orders = new Array<Order>();
        for (int i = 0; i < wantedOrders; i++) {
            List<Ingredient.Type> ingredients = Arrays.asList(wantedIngredients);
            Collections.shuffle(ingredients);
            ingredients = ingredients.subList(0, 3);
            orders.add(new Order(ingredients));
        }
    }

    static DayState createLevels() {
        Optional<DayState> dayState = Optional.empty();
        for (int level = 5; level >= 1; level--) {
            String mapFile = String.format("Maps/day%dMap.tmx", level);
            String name = String.format("Day %d", level);
            Ingredient.Type[] ingredients;
            int wantedOrders;
            // right now the only ingredients are ham, cheese, lettuce, and tomato
            // bread is not included since bread is always available
            // for all days
            switch (level) {
                case 1:
                    ingredients = new Ingredient.Type[] {
                            Ingredient.Type.ham,
                            Ingredient.Type.cheese,
                            Ingredient.Type.lettuce,
                            Ingredient.Type.tomato };
                    wantedOrders = 10;
                    break;
                // repeat for cases 2 through 5
                case 2:
                    ingredients = new Ingredient.Type[] {
                            Ingredient.Type.ham,
                            Ingredient.Type.cheese,
                            Ingredient.Type.lettuce,
                            Ingredient.Type.tomato };
                    wantedOrders = 10;
                    break;
                case 3:
                    ingredients = new Ingredient.Type[] {
                            Ingredient.Type.ham,
                            Ingredient.Type.cheese,
                            Ingredient.Type.lettuce,
                            Ingredient.Type.tomato };
                    wantedOrders = 10;
                    break;
                // repeat for cases 4 and 5
                case 4:
                    ingredients = new Ingredient.Type[] {
                            Ingredient.Type.ham,
                            Ingredient.Type.cheese,
                            Ingredient.Type.lettuce,
                            Ingredient.Type.tomato };
                    wantedOrders = 10;
                    break;
                case 5:
                    ingredients = new Ingredient.Type[] {
                            Ingredient.Type.ham,
                            Ingredient.Type.cheese,
                            Ingredient.Type.lettuce,
                            Ingredient.Type.tomato };
                    wantedOrders = 10;
                    break;
                default:
                    throw new RuntimeException("Invalid level");
            }
            dayState = Optional.of(new DayState(mapFile, name, dayState, wantedOrders, ingredients));
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

    void mark(Ingredient.Type ing) {
        if (orders.isEmpty()) {
            return;
        }
        orders.peek().mark(ing);
        if (orders.peek().complete()) {
            orders.pop();
        }
    }
    Array<Ingredient.Type> neededIngredients() {
        if (orders.isEmpty()) {
            return new Array<Ingredient.Type>();
        }
        return orders.peek().neededIngredients();
    }
}
