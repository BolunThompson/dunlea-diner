package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.appliance.*;
import com.mygdx.game.holdable.Holdable;
import com.mygdx.game.holdable.Ingredient;

import java.text.DecimalFormat;
import java.util.Iterator;

/**
 * DayScreen class
 *
 * Created: May 19, 2023
 */
public class DayScreen implements Screen {
    final Diner game;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    // screen dimensions in pixels (1200 x 900)
    static final int screenWidth = 100 * 12;
    static final int screenHeight = 100 * 9;

    // screen dimensions in tiles (12 x 9)
    static final int numWidthTiles = 12;
    static final int numHeightTiles = 9;

    // tile dimensions in pixels (100 x 100)
    static final int tileWidth = screenWidth / numWidthTiles;
    static final int tileHeight = screenHeight / numHeightTiles;

    DayState dayState;

    OrthographicCamera camera;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    Player player, playerTwo;
    private boolean pressE, pressShift;
    private Array<Appliance> appliances;

    Texture orderTex;

    // delete later - for testing purposes
    exampleMusic proudLion;
    ShapeRenderer showHitboxRender;

    DayScreen(Diner game, DayState dayState) {
        this.game = game;
        this.dayState = dayState;

        switch(dayState.level) {
            case 1:
            default:
                appliances = getDay1Apps();
                break;
            case 2:
                appliances = getDay2Apps();
                break;
            case 3:
                appliances = getDay3Apps();
                break;
            case 4:
                appliances = getDay4Apps();
                break;
            case 5:
                appliances = getDay5Apps();
                break;
        }
        orderTex = new Texture(Gdx.files.internal("Orders/Sprite-Order_Blank.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, numWidthTiles, numHeightTiles);
        camera.update();

        tiledMap = new TmxMapLoader().load(dayState.mapFile);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 32f); // unit scale is probably wrong

        /**
         * The following music was used for this media project:
         * Music: Local Forecast - Slower by Kevin MacLeod
         * Free download: https://filmmusic.io/song/3988-local-forecast-slower
         * License (CC BY 4.0): https://filmmusic.io/standard-license
         */
        proudLion = new exampleMusic(Gdx.audio
                .newMusic((Gdx.files.internal("Sounds/local-forecast-slower-by-kevin-macleod-from-filmmusic-io.mp3"))));
        showHitboxRender = new ShapeRenderer();

        /**
         * PLAYER & CONTROLS
         */
        player = new Player(new Texture(Gdx.files.internal("Misc/Sprite-Chef_WalkAll.png")), (int) (tileWidth),
                (int) (tileHeight));
        playerTwo = new Player(new Texture(Gdx.files.internal("Misc/Sprite-Chef_WalkAllP2.png")), (int) (tileWidth),
                (int) (tileHeight));
        pressE = false;
        pressShift = false;
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean keyDown(int keycode) {
                switch (keycode) {
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

                    case Input.Keys.LEFT:
                        playerTwo.moveLeft = true;
                        break;
                    case Input.Keys.RIGHT:
                        playerTwo.moveRight = true;
                        break;
                    case Input.Keys.UP:
                        playerTwo.moveUp = true;
                        break;
                    case Input.Keys.DOWN:
                        playerTwo.moveDown = true;
                        break;
                    case Input.Keys.SHIFT_RIGHT: // interact key
                        pressShift = true;
                        break;

                    // TEST STUFF (delete later)
                    case Input.Keys.M: // test music
                        proudLion.pause();
                        break;
                    case Input.Keys.Q: // skip day
                        dayState.currentTime = DayState.maxTime;
                        break;
                }
                return true;
            }

            public boolean keyUp(int keycode) {
                switch (keycode) {
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

                    case Input.Keys.LEFT:
                        playerTwo.moveLeft = false;
                        break;
                    case Input.Keys.RIGHT:
                        playerTwo.moveRight = false;
                        break;
                    case Input.Keys.UP:
                        playerTwo.moveUp = false;
                        break;
                    case Input.Keys.DOWN:
                        playerTwo.moveDown = false;
                        break;
                }
                return true;
            }
        }); // there may be a better way to do input, but this functions
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        dayState.currentTime += delta;
        if (dayState.isOver()) {
            nextLevel();
            return;
        }

        // save old position in case of collision
        // if collision, player position is set to (oldX, oldY)
        float oldX = player.getX();
        float oldY = player.getY();
        float oldX2 = playerTwo.getX();
        float oldY2 = playerTwo.getY();

        player.update(delta); // update player position & animation frame
        playerTwo.update(delta);

        // check if player is past screen border
        if (player.getX() < 0)
            player.setX(0);
        if (player.getX() > screenWidth - player.getWidth())
            player.setX(screenWidth - player.getWidth());
        if (player.getY() < 0)
            player.setY(0);
        if (player.getY() > screenHeight - player.getHeight())
            player.setY(screenHeight - player.getHeight());

        // appliance collision & interaction
        for (Appliance app : appliances) {
            app.update(delta); // update time info for appliance animation

            // collision
            Rectangle appRect = app.getCollisionRegion();
            if (Intersector
                    .overlaps(new Rectangle(appRect.getX() + appRect.getWidth() - 1, appRect.getY(), 1,
                            appRect.getHeight()), player.getBoundingRectangle())
                    || Intersector.overlaps(new Rectangle(appRect.getX(), appRect.getY(), 1, appRect.getHeight()),
                            player.getBoundingRectangle()))
                player.setX(oldX);
            if (Intersector.overlaps(new Rectangle(appRect.getX(), appRect.getY(), appRect.getWidth(), 1),
                    player.getBoundingRectangle()) ||
                    Intersector.overlaps(new Rectangle(appRect.getX(), appRect.getY() + appRect.getHeight() - 1,
                            appRect.getWidth(), 1), player.getBoundingRectangle()))
                player.setY(oldY);

            if (Intersector
                    .overlaps(new Rectangle(appRect.getX() + appRect.getWidth() - 1, appRect.getY(), 1,
                            appRect.getHeight()), playerTwo.getBoundingRectangle())
                    || Intersector.overlaps(new Rectangle(appRect.getX(), appRect.getY(), 1, appRect.getHeight()),
                            playerTwo.getBoundingRectangle()))
                playerTwo.setX(oldX2);
            if (Intersector.overlaps(new Rectangle(appRect.getX(), appRect.getY(), appRect.getWidth(), 1),
                    playerTwo.getBoundingRectangle()) ||
                    Intersector.overlaps(new Rectangle(appRect.getX(), appRect.getY() + appRect.getHeight() - 1,
                            appRect.getWidth(), 1), playerTwo.getBoundingRectangle()))
                playerTwo.setY(oldY2);

            // interaction
            if (pressE && Intersector.overlaps(app.getInteractRegion(), player.getInteractRectangle())
                    && app.canInteract(player.getItem())) {
                player.interact(app.interact(player.getItem()));
            }

            if (pressShift && Intersector.overlaps(app.getInteractRegion(), playerTwo.getInteractRectangle())
                    && app.canInteract(playerTwo.getItem())) {
                playerTwo.interact(app.interact(playerTwo.getItem()));
            }
        }
        pressE = false;
        pressShift = false;

        // TEST STUFF (delete later)
        showHitboxRender.begin(ShapeRenderer.ShapeType.Filled);
        showHitboxRender.setColor(0.1f, 1f, 0.3f, 1f);
        showHitboxRender.rect(player.getBoundingRectangle().getX(), player.getBoundingRectangle().getY(),
                player.getBoundingRectangle().getWidth(), player.getBoundingRectangle().getHeight());
        showHitboxRender.end();

        // draw screen
        game.batch.begin();
        player.draw(game.batch); // player
        playerTwo.draw(game.batch);
        for (Appliance app : appliances) { // appliances
            app.draw(game.batch);
        }

        // draw order paper
        drawOrder(game.batch);

        // draw timer & # orders remaining
        game.font.getData().setScale(1.1f);
        game.font.draw(game.batch, "Orders left: ", tileWidth * 9.2f, tileHeight * 1.4f);
        game.font.getData().setScale(1.5f);
        game.font.draw(game.batch, String.format("%.02f", DayState.maxTime - dayState.currentTime), tileWidth * 9.4f,
                tileHeight * 0.7f);

        game.batch.end();
    }

    public void nextLevel() {
        dispose();
        game.setScreen(new TransitionScreen(game, dayState));
    }

    public void drawOrder(Batch batch) {
        float tempX = -30;
        float tempY = 670;
        final float tempWidth = tileWidth * 1.5f;
        final float tempHeight = tileHeight * 1.5f;

        batch.draw(orderTex, tempX + 30, tempY - 20, tempWidth / 1.5f * 2.5f, tileHeight * 2.5f);
        Order order = dayState.orders.get(dayState.orderIndex);
        Array<Ingredient> ingredients = order.ingredients.keys().toArray();
        ingredients.add(order.bread);
        for (int i = 0; i < ingredients.size; i++) {
            if (i != 0 && i % 3 == 0) {
                tempY -= tileHeight * 0.6;
                tempX -= tileWidth * 0.8 * 3;
            }
            Ingredient ingredient = ingredients.get(i);
            batch.draw(ingredient.getIcon(), tempX, tempY, tempWidth, tempHeight);
            tempX += tileWidth * 0.8f;
        }

    }

    @Override
    public void dispose() {
        player.dispose();
        playerTwo.dispose();
        for (Appliance app : appliances) {
            app.dispose();
        }
        orderTex.dispose();

        // TEST STUFF (delete later)
        proudLion.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    } // runs upon screen shown

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {}

    // putting this mess down here
    public Array<Appliance> getDay1Apps() {
        int tileWidth = 100;
        int tileHeight = 100;

        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(tileWidth * 3, tileHeight * 6, tileWidth, tileHeight, Appliance.direction.UP)); // left top counter
        apps.add(new Counter(tileWidth * 3, tileHeight * 3, tileWidth, tileHeight, Appliance.direction.UP)); // left bottom counter
        for (int i = 0; i < 5; i++) {                                                                                                    // bottom counters
            apps.add(new Counter(tileWidth * (6 + i), tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT));
        }
        apps.add(new Counter(tileWidth * 8, tileHeight * 8, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top left counter
        apps.add(new Counter(tileWidth * 11, tileHeight * 8, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top right counter

        apps.add(new Crate(tileWidth * 6, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.bread)); // bread container
        apps.add(new Crate(tileWidth * 7, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.ham)); // ham container
        apps.add(new Crate(tileWidth * 8, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.cheese)); // cheese container
        apps.add(new Crate(tileWidth * 9, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.lettuce)); // lettuce container
        apps.add(new Crate(tileWidth * 10, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.tomato)); // tomato container

        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 5, tileWidth, tileHeight)); // top cutting board
        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 4, tileWidth, tileHeight)); // bottom cutting board
        apps.add(new Toaster(tileWidth * 6, tileHeight * 8, tileWidth, tileHeight)); // toaster (left)
        apps.add(new Toaster(tileWidth * 7, tileHeight * 8, tileWidth, tileHeight)); // toaster (right)
        apps.add(new Trash(tileWidth * 11, tileHeight * 2, tileWidth, tileHeight)); // trash
        apps.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight, this.dayState)); // serving windows (2x1)

        return apps;
    }

    public Array<Appliance> getDay2Apps() {
        int tileWidth = 100;
        int tileHeight = 100;

        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(tileWidth * 11, tileHeight * 8, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top right counter
        apps.add(new Counter(tileWidth * 3, tileHeight * 5, tileWidth, tileHeight, Appliance.direction.UP)); // mid left counter
        for (int i = 0; i < 5; i++) {                                                        // bottom counters
            apps.add(new Counter(tileWidth * (6 + i), tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT));
        }

        apps.add(new Crate(tileWidth * 5, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.bread)); // bread container
        apps.add(new Crate(tileWidth * 6, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.wheatBread)); // wheat bread container
        apps.add(new Crate(tileWidth * 7, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.ham)); // ham container
        apps.add(new Crate(tileWidth * 8, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.cheese)); // cheese container
        apps.add(new Crate(tileWidth * 9, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.lettuce)); // lettuce container
        apps.add(new Crate(tileWidth * 10, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.tomato)); // tomato container

        apps.add(new Toaster(tileWidth * 7, tileHeight * 8, tileWidth, tileHeight)); // left toaster
        apps.add(new Toaster(tileWidth * 8, tileHeight * 8, tileWidth, tileHeight)); // right toaster
        apps.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight, this.dayState)); // serving windows (2x1)
        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 7, tileWidth, tileHeight)); // top cutting board
        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 6, tileWidth, tileHeight)); // top cutting board
        apps.add(new FryingPan(tileWidth * 3, tileHeight * 4, tileWidth, tileHeight)); // top frying pan
        apps.add(new FryingPan(tileWidth * 3, tileHeight * 3, tileWidth, tileHeight)); // bottom frying pan
        apps.add(new Trash(tileWidth * 11, tileHeight * 2, tileWidth, tileHeight)); // trash

        return apps;
    }

    public Array<Appliance> getDay3Apps() {
        int tileWidth = 100;
        int tileHeight = 100;

        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(tileWidth * 11, tileHeight * 8, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top right counter
        apps.add(new Counter(tileWidth * 6, tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT)); // bottom left counter
        for (int i = 0; i < 2; i++) {                                                        // bottom right counters
            apps.add(new Counter(tileWidth * (9 + i), tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT));
        }

        apps.add(new Crate(tileWidth * 4, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.bread)); // bread container
        apps.add(new Crate(tileWidth * 5, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.wheatBread)); // wheat bread container
        apps.add(new Crate(tileWidth * 6, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.sourBread)); // sour bread container
        apps.add(new Crate(tileWidth * 6, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.ham)); // ham container
        apps.add(new Crate(tileWidth * 7, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.cheese)); // cheese container
        apps.add(new Crate(tileWidth * 8, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.lettuce)); // lettuce container
        apps.add(new Crate(tileWidth * 9, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.tomato)); // tomato container

        apps.add(new Toaster(tileWidth * 7, tileHeight * 8, tileWidth, tileHeight)); // left toaster
        apps.add(new Toaster(tileWidth * 8, tileHeight * 8, tileWidth, tileHeight)); // right toaster
        apps.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight, this.dayState)); // serving windows (2x1)
        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 6, tileWidth, tileHeight)); // top cutting board
        apps.add(new ChoppingBoard(tileWidth * 3, tileHeight * 5, tileWidth, tileHeight)); // top cutting board
        apps.add(new FryingPan(tileWidth * 3, tileHeight * 4, tileWidth, tileHeight)); // top frying pan
        apps.add(new FryingPan(tileWidth * 3, tileHeight * 3, tileWidth, tileHeight)); // bottom frying pan
        apps.add(new KetchupBottle(tileWidth * 7, tileHeight * 2, tileWidth, tileHeight)); // ketchup
        apps.add(new MustardBottle(tileWidth * 8, tileHeight * 2, tileWidth, tileHeight)); // mustard
        apps.add(new Trash(tileWidth * 11, tileHeight * 2, tileWidth, tileHeight)); // trash

        return apps;
    }

    public Array<Appliance> getDay4Apps() {
        int tileWidth = 100;
        int tileHeight = 100;

        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(tileWidth * 11, tileHeight * 8, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top right counter
        apps.add(new Counter(tileWidth * 6, tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT)); // bottom left counter
        apps.add(new Counter(tileWidth * 10, tileHeight * 2, tileWidth, tileHeight, Appliance.direction.RIGHT)); // bottom right counter

        apps.add(new Crate(tileWidth * 4, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.bread)); // bread container
        apps.add(new Crate(tileWidth * 5, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.wheatBread)); // wheat bread container
        apps.add(new Crate(tileWidth * 6, tileHeight * 8, tileWidth, tileHeight, Holdable.Type.sourBread)); // sour bread container
        apps.add(new Crate(tileWidth * 5, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.ham)); // ham container
        apps.add(new Crate(tileWidth * 6, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.cheese)); // cheese container
        apps.add(new Crate(tileWidth * 7, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.lettuce)); // lettuce container
        apps.add(new Crate(tileWidth * 8, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.tomato)); // tomato container
        apps.add(new Crate(tileWidth * 9, tileHeight * 5, tileWidth, tileHeight, Holdable.Type.minionBread)); // minion bread container

        apps.add(new Toaster(tileWidth * 8, tileHeight * 8, tileWidth, tileHeight)); // right toaster
        apps.add(new ServingWindow(tileWidth * 9, tileHeight * 8, tileWidth * 2, tileHeight, this.dayState)); // serving windows (2x1)
        apps.add(new ChoppingBoard(tileWidth * 2, tileHeight * 6, tileWidth, tileHeight)); // top cutting board
        apps.add(new ChoppingBoard(tileWidth * 2, tileHeight * 5, tileWidth, tileHeight)); // top cutting board
        apps.add(new FryingPan(tileWidth * 2, tileHeight * 4, tileWidth, tileHeight)); // top frying pan
        apps.add(new KetchupBottle(tileWidth * 7, tileHeight * 2, tileWidth, tileHeight)); // ketchup
        apps.add(new MustardBottle(tileWidth * 8, tileHeight * 2, tileWidth, tileHeight)); // mustard
        apps.add(new Trash(tileWidth * 11, tileHeight * 2, tileWidth, tileHeight)); // trash

        return apps;
    }

    public Array<Appliance> getDay5Apps() {
        int tileWidth = 100;
        int tileHeight = 100;

        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(tileWidth * 10, tileHeight * 7, tileWidth, tileHeight, Appliance.direction.RIGHT)); // top right counter
        apps.add(new Counter(tileWidth * 10, tileHeight, tileWidth, tileHeight, Appliance.direction.RIGHT)); // bottom right counter

        apps.add(new Crate(tileWidth * 3, tileHeight * 7, tileWidth, tileHeight, Holdable.Type.bread)); // bread container
        apps.add(new Crate(tileWidth * 4, tileHeight * 7, tileWidth, tileHeight, Holdable.Type.wheatBread)); // wheat bread container
        apps.add(new Crate(tileWidth * 5, tileHeight * 7, tileWidth, tileHeight, Holdable.Type.sourBread)); // sour bread container
        apps.add(new Crate(tileWidth * 4, tileHeight * 4, tileWidth, tileHeight, Holdable.Type.ham)); // ham container
        apps.add(new Crate(tileWidth * 5, tileHeight * 4, tileWidth, tileHeight, Holdable.Type.cheese)); // cheese container
        apps.add(new Crate(tileWidth * 6, tileHeight * 4, tileWidth, tileHeight, Holdable.Type.lettuce)); // lettuce container
        apps.add(new Crate(tileWidth * 7, tileHeight * 4, tileWidth, tileHeight, Holdable.Type.tomato)); // tomato container
        apps.add(new Crate(tileWidth * 8, tileHeight * 4, tileWidth, tileHeight, Holdable.Type.minionBread)); // minion bread container

        apps.add(new Toaster(tileWidth * 7, tileHeight * 7, tileWidth, tileHeight)); // right toaster
        apps.add(new ServingWindow(tileWidth * 8, tileHeight * 7, tileWidth * 2, tileHeight, this.dayState)); // serving windows (2x1)
        apps.add(new ChoppingBoard(tileWidth, tileHeight * 4, tileWidth, tileHeight)); // bottom cutting board
        apps.add(new FryingPan(tileWidth, tileHeight * 3, tileWidth, tileHeight)); // top frying pan
        apps.add(new KetchupBottle(tileWidth * 7, tileHeight, tileWidth, tileHeight)); // ketchup
        apps.add(new MustardBottle(tileWidth * 8, tileHeight, tileWidth, tileHeight)); // mustard
        apps.add(new Trash(tileWidth * 10, tileHeight, tileWidth, tileHeight)); // trash

        return apps;
    }
}
