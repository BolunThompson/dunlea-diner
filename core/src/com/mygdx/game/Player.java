package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.holdable.Holdable;

/**
 * Player class
 *
 * Created: May 19, 2023
 *
 * NOTES:
 * Sprite animations code from here:
 *      https://www.youtube.com/watch?v=SVyYvi0I6Bc
 */
public class Player {

    final private int startX = 100 * 8;
    final private int startY = 100 * 3;
    final private int speed = 350;

    private float x, y, width, height;
    public boolean moveLeft, moveRight, moveUp, moveDown;

    private Rectangle rectangle; // collision hitbox
    private Rectangle interactRect; // the area checked to determine if player is close enough to interact with DayScreen.java

    private Texture texture;
    private TextureRegion[] idleFrames;
    private TextureRegion[] leftWalkFrames;
    private TextureRegion[] rightWalkFrames;
    private TextureRegion[] upWalkFrames;
    private TextureRegion[] downWalkFrames;
    private Animation animation, idleAnimation, leftWalkAnimation, rightWalkAnimation, upWalkAnimation, downWalkAnimation;
    private float elapsedTime;

    private Holdable item;

    public Player(Texture texture, int width, int height)
    {
        // position & size
        this.width = width;
        this.height = height;
        rectangle = new Rectangle(startX + width*0.2f,startY, width*0.6f, height*0.9f);
        interactRect = new Rectangle(startX + width/4f, startY + height/4f, width/2f, height/2f);
        setPosition(startX,startY);

        // sprite texture & animation
        this.texture = texture;
        TextureRegion[][] tmpFrames = TextureRegion.split(texture, 32, 32);

        idleFrames = new TextureRegion[2];
        leftWalkFrames = new TextureRegion[2];
        rightWalkFrames = new TextureRegion[2];
        upWalkFrames = new TextureRegion[2];
        downWalkFrames = new TextureRegion[2];

        idleFrames[0] = tmpFrames[0][0]; // ik... this section looks awful lol
        idleFrames[1] = tmpFrames[0][1];
        leftWalkFrames[0] = tmpFrames[0][2];
        leftWalkFrames[1] = tmpFrames[0][3];
        rightWalkFrames[0] = tmpFrames[0][4];
        rightWalkFrames[1] = tmpFrames[0][5];
        upWalkFrames[0] = tmpFrames[0][6];
        upWalkFrames[1] = tmpFrames[0][7];
        downWalkFrames[0] = tmpFrames[0][8];
        downWalkFrames[1] = tmpFrames[0][9];

        idleAnimation = new Animation(1/3f, (Object[])idleFrames); // cast to (Object[]) because console said to
        leftWalkAnimation = new Animation(1/5f, (Object[])leftWalkFrames);
        rightWalkAnimation = new Animation(1/5f, (Object[])rightWalkFrames);
        upWalkAnimation = new Animation(1/5f, (Object[])upWalkFrames);
        downWalkAnimation = new Animation(1/5f, (Object[])downWalkFrames);
        animation = idleAnimation;

        item = null;
    }

    /**
     * Update player position & appearance based on input movement
     */
    public void update(float delta)
    {
        elapsedTime += Gdx.graphics.getDeltaTime();

        if(moveLeft) {
            setX(getX() - speed * delta);
            animation = leftWalkAnimation;
        }
        if(moveRight) {
            setX(getX() + speed * delta);
            animation = rightWalkAnimation;
        }
        if(moveUp) {
            setY(getY() + speed * delta);
            animation = upWalkAnimation;
        }
        if(moveDown) {
            setY(getY() - speed * delta);
            animation = downWalkAnimation;
        }

        // idle
        if(!moveLeft && !moveRight && !moveUp && !moveDown)
        {
            animation = idleAnimation;
        }
    }

    /**
     * Interact with appliancess
     *
     * @param item - Item held by the appliance (null if nothing held)
     * @return Item held by the player (null if nothing held)
     */
    public Holdable interact(Holdable item)
    {
        Holdable temp = this.item;
        this.item = item;
        return temp;
    }

    /**
     * Draw the player & held item on screen
     */
    public void draw(Batch batch)
    {
        batch.draw((TextureRegion)animation.getKeyFrame(elapsedTime, true), x, y, width, height);
        if(item != null) {
            item.draw(batch, x, y + width*0.4f, width, height);
        }
    }

    public void dispose()
    {
        texture.dispose();
        if(item != null)
            item.dispose();
    }

    /**
     * Get values
     */
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }
    public Rectangle getBoundingRectangle() {
        return rectangle;
    }
    public Rectangle getInteractRectangle() {
        return interactRect;
    }
    public Holdable getItem() {
        return item;
    }

    /**
     * Set values
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;

        rectangle.setPosition(x+0.2f*width, y);
        interactRect.setPosition(x + width/4f, y + height/4f);
    }
    public void setX(float x) {
        this.x = x;
        rectangle.setX(x+0.2f*width);
        interactRect.setX(x + width/4f);
    }
    public void setY(float y) {
        this.y = y;
        rectangle.setY(y);
        interactRect.setY(y + height/4f);
    }
}
