package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.appliance.*;

/**
 * DayScreen class
 *
 * Created: May 19, 2023
 *
 * NOTES:
 *
 * For now everything is a counter. Only bottom cutting board has a test interaction region set up.
 */
public class DayScreen implements Screen {
    final Diner game;

    // screen dimensions in pixels (1200 x 900)
    static final int screenWidth = 100 * 12;
    static final int screenHeight = 100 * 9;

    // screen dimensions in tiles (12 x 9)
    static final int numWidthTiles = 12;
    static final int numHeightTiles = 9;

    static final int tileWidth = screenWidth / numWidthTiles;
    static final int tileHeight = screenHeight / numHeightTiles;

    DayState dayState;

    OrthographicCamera camera;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    exampleMusic proudLion;
    exampleSound tallgiraffe;

    Array<Appliance> appliances;

    Player player;
    boolean pressE;

    DayScreen(Diner game, DayState dayState)
    {
        this.game = game;
        this.dayState = dayState;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, numWidthTiles, numHeightTiles);
        camera.update();

        tiledMap = new TmxMapLoader().load(dayState.mapFile);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/32f); // unit scale is probably wrong

        /**
         * The following music was used for this media project:
         * Music: Local Forecast - Slower by Kevin MacLeod
         * Free download: https://filmmusic.io/song/3988-local-forecast-slower
         * License (CC BY 4.0): https://filmmusic.io/standard-license
         */
        proudLion = new exampleMusic(Gdx.audio.newMusic((Gdx.files.internal("Sounds/local-forecast-slower-by-kevin-macleod-from-filmmusic-io.mp3"))));
        tallgiraffe = new exampleSound(Gdx.audio.newSound(Gdx.files.internal("Sounds/Toaster-pop-up.wav")));


        appliances = new Array<Appliance>();

        // blank countertops
        appliances.add(new Counter(tileWidth * 3, tileHeight * 3, tileWidth, tileHeight * 4, 4, Appliance.direction.UP)); // left counter
        appliances.add(new Counter(tileWidth * 5, tileHeight * 2, tileWidth * 6, tileHeight, 6, Appliance.direction.RIGHT)); // bottom counter
        appliances.add(new Counter(tileWidth * 8, tileHeight * 8, tileWidth * 6, tileHeight, 6, Appliance.direction.RIGHT)); // top counter

        // ingredient containers
        appliances.add(new Crate(tileWidth * 6, tileHeight * 5, tileWidth, tileHeight, Ingredient.Type.bread)); // bread container
        appliances.add(new Crate(tileWidth * 7, tileHeight * 5, tileWidth, tileHeight, Ingredient.Type.ham)); // ham container
        appliances.add(new Crate(tileWidth * 8, tileHeight * 5, tileWidth, tileHeight, Ingredient.Type.cheese)); // cheese container
        appliances.add(new Crate(tileWidth * 9, tileHeight * 5, tileWidth, tileHeight, Ingredient.Type.lettuce)); // lettuce container
        appliances.add(new Crate(tileWidth * 10, tileHeight * 5, tileWidth, tileHeight, Ingredient.Type.tomato)); // tomato container

        // appliances
        //appliances.add(new ChoppingBoard(tileWidth * 3, tileHeight * 4, tileWidth, tileHeight)); // bottom cutting board
        appliances.add(new Toaster(tileWidth * 6, tileHeight * 8, tileWidth, tileHeight)); // toaster (left)
        appliances.add(new Toaster(tileWidth * 7, tileHeight * 8, tileWidth, tileHeight)); // toaster (right)
        appliances.add(new Trash(tileWidth * 11, tileHeight * 2, tileWidth, tileHeight)); // trash
        appliances.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight)); // serving windows (2x1)

        player = new Player((int)(tileWidth - 4), (int)(tileHeight - 4));

        pressE = false;
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean keyDown(int keycode)
            {
                switch(keycode)
                {
                    case Input.Keys.A:
                        player.moveLeft = true;
                        break;
                    case Input.Keys.D:
                        player.moveRight = true;
                        break;
                    case Input.Keys.W:
                        player.moveUp = true;
                        break;
                    case Input.Keys.S:
                        player.moveDown = true;
                        break;
                    case Input.Keys.E: // interact key
                        pressE = true;
                        break;
                    case Input.Keys.SPACE: // test sound
                        tallgiraffe.playSound();
                        break;
                    case Input.Keys.M: // test music
                        proudLion.pause();
                        break;
                }
                return true;
            }
            public boolean keyUp(int keycode)
            {
                switch(keycode)
                {
                    case Input.Keys.A:
                        player.moveLeft = false;
                        break;
                    case Input.Keys.D:
                        player.moveRight = false;
                        break;
                    case Input.Keys.W:
                        player.moveUp = false;
                        break;
                    case Input.Keys.S:
                        player.moveDown = false;
                        break;
                }
                return true;
            }
        });  // there may be a better way to do input, but this functions
    }

    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(0,0,0,1);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        dayState.currentTime += delta;
        if (dayState.isOver())
        {
            nextLevel();
            return;
        }

        // if collision, player position is set to (oldX, oldY)
        float oldX = player.getX();
        float oldY = player.getY();

        player.update(delta); // update player position & animation frame

        // check if past screen border
        if(player.getX() < 0)
            player.setX(0);
        if(player.getX() > screenWidth - player.getWidth())
            player.setX(screenWidth - player.getWidth());
        if(player.getY() < 0)
            player.setY(0);
        if(player.getY() > screenHeight - player.getHeight())
            player.setY(screenHeight - player.getHeight());


        // appliance collision & interaction
        boolean withinInteractRegion = false;
        for(Appliance app:appliances)
        {
            app.update(delta); // update time info for appliance animation

            // collision
            Rectangle appRect = app.getCollisionRegion();
            if(Intersector.overlaps(new Rectangle(appRect.getX()+appRect.getWidth()-1, appRect.getY(), 1, appRect.getHeight()), player.getBoundingRectangle())
                    || Intersector.overlaps(new Rectangle(appRect.getX(), appRect.getY(), 1, appRect.getHeight()), player.getBoundingRectangle()))
                player.setX(oldX);
            if(Intersector.overlaps(new Rectangle(appRect.getX(), appRect.getY(), appRect.getWidth(), 1), player.getBoundingRectangle()) ||
                    Intersector.overlaps(new Rectangle(appRect.getX(), appRect.getY() + appRect.getHeight()-1, appRect.getWidth(), 1), player.getBoundingRectangle()))
                player.setY(oldY);

            // interaction
            if(pressE && Intersector.overlaps(app.getInteractRegion(), player.getInteractRectangle()) && app.canInteract(player.getIngredient()))
            {
                player.interact(app.interact(player.getIngredient()));
            }
        }
        pressE = false;

        // draw player
        game.batch.begin();
        for(Appliance app:appliances)
        {
            app.draw(game.batch);
        }
        player.draw(game.batch);
        game.batch.end();
    }

    public void nextLevel()
    {
        dispose();
        game.setScreen(new TransitionScreen(game, dayState));
    }

    @Override
    public void dispose() {
        player.dispose();
        for(Appliance app:appliances) {
            app.dispose();
        }
        proudLion.dispose();
        tallgiraffe.dispose();
    }
    public void resize(int width, int height) {}
    public void show() {} // runs upon screen shown
    public void hide() {}
    public void pause() {}
    public void resume() {}
}
