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

import java.awt.*;
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

    private DayState dayState;

    private OrthographicCamera camera;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    private Player player, playerTwo;
    private boolean pressE, pressShift;
    private Array<Appliance> appliances;

    // for drawing orders
    private Texture orderTex, breadTex, hamTex, cheeseTex, lettuceTex, tomatoTex;

    // testing
    private static HitboxDrawer hitboxDrawer;

    exampleMusic bgMusic;

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

        camera = new OrthographicCamera();
        camera.setToOrtho(false, numWidthTiles, numHeightTiles);
        camera.update();

        tiledMap = new TmxMapLoader().load(dayState.mapFile);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 32f); // unit scale is probably wrong\

        orderTex = new Texture(Gdx.files.internal("Orders/Sprite-Order_Blank.png"));
        breadTex = new Texture(Gdx.files.internal("Ingredients/breadSlice.png"));
        hamTex = new Texture(Gdx.files.internal("Ingredients/ham.png"));
        cheeseTex = new Texture(Gdx.files.internal("Ingredients/cheese.png"));
        lettuceTex = new Texture(Gdx.files.internal("Ingredients/lettuce.png"));
        tomatoTex = new Texture(Gdx.files.internal("Ingredients/tomato.png"));;

        /**
         * The following music was used for this media project:
         * Music: Local Forecast - Slower by Kevin MacLeod
         * Free download: https://filmmusic.io/song/3988-local-forecast-slower
         * License (CC BY 4.0): https://filmmusic.io/standard-license
         */
        bgMusic = new exampleMusic(Gdx.audio.newMusic((Gdx.files.internal("Sounds/local-forecast-slower-by-kevin-macleod-from-filmmusic-io.mp3"))));

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
                    case Input.Keys.P: // pause music
                        bgMusic.pause();
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

        hitboxDrawer = new HitboxDrawer(camera, player, playerTwo, appliances);
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

        player.update(delta); // update player positions & animation frame
        playerTwo.update(delta);

        // check if player 1 is past screen border
        if(player.getX() < 0)
            player.setX(0);
        if (player.getX() > screenWidth - player.getWidth())
            player.setX(screenWidth - player.getWidth());
        if (player.getY() < 0)
            player.setY(0);
        if (player.getY() > screenHeight - player.getHeight())
            player.setY(screenHeight - player.getHeight());

        // check if player 2 is past screen border
        if(playerTwo.getX() < 0)
            playerTwo.setX(0);
        if(playerTwo.getX() > screenWidth - playerTwo.getWidth())
            playerTwo.setX(screenWidth - playerTwo.getWidth());
        if(playerTwo.getY() < 0)
            playerTwo.setY(0);
        if(playerTwo.getY() > screenHeight - playerTwo.getHeight())
            playerTwo.setY(screenHeight - playerTwo.getHeight());


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
            if (pressE && (Intersector.overlaps(app.getInteractRegion(), player.getInteractRectangle())
                    || Intersector.overlaps(app.getInteractRegion2(), player.getInteractRectangle()))
                    && app.canInteract(player.getItem())) {
                player.interact(app.interact(player.getItem()));
            }

            if (pressShift && (Intersector.overlaps(app.getInteractRegion(), playerTwo.getInteractRectangle())
                    || Intersector.overlaps(app.getInteractRegion2(), playerTwo.getInteractRectangle()))
                    && app.canInteract(playerTwo.getItem())) {
                playerTwo.interact(app.interact(playerTwo.getItem()));
            }
        }
        pressE = false;
        pressShift = false;

        // draw hitboxes
        hitboxDrawer.drawCollision();
        hitboxDrawer.drawInteraction();

        // draw screen
        game.batch.begin();
        for (Appliance app : appliances) { // appliances
            app.draw(game.batch);
        }
        player.draw(game.batch); // player
        playerTwo.draw(game.batch);

        // draw order paper
        drawOrder(game.batch);

        // draw timer & # orders remaining
        game.font.getData().setScale(1.1f);
        game.font.draw(game.batch, "Orders left: " + (dayState.wantedOrders - dayState.orderIndex), tileWidth * 9.2f, tileHeight * 1.4f);
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
        if (order.shouldBeGrilled) {
            game.font.draw(batch, "Grill!", 115, 710);
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
        breadTex.dispose();
        hamTex.dispose();
        cheeseTex.dispose();
        lettuceTex.dispose();
        tomatoTex.dispose();

        bgMusic.dispose();
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

    public Array<Appliance> getDay1Apps() {
        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(8, 8)); // top counters
        apps.add(new Counter(11, 8));
        apps.add(new Counter(3, 6)); // left counters
        apps.add(new Counter(3, 3));
        for(int i = 0; i < 5; i++) {      // bottom counters
            apps.add(new Counter(6 + i, 2));
        }

        apps.add(new Crate(6, 5, Holdable.Type.bread)); // bread container
        apps.add(new Crate(7, 5, Holdable.Type.ham)); // ham container
        apps.add(new Crate(8, 5, Holdable.Type.cheese)); // cheese container
        apps.add(new Crate(9, 5, Holdable.Type.lettuce)); // lettuce container
        apps.add(new Crate(10, 5, Holdable.Type.tomato)); // tomato container

        apps.add(new ChoppingBoard(3, 5)); // top cutting board
        apps.add(new ChoppingBoard(3, 4)); // bottom cutting board
        apps.add(new Toaster(6, 8)); // toaster (left)
        apps.add(new Toaster(7, 8)); // toaster (right)
        apps.add(new Trash(11, 2)); // trash
        apps.add(new ServingWindow(9, 8, this.dayState)); // serving windows (2x1)

        return apps;
    }

    public Array<Appliance> getDay2Apps() {
        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(11, 8)); // top counter
        apps.add(new Counter(3, 5));  // left counter
        for (int i = 0; i < 5; i++) {      // bottom counters
            apps.add(new Counter(6 + i, 2));
        }

        apps.add(new Crate(5, 8, Holdable.Type.bread)); // bread container
        apps.add(new Crate(6, 8, Holdable.Type.wheatBread)); // wheat bread container
        apps.add(new Crate(7, 5, Holdable.Type.ham)); // ham container
        apps.add(new Crate(8, 5, Holdable.Type.cheese)); // cheese container
        apps.add(new Crate(9, 5, Holdable.Type.lettuce)); // lettuce container
        apps.add(new Crate(10, 5, Holdable.Type.tomato)); // tomato container

        apps.add(new Toaster(7, 8)); // left toaster
        apps.add(new Toaster(8, 8)); // right toaster
        apps.add(new ServingWindow(9, 8, this.dayState)); // serving windows (2x1)
        apps.add(new ChoppingBoard(3, 7)); // top cutting board
        apps.add(new ChoppingBoard(3, 6)); // top cutting board
        apps.add(new FryingPan(3, 4)); // top frying pan
        apps.add(new FryingPan(3, 3)); // bottom frying pan
        apps.add(new Trash(11, 2)); // trash

        return apps;
    }

    public Array<Appliance> getDay3Apps() {
        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(11, 8)); // top right counter
        apps.add(new Counter(3, 4)); // left counter
        apps.add(new Counter(6, 2)); // bottom counters
        for (int i = 0; i < 2; i++) {
            apps.add(new Counter(9 + i, 2));
        }

        apps.add(new Crate(4, 8, Holdable.Type.bread)); // bread container
        apps.add(new Crate(5, 8, Holdable.Type.wheatBread)); // wheat bread container
        apps.add(new Crate(6, 8, Holdable.Type.sourBread)); // sour bread container
        apps.add(new Crate(6, 5, Holdable.Type.ham)); // ham container
        apps.add(new Crate(7, 5, Holdable.Type.cheese)); // cheese container
        apps.add(new Crate(8, 5, Holdable.Type.lettuce)); // lettuce container
        apps.add(new Crate(9, 5, Holdable.Type.tomato)); // tomato container

        apps.add(new Toaster(7, 8)); // left toaster
        apps.add(new Toaster(8, 8)); // right toaster
        apps.add(new ServingWindow(9, 8, this.dayState)); // serving windows (2x1)
        apps.add(new ChoppingBoard(3, 6)); // top cutting board
        apps.add(new ChoppingBoard(3, 5)); // top cutting board
        apps.add(new FryingPan(3, 3)); // top frying pan
        apps.add(new FryingPan(3, 2)); // bottom frying pan
        apps.add(new KetchupBottle(7, 2)); // ketchup
        apps.add(new MustardBottle(8, 2)); // mustard
        apps.add(new Trash(11, 2)); // trash

        return apps;
    }

    public Array<Appliance> getDay4Apps() {
        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(11, 8)); // top right counter
        apps.add(new Counter(6, 2)); // bottom counters
        apps.add(new Barrier(9, 2));
        apps.add(new Counter(10, 2));

        apps.add(new Crate(4, 8, Holdable.Type.bread)); // bread container
        apps.add(new Crate(5, 8, Holdable.Type.wheatBread)); // wheat bread container
        apps.add(new Crate(6, 8, Holdable.Type.sourBread)); // sour bread container
        apps.add(new Crate(5, 5, Holdable.Type.ham)); // ham container
        apps.add(new Crate(6, 5, Holdable.Type.cheese)); // cheese container
        apps.add(new Crate(7, 5, Holdable.Type.lettuce)); // lettuce container
        apps.add(new Crate(8, 5, Holdable.Type.tomato)); // tomato container
        apps.add(new Crate(9, 5, Holdable.Type.minionBread)); // minion bread container

        apps.add(new Barrier(7, 8)); // left toaster
        apps.add(new Toaster(8, 8)); // right toaster
        apps.add(new ServingWindow(9, 8, this.dayState)); // serving windows (2x1)
        apps.add(new ChoppingBoard(2, 6)); // top cutting board
        apps.add(new ChoppingBoard(2, 5)); // top cutting board
        apps.add(new FryingPan(2, 4)); // top frying pan
        apps.add(new Barrier(2, 3)); // bottom frying pan
        apps.add(new KetchupBottle(7, 2)); // ketchup
        apps.add(new MustardBottle(8, 2)); // mustard
        apps.add(new Trash(11, 2)); // trash

        apps.add(new Barrier(10, 6)); // banana peels
        apps.add(new Barrier(11, 4));
        apps.add(new Barrier(9, 2));

        return apps;
    }

    public Array<Appliance> getDay5Apps() {
        Array<Appliance> apps = new Array<Appliance>();

        apps.add(new Counter(10, 7)); // top right counter
        apps.add(new Barrier(5, 1)); // bottom counters
        apps.add(new Barrier(8, 1));
        apps.add(new Counter(9, 1));

        apps.add(new Crate(3, 7, Holdable.Type.bread)); // bread container
        apps.add(new Crate(4, 7, Holdable.Type.wheatBread)); // wheat bread container
        apps.add(new Crate(5, 7, Holdable.Type.sourBread)); // sour bread container
        apps.add(new Crate(4, 4, Holdable.Type.ham)); // ham container
        apps.add(new Crate(5, 4, Holdable.Type.cheese)); // cheese container
        apps.add(new Crate(6, 4, Holdable.Type.lettuce)); // lettuce container
        apps.add(new Crate(7, 4, Holdable.Type.tomato)); // tomato container
        apps.add(new Crate(8, 4, Holdable.Type.minionBread)); // minion bread container

        apps.add(new Barrier(6,7)); // left toaster
        apps.add(new Toaster(7, 7)); // right toaster
        apps.add(new ServingWindow(8, 7, this.dayState)); // serving windows (2x1)
        apps.add(new Barrier(1, 5)); // top cutting board
        apps.add(new ChoppingBoard(1, 4)); // bottom cutting board
        apps.add(new FryingPan(1, 3)); // top frying pan
        apps.add(new Barrier(1, 2)); // bottom frying pan
        apps.add(new KetchupBottle(6, 1)); // ketchup
        apps.add(new MustardBottle(7, 1)); // mustard
        apps.add(new Trash(10, 1)); // trash

        apps.add(new Barrier(0, 8, 12, 1)); // fire border
        apps.add(new Barrier(11, 0, 1, 9));
        apps.add(new Barrier(0, 0, 12, 1));
        apps.add(new Barrier(0, 0, 1, 9));

        apps.add(new Barrier(2, 6)); // fire
        apps.add(new Barrier(9, 4));
        apps.add(new Barrier(5, 3));
        apps.add(new Barrier(10, 3));
        apps.add(new Barrier(3, 2));

        return apps;
    }
}
