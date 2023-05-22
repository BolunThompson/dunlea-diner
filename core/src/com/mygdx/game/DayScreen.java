package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

/**
 * Day1Screen class
 *
 * Created: May 19, 2023
 * Last Updated: May 22, 2023
 */
public class DayScreen implements Screen {
    final Diner game;

    // screen dimensions in pixels (1200 x 900)
    final int screenWidth = 100 * 12;
    final int screenHeight = 100 * 9;

    // screen dimensions in tiles (12 x 9)
    final int numWidthTiles = 12;       final int numHeightTiles = 9;

    // tile size in pixels
    final int tileWidth = screenWidth / numWidthTiles;
    final int tileHeight = screenHeight / numHeightTiles;

    OrthographicCamera camera;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    TiledMapTileLayer obstacleLayer;
    MapObjects obstacles;

    Texture playerImage;
    Player player;

    public DayScreen(Diner game, String tileMapFile)
    {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
        camera.update();

        tiledMap = new TmxMapLoader().load(tileMapFile);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, screenWidth/numWidthTiles/32f); // unit scale is probably wrong

        obstacleLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
        obstacles = obstacleLayer.getObjects();
        obstacles.add(new RectangleMapObject(tileWidth * 3,tileHeight * 3, tileWidth,tileHeight * 4));

        playerImage = new Texture(Gdx.files.internal("Misc/Sprite-Chef_Idle1.png"));
        player = new Player(playerImage, (int)(tileWidth/1.5), (int)(tileHeight/1.5f));

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

        float oldX = player.getX();
        float oldY = player.getY();

        player.update(delta);

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
            if(Intersector.overlaps(new Rectangle(temp.getX()+temp.getWidth(),temp.getY(),1,temp.getHeight()), player.getBoundingRectangle()) ||
                    Intersector.overlaps(new Rectangle(temp.getX(),temp.getY(),1,temp.getHeight()), player.getBoundingRectangle()))
            {
                player.setX(oldX);
            }
            if(Intersector.overlaps(new Rectangle(temp.getX(),temp.getY(),temp.getWidth(),1), player.getBoundingRectangle()) ||
                    Intersector.overlaps(new Rectangle(temp.getX(),temp.getY() + temp.getHeight(),temp.getWidth(),1), player.getBoundingRectangle()))
            {
                player.setY(oldY);
            }
        }

        // draw player
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
