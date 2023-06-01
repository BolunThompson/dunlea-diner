package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import java.util.Optional;

public class Diner extends Game {
    static final int LENGTH = 1200;
    static final int WIDTH = 900;
    DayState firstDay;

    public SpriteBatch batch;
    public BitmapFont font;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("Fonts/sh-pinscher.fnt"));
        firstDay = DayState.createLevels();
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
