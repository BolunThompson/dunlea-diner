package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.holdable.Ingredient;

/**
 * Player class
 *
 * Created: May 19, 2023
 *
 * NOTES:
 *
 * Sprite Animations code from here:
 *      https://www.youtube.com/watch?v=SVyYvi0I6Bc
 */
public class Player {

    final private int startX = 100 * 8;
    final private int startY = 100 * 3;
    final private int speed = 300;

    private float x, y, width, height;
    public boolean moveLeft, moveRight, moveUp, moveDown;

    private Rectangle rectangle; // collision hitbox
    private Rectangle interactRect; // the area checked for overlap with appliance interactRegion

    private Texture texture;
    private TextureRegion[] idleFrames;
    private TextureRegion[] leftWalkFrames;
    private TextureRegion[] rightWalkFrames;
    private TextureRegion[] upWalkFrames;
    private TextureRegion[] downWalkFrames;
    private Animation animation, idleAnimation, leftWalkAnimation, rightWalkAnimation, upWalkAnimation, downWalkAnimation;
    private float elapsedTime;

    private Ingredient ingr;

    public Player(int width, int height)
    {
        // position & size
        this.width = width;
        this.height = height;
        rectangle = new Rectangle(startX,startY, width, height);
        interactRect = new Rectangle(startX + width/4f, startY + height/4f, width/2, height/2);
        setPosition(startX,startY); // currently sets initial pos to bottom left corner; change later to set to middle of screen?

        // sprite texture & animation
        this.texture = new Texture(Gdx.files.internal("Misc/Sprite-Chef_WalkALL.png"));
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

        /**int index = 0;
        for(int i = 0; i < 1; i++) { // rows
            for(int j = 0; j < 10; j++) { // cols
                animationFrames[index++] = tmpFrames[i][j];
            }
        }**/

        idleAnimation = new Animation(1/3f, (Object[])idleFrames); // cast to (Object[]) because console said to
        leftWalkAnimation = new Animation(1/5f, (Object[])leftWalkFrames);
        rightWalkAnimation = new Animation(1/5f, (Object[])rightWalkFrames);
        upWalkAnimation = new Animation(1/5f, (Object[])upWalkFrames);
        downWalkAnimation = new Animation(1/5f, (Object[])downWalkFrames);
        animation = idleAnimation;

        ingr = null;
    }

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

    // pick up or put down an item
    public Ingredient interact(Ingredient ingr)
    {
        Ingredient temp = this.ingr;
        this.ingr = ingr;
        return temp;
    }

    public void draw(Batch batch)
    {
        batch.draw((TextureRegion)animation.getKeyFrame(elapsedTime, true), x, y, width, height);
        if(ingr != null)
            batch.draw(ingr.getTexture(), x, y, width, height);
    }

    public void dispose()
    {
        texture.dispose();
        if(ingr != null)
            ingr.dispose();
    }

    /**
     * Return values
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
    public Ingredient getIngredient() {
        return ingr;
    }

    /**
     * Set values
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;

        rectangle.setPosition(x, y);
        interactRect.setPosition(x + width/4f, y + height/4f);
    }
    public void setX(float x) {
        this.x = x;
        rectangle.setX(x);
        interactRect.setX(x + width/4f);
    }
    public void setY(float y) {
        this.y = y;
        rectangle.setY(y);
        interactRect.setY(y + height/4f);
    }
}
