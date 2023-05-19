package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Player class
 *
 * Created: May 19, 2023
 * Last Updated:
 *
 * NOTES:
 * This one post online said that the Player class shouldn't be a sprite because the player uses Animations.
 *      https://stackoverflow.com/questions/20063281/libgdx-collision-detection-with-tiledmap
 *
 * Also I have code for sliding movement, here is just non sliding movement, so if needed I can change to that
 */
public class Player extends Sprite {

    final int speed = 100;
    public boolean moveLeft, moveRight, moveUp, moveDown;

    public Player(Texture texture, int width, int height)
    {
        super(texture, width, height);
        setPosition(0,0); // currently sets initial pos to bottom left corner; change later to set to middle of screen?
    }

    public void update(float delta)
    {
        if(moveLeft)
            setX(getX() + speed * delta);
        if(moveRight)
            setX(getX() - speed * delta);
        if(moveUp)
            setY(getY() + speed * delta);
        if(moveDown)
            setY(getY() - speed * delta);
    }
}
