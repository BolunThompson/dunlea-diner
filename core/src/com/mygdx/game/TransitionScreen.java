package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

class TransitionScreen implements Screen {
    private final Diner game;
    private final OrthographicCamera camera;

    private final DayState dayState;

    TransitionScreen(final Diner game, DayState dayState) {
        this.game = game;
        this.dayState = dayState;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Diner.LENGTH, Diner.WIDTH);
    }

    @Override
    public void render(float delta) {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        ScreenUtils.clear(1, 1, 1, 1);
        game.batch.begin();
        game.font.draw(game.batch, getMsg(), Diner.LENGTH * 0.2f, Diner.WIDTH * 0.5f);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            dispose();
            Screen nextScreen = dayState.nextDay
                    .<Screen>map(v -> new DayScreen(game, v))
                    .orElseGet(() -> new FinalScreen());
            game.setScreen(nextScreen);
        }
    }

    private String getMsg() {
        String msg = "";
        if (dayState.orders >= dayState.wantedOrders) {
            String text = "You have completed %d orders out of %d!\nYou have completed the day!";
            msg = String.format(text, dayState.orders, dayState.wantedOrders);
            msg += "\n\nClick anywhere to continue to the next level";
        } else {
            String text = "You have completed %d orders out of %d. You have failed the day.";
            msg = String.format(text, dayState.orders, dayState.wantedOrders);
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