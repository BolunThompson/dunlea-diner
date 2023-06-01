package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

class FinalScreen implements Screen {
    private final Diner game;
    private final OrthographicCamera camera;

    FinalScreen(final Diner game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Diner.LENGTH, Diner.WIDTH);
    }

    @Override
    public void render(float delta) {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        ScreenUtils.clear(1, 1, 1, 1);
        game.batch.begin();
        game.font.getData().setScale(1.2f);
        game.font.draw(game.batch, getMsg(), Diner.LENGTH * 0.1f, Diner.WIDTH * 0.9f);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            dispose();
            game.restart();
        }
    }

    private String getMsg() {
        return "Your high score is " + game.highScore + "!\n\nClick to play again!";
    
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
