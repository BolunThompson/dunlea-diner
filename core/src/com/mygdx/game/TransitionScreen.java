package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

class TransitionScreen implements Screen {
    private final Diner game;
    private final OrthographicCamera camera;

    private final DayState dayState;
    private final boolean highScore;
    private final int oldHighScore;

    TransitionScreen(final Diner game, DayState dayState) {
        this.game = game;
        this.dayState = dayState;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Diner.LENGTH, Diner.WIDTH);

        highScore = dayState.score() > game.highScore;
        oldHighScore = game.highScore;
        if (highScore) {
            game.highScore = dayState.score();
        }
    }

    @Override
    public void render(float delta) {
        if (dayState.nextDay.isEmpty()) {
            game.setScreen(new FinalScreen(game));
            return;
        }

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        ScreenUtils.clear(1, 1, 1, 1);
        game.batch.begin();
        game.font.getData().setScale(1.2f);
        game.font.setColor(0, 0, 0, 1.0f);
        game.font.draw(game.batch, getMsg(), Diner.LENGTH * 0.1f, Diner.WIDTH * 0.9f);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            dispose();
            dayState.reset();
            Screen nextScreen = dayState.nextDay
                    .<Screen>map(v -> new DayScreen(game, v))
                    .orElseGet(() -> new FinalScreen(game));
            game.setScreen(nextScreen);
        }
    }

    private String getMsg() {
        String msg = "";
        if (dayState.ordersCnt() >= dayState.wantedOrders) {
            String text = "You have completed %d orders out of %d!\nYou have completed the day!\n\nYour score is %d";
            msg = String.format(text, dayState.orderIndex, dayState.wantedOrders, dayState.score());
            if (highScore) {
                text += ", a new high score!\nYour past high score was " + oldHighScore + ".";
            } else {
                text += ".\nYour high score is " + game.highScore + ".";
            }
            msg = String.format(text, dayState.ordersCnt(), dayState.wantedOrders, dayState.score());
            msg += "\n\nClick anywhere to continue to the next level.";
        } else {
            String text = "You have completed %d orders out of %d.\nYou have failed.\n\nYour score is %d";
            if (highScore) {
                text += ", a new high score!\nYour past high score was " + oldHighScore + ".";
            } else {
                text += ".\nYour high score is " + game.highScore + ".";
            }
            msg = String.format(text, dayState.ordersCnt(), dayState.wantedOrders, dayState.score());
            msg += "\n\nClick anywhere to try again";
        }
        return msg;
    }

    public void show() {
    }

    public void hide() {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void resize(int width, int height) {
    }

    public void dispose() {
        // should something be disposed here?
    }

}
