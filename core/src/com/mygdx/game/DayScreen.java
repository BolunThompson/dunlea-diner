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
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.appliance.*;
import com.mygdx.game.holdable.Holdable;
import com.mygdx.game.holdable.Ingredient;

import java.text.DecimalFormat;

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

    // order stuff
    Texture breadTex = new Texture(Gdx.files.internal("Ingredients/breadSlice.png"));
    Texture hamTex = new Texture(Gdx.files.internal("Ingredients/ham.png"));
    Texture cheeseTex = new Texture(Gdx.files.internal("Ingredients/cheese.png"));
    Texture lettuceTex = new Texture(Gdx.files.internal("Ingredients/lettuce.png"));
    Texture tomatoTex = new Texture(Gdx.files.internal("Ingredients/tomato.png"));
    Texture orderTex;

    // delete later - for testing purposes
    exampleMusic proudLion;
    ShapeRenderer showHitboxRender;

    DayScreen(Diner game, DayState dayState)
    {
        this.game = game;
        this.dayState = dayState;

        appliances = dayState.apps;
        orderTex = new Texture(Gdx.files.internal("Orders/Sprite-Order_Blank.png"));

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
        showHitboxRender = new ShapeRenderer();



        /**
         * PLAYER & CONTROLS
         */
        player = new Player(new Texture(Gdx.files.internal("Misc/Sprite-Chef_WalkALL.png")), (int)(tileWidth), (int)(tileHeight));
        playerTwo = new Player(new Texture(Gdx.files.internal("Misc/Sprite-Chef_WalkALLP2.png")), (int)(tileWidth), (int)(tileHeight));
        pressE = false;
        pressShift = false;
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

        // save old position in case of collision
        // if collision, player position is set to (oldX, oldY)
        float oldX = player.getX();
        float oldY = player.getY();
        float oldX2 = playerTwo.getX();
        float oldY2 = playerTwo.getY();

        player.update(delta); // update player position & animation frame
        playerTwo.update(delta);

        // check if player is past screen border
        if(player.getX() < 0)
            player.setX(0);
        if(player.getX() > screenWidth - player.getWidth())
            player.setX(screenWidth - player.getWidth());
        if(player.getY() < 0)
            player.setY(0);
        if(player.getY() > screenHeight - player.getHeight())
            player.setY(screenHeight - player.getHeight());


        // appliance collision & interaction
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

            if(Intersector.overlaps(new Rectangle(appRect.getX()+appRect.getWidth()-1, appRect.getY(), 1, appRect.getHeight()), playerTwo.getBoundingRectangle())
                    || Intersector.overlaps(new Rectangle(appRect.getX(), appRect.getY(), 1, appRect.getHeight()), playerTwo.getBoundingRectangle()))
                playerTwo.setX(oldX2);
            if(Intersector.overlaps(new Rectangle(appRect.getX(), appRect.getY(), appRect.getWidth(), 1), playerTwo.getBoundingRectangle()) ||
                    Intersector.overlaps(new Rectangle(appRect.getX(), appRect.getY() + appRect.getHeight()-1, appRect.getWidth(), 1), playerTwo.getBoundingRectangle()))
                playerTwo.setY(oldY2);

            // interaction
            if(pressE && Intersector.overlaps(app.getInteractRegion(), player.getInteractRectangle()) && app.canInteract(player.getItem()))
            {
                player.interact(app.interact(player.getItem()));
            }

            if(pressShift && Intersector.overlaps(app.getInteractRegion(), playerTwo.getInteractRectangle()) && app.canInteract(playerTwo.getItem()))
            {
                playerTwo.interact(app.interact(playerTwo.getItem()));
            }
        }
        pressE = false;
        pressShift = false;

        // TEST STUFF (delete later)
        showHitboxRender.begin(ShapeRenderer.ShapeType.Filled);
        showHitboxRender.setColor(0.1f, 1f, 0.3f, 1f);
        showHitboxRender.rect(player.getBoundingRectangle().getX(), player.getBoundingRectangle().getY(), player.getBoundingRectangle().getWidth(), player.getBoundingRectangle().getHeight());
        showHitboxRender.end();

        // draw screen
        game.batch.begin();
        player.draw(game.batch); // player
        playerTwo.draw(game.batch);
        for(Appliance app:appliances) { // appliances
            app.draw(game.batch);
        }

        // draw order paper
        drawOrder(game.batch);

        // draw timer & # orders remaining
        game.font.getData().setScale(1.1f);
        game.font.draw(game.batch, "Orders left: ", tileWidth * 9.2f, tileHeight * 1.4f);
        game.font.getData().setScale(1.5f);
        game.font.draw(game.batch, String.format("%.02f", dayState.maxTime - dayState.currentTime), tileWidth * 9.4f, tileHeight * 0.7f);

        game.batch.end();
    }

    public void nextLevel()
    {
        dispose();
        game.setScreen(new TransitionScreen(game, dayState));
    }

    public void drawOrder(Batch batch) {
        float tempX = -30;
        float tempY = 670;
        float tempWidth = tileWidth * 1.5f;
        float tempHeight = tileHeight * 1.5f;

        batch.draw(orderTex, tempX+30, tempY-20, tempWidth/1.5f*2.5f, tileHeight*2.5f);
        Order order = dayState.orders.get(dayState.orderIndex);
        for(Holdable.Type ingredient : order.ingredients.keys()) {
            switch(ingredient) {
                case bread:
                    batch.draw(breadTex, tempX, tempY, tempWidth, tempHeight);
                    break;
                case ham:
                    batch.draw(hamTex, tempX, tempY, tempWidth, tempHeight);
                    break;
                case cheese:
                    batch.draw(cheeseTex, tempX, tempY, tempWidth, tempHeight);
                    break;
                case lettuce:
                    batch.draw(lettuceTex, tempX, tempY, tempWidth, tempHeight);
                    break;
                case tomato:
                    batch.draw(tomatoTex, tempX, tempY, tempWidth, tempHeight);
                    break;
            }
            tempX += tileWidth * 0.8f;
        }

    }

    @Override
    public void dispose() {
        player.dispose();
        playerTwo.dispose();
        for(Appliance app:appliances) {
            app.dispose();
        }
        orderTex.dispose();
        breadTex.dispose();
        hamTex.dispose();
        cheeseTex.dispose();
        lettuceTex.dispose();
        tomatoTex.dispose();

        // TEST STUFF (delete later)
        proudLion.dispose();
    }
    @Override
    public void resize(int width, int height) {}
    @Override
    public void show() {} // runs upon screen shown
    @Override
    public void hide() {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
}
