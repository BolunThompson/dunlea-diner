package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

class MainMenu implements Screen {
    private final OrthographicCamera camera;
    private final Diner game;

    private Texture title;
    private TextureRegion[] menuFrames;
    private int menuIndex;

    MainMenu(final Diner game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 100*12, 100*9); // this camera MUST have the same dimensions as camera in DayScreen.java

        // create texture for "misc/title.png"
        title = new Texture(Gdx.files.internal("Misc/menu_sheet.png")); // original screen size: 800 x 480
        TextureRegion[][] temp = TextureRegion.split(title, 1200, 900);
        menuFrames = new TextureRegion[4];
        for(int i = 0; i < 4; i++) {
            menuFrames[i] = temp[0][i];
        }
        menuIndex = 0;
    }

    @Override
    public void render(float delta) {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        ScreenUtils.clear(1, 1, 1, 1);
        game.batch.begin();

        // sandwich
        game.batch.draw(menuFrames[menuIndex], 0, 0, 1200, 900);

        // text
        if (menuIndex == 0) {
            game.font.setColor(0, 0, 0, 1);
            game.font.getData().setScale(1.5f);
            game.font.draw(game.batch, "Welcome to Dunlea's Diner!", 460, 800);
            game.font.draw(game.batch, "Click anywhere to begin", 520, 700);
            game.font.draw(game.batch, "Press space for tutorial", 520, 600);
        }

        game.batch.end();

        if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            if(menuIndex < 4)
                menuIndex++;
            if(menuIndex == 4)
                menuIndex = 0;
        }

        if (Gdx.input.justTouched()) {
            if(menuIndex == 0) {
                game.setScreen(new DayScreen(game, game.firstDay));
                dispose();
            }
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
