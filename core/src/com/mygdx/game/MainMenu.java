package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

class MainMenu implements Screen {
    private final OrthographicCamera camera;
    private final Diner game;

    private Texture title;
    private TextureRegion[] menuFrames;
    private int menuIndex;

    ShapeRenderer shapeRender;
    Rectangle startButton, tutorialButton, creditsButton;
    final float buttonY = -100;

    Music music;

    MainMenu(final Diner game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 100*12, 100*9); // this camera MUST have the same dimensions as camera in DayScreen.java

        // create texture for "misc/title.png"
        title = new Texture(Gdx.files.internal("Misc/menu_sheet.png")); // original screen size: 800 x 480
        TextureRegion[][] temp = TextureRegion.split(title, 1200, 900);
        menuFrames = new TextureRegion[5];
        for(int i = 0; i < 5; i++) {
            menuFrames[i] = temp[0][i];
        }
        menuIndex = 0;

        /**
         * The following music was used for this media project:
         * Music: Airport Lounge by Kevin MacLeod
         * Free download: https://filmmusic.io/song/3347-airport-lounge
         * License (CC BY 4.0): https://filmmusic.io/standard-license
         */
        music = Gdx.audio.newMusic(Gdx.files.internal("Sounds/airport-lounge-by-kevin-macleod-from-filmmusic-io.mp3"));
        music.setVolume(0.8f);
        music.setLooping(true);
        music.play();

        shapeRender = new ShapeRenderer();
        startButton = new Rectangle(750, 560 + buttonY, 300, 100);
        tutorialButton = new Rectangle(750, 430 + buttonY, 300, 100);
        creditsButton = new Rectangle(750, 300 + buttonY, 300, 100);
    }

    @Override
    public void render(float delta) {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        ScreenUtils.clear(1, 1, 1, 1);

        game.batch.begin();

        // sandwich
        game.batch.draw(menuFrames[menuIndex], 0, 0, 1200, 900);

        // start screen text
        if (menuIndex == 0) {
            game.font.setColor(0, 0, 0, 1);
            game.font.getData().setScale(1.5f);
            game.font.draw(game.batch, "Welcome to Dunlea's Deli!", 460, 800);
            game.font.draw(game.batch, "Click START to begin", 520, 700);
        }

        // credits text
        if(menuIndex == 4) {
            int x = 190;
            int y = 700;

            game.font.setColor(0, 0, 0, 1);
            game.font.getData().setScale(1.0f);
            game.font.draw(game.batch, "Music - 'Local Forecast - Slower' by Kevin MacLeod", x, y);
            game.font.draw(game.batch, "        'Airport Lounge' by Kevin MacLeod", x, y - 130);
            game.font.draw(game.batch, "Sound Effects - Universal Production Music", x, y - 270);
            game.font.draw(game.batch, "Art - Kaitlyn Nguy & Leonardo Emmanuel Pimentel", x, y - 360);
            game.font.draw(game.batch, "Scripting - Bolun Thompson & Erica Wang", x, y - 450);
            game.font.draw(game.batch, "Made with LibGDX", x, y - 570);
            game.font.draw(game.batch, "Special Thanks to Mr. Dunlea & Mr. Mez", x, y - 640);

            game.font.getData().setScale(0.6f);
            game.font.draw(game.batch, "Free download: https://filmmusic.io/song/3988-local-forecast-slower", x + 150, y - 50);
            game.font.draw(game.batch, "License (CC BY 4.0): https://filmmusic.io/standard-license", x + 150, y - 75);
            game.font.draw(game.batch, "Free download: https://filmmusic.io/song/3347-airport-lounge", x + 150, y - 180);
            game.font.draw(game.batch, "License (CC BY 4.0): https://filmmusic.io/standard-license", x + 150, y - 205);
        }

        game.batch.end();

        // buttons
        if(menuIndex == 0) {
            shapeRender.setProjectionMatrix(camera.combined);
            shapeRender.setColor(0.8f, 0.8f, 0.8f, 1.0f);

            shapeRender.begin(ShapeRenderer.ShapeType.Filled);
            shapeRender.rect(startButton.x, startButton.y, startButton.width, startButton.height);
            shapeRender.rect(tutorialButton.x, tutorialButton.y, tutorialButton.width, tutorialButton.height);
            shapeRender.rect(creditsButton.x, creditsButton.y, creditsButton.width, creditsButton.height);
            shapeRender.end();
        }

        game.batch.begin();

        // buttons text
        if(menuIndex == 0) {
            game.font.setColor(0.2f, 0.2f, 0.2f, 1.0f);
            game.font.draw(game.batch, "START", 820, 635 + buttonY);
            game.font.draw(game.batch, "TUTORIAL", 790, 505 + buttonY);
            game.font.draw(game.batch, "CREDITS", 810, 375 + buttonY);
        }

        game.batch.end();

        // input to view tutorial/credits & start game
        if (Gdx.input.justTouched()) {
            if(menuIndex == 0 && startButton.contains(Gdx.input.getX(), 900 - Gdx.input.getY())) {
                game.setScreen(new DayScreen(game, game.firstDay));
                dispose();
            }
            else if(menuIndex == 0 && tutorialButton.contains(Gdx.input.getX(), 900 - Gdx.input.getY())) {
                menuIndex = 1;
            }
            else if (menuIndex == 0 && creditsButton.contains(Gdx.input.getX(), 900 - Gdx.input.getY())) {
                menuIndex = 4;
            }
            else if (menuIndex != 0) {
                menuIndex ++;
                // reset to start screen after tutorial/credits frames(s) done
                if(menuIndex == 4 || menuIndex == 5)
                    menuIndex = 0;
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
        music.dispose();
    }

}
