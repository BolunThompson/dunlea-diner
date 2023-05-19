package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Day1Screen class
 *
 * Created: May 19, 2023
 * Last Updated:
 *
 * Known Issues:
 * Pls bring back the .tsx file.
 */
public class Day1Screen implements Screen {
    final Diner game;

    OrthographicCamera camera;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    Texture playerImage;
    Player player;

    public Day1Screen(Diner game)
    {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        camera.update();

        tiledMap = new TmxMapLoader().load("Maps/day1Map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/32f);

        playerImage = new Texture(Gdx.files.internal("Grass.png"));  // placeholder texture
        player = new Player(playerImage, 32, 32);

        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean keyDown(int keycode)
            {
                switch(keycode)
                {
                    case Input.Keys.LEFT:
                        player.moveLeft = true;
                        break;
                    case Input.Keys.RIGHT:
                        player.moveRight = true;
                        break;
                    case Input.Keys.UP:
                        player.moveUp = true;
                        break;
                    case Input.Keys.DOWN:
                        player.moveDown = true;
                        break;
                }
                return true;
            }
            public boolean keyUp(int keycode)
            {
                switch(keycode)
                {
                    case Input.Keys.LEFT:
                        player.moveLeft = false;
                        break;
                    case Input.Keys.RIGHT:
                        player.moveRight = false;
                        break;
                    case Input.Keys.UP:
                        player.moveUp = false;
                        break;
                    case Input.Keys.DOWN:
                        player.moveDown = false;
                        break;
                }
                return true;
            }
        });  // there may be a better way to do input, but this functions
    }

    public void render(float delta)
    {
        ScreenUtils.clear(0,0,0,1);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        player.update(delta);

        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();
    }

    public void dispose()
    {
        playerImage.dispose();
    }

    @Override
    public void resize(int width, int height) {}
    public void show() {} // runs upon screen shown
    public void hide() {}
    public void pause() {}
    public void resume() {}
}
