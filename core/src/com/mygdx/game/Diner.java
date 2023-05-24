package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import java.util.Optional;

public class Diner extends Game {
    static final int LENGTH = 768;
    static final int WIDTH = 576;
    DayState firstDay;

    public SpriteBatch batch;
    public BitmapFont font;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("Fonts/sh-pinscher.fnt"));
        // for Maps/day1Map.tmx to day5Map.tmx
        Optional<DayState> dayState = Optional.empty();
        for (int i = 5; i >= 1; i--) {
            String mapFile = String.format("Maps/day%dMap.tmx", i);
            String name = String.format("Day %d", i);
            int wantedOrders = 10; // to be changed
            dayState = Optional.of(new DayState(mapFile, name, wantedOrders, dayState));
        }
        this.firstDay = dayState.get();
        this.setScreen(new MainMenu(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
