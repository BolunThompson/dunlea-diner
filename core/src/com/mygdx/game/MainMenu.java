package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

class MainMenu implements Screen {
    private final OrthographicCamera camera;
    private final Diner game;

    private Texture title;

    MainMenu(final Diner game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 100*12, 100*9); // this camera MUST have the same dimensions as camera in DayScreen.java
    }

    @Override
    public void render(float delta) {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        // create texture for "misc/title.png"
        title = new Texture(Gdx.files.internal("Misc/Menu.png")); // original screen size: 800 x 480

        ScreenUtils.clear(1, 1, 1, 1);
        game.batch.begin();

        // sandwich
        game.batch.draw(title, 0, 0, title.getWidth() * 3f, title.getHeight() * 3f);

        // text
        game.font.setColor(0, 0, 0, 1);
        game.font.getData().setScale(1.5f);
        game.font.draw(game.batch, "Welcome to Dunlea's Diner!", 460, 800);
        game.font.draw(game.batch, "Tap anywhere to begin", 520, 700);

        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new DayScreen(game, game.firstDay));
            dispose();
        }
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
        title.dispose();
    }

}
