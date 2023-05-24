package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * DayScreen class
 *
 * Created: May 19, 2023
 * Last Updated: May 23, 2023
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


    OrthographicCamera camera;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    TiledMapTileLayer obstacleLayer;
    MapObjects obstacles;

    TiledMapTileLayer interactLayer;
    MapObjects interactRegions;

    Player player;

    DayState dayState;

    DayScreen(Diner game, DayState dayState)
    {
        this.game = game;
        this.dayState = dayState;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, numWidthTiles, numHeightTiles);
        camera.update();

        tiledMap = new TmxMapLoader().load(dayState.mapFile);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/32f); // unit scale is probably wrong

        obstacleLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
        obstacles = obstacleLayer.getObjects();
        obstacles.add(new RectangleMapObject(tileWidth * 3,tileHeight * 3, tileWidth,tileHeight * 4)); // left counter
        obstacles.add(new RectangleMapObject(tileWidth * 5, tileHeight * 2, tileWidth * 7, tileHeight)); // bottom counter
        obstacles.add(new RectangleMapObject(tileWidth * 6, tileHeight * 5, tileWidth * 5, tileHeight)); // food boxes
        obstacles.add(new RectangleMapObject(tileWidth * 6, tileHeight * 8, tileWidth * 6, tileHeight)); // ovens & order-in

        interactLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
        interactRegions = interactLayer.getObjects();
        // add rectangles here ^^^

        player = new Player((int)(tileWidth - 2), (int)(tileHeight - 2));

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

        // collision
        for(MapObject object:obstacles)
        {
            Rectangle temp = ((RectangleMapObject)object).getRectangle();
            if(Intersector.overlaps(new Rectangle(temp.getX()+temp.getWidth()-1,temp.getY(),1,temp.getHeight()), player.getBoundingRectangle()) ||
                    Intersector.overlaps(new Rectangle(temp.getX(),temp.getY(),1,temp.getHeight()), player.getBoundingRectangle()))
            {
                player.setX(oldX);
            }
            if(Intersector.overlaps(new Rectangle(temp.getX(),temp.getY(),temp.getWidth(),1), player.getBoundingRectangle()) ||
                    Intersector.overlaps(new Rectangle(temp.getX(),temp.getY() + temp.getHeight()-1,temp.getWidth(),1), player.getBoundingRectangle()))
            {
                player.setY(oldY);
            }
        }

        // interaction
        for(MapObject interactRegion:interactRegions)
        {
            if(Intersector.overlaps(((RectangleMapObject)interactRegion).getRectangle(), player.getBoundingRectangle()))
            {
                // something
            }
        }

        // draw player
        game.batch.begin();
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
    }
    public void resize(int width, int height) {}
    public void show() {} // runs upon screen shown
    public void hide() {}
    public void pause() {}
    public void resume() {}
}
