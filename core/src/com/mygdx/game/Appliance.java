package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Appliance class (abstract class)
 *
 * Created: May 24, 2023
 * Last Updated: May 25, 2023
 */
public abstract class Appliance {

    Texture texture;
    Rectangle collisionRegion;
    Rectangle interactRegion;

    private TextureRegion[] frames;
    private Animation animation;
    private float elapsedTime;

    protected boolean doAnimation;

    public enum direction {
        LEFT, RIGHT, UP, DOWN
    }

    public Appliance(Texture texture, int x, int y, int width, int height) // no interaction
    {
        this.texture = texture;
        doAnimation = false;
    }

    public Appliance(Texture texture, int x, int y, int width, int height, direction dir) // interaction
    {
        this(texture, x, y, width, height);
        collisionRegion = new Rectangle(x, y, width, height);

        switch(dir) { // from what direction the player interacts from
            case LEFT:
                interactRegion = new Rectangle(x - width/2f, y, width/2f, height);
                break;
            case RIGHT:
                interactRegion = new Rectangle(x + width, y, width/2f, height);
                break;
            case UP:
                interactRegion = new Rectangle(x, y + height, width, height/2f);
                break;
            case DOWN:
                interactRegion = new Rectangle(x, y - height/2f, width, height/2f);
                break;
            default:
                interactRegion = new Rectangle(x - width/2f, y - height/2f, width*2f, height*2f);
                break;
        }
    }

    public Appliance(Texture defaultTexture, Texture sprite_sheet, int x, int y, int width, int height, direction dir) // has animation
    {
        this(defaultTexture, x, y, width, height, dir);

        TextureRegion[][] tmpFrames = TextureRegion.split(sprite_sheet, width, height);
        frames = new TextureRegion[4];
        animation = new Animation(1f, (Object[])frames);
    }

    public void interact()
    {

    }

    public void draw(Batch batch)
    {
        if(doAnimation)
            batch.draw((TextureRegion)animation.getKeyFrame(elapsedTime, false), collisionRegion.x, collisionRegion.y, collisionRegion.width, collisionRegion.height);
        else
            batch.draw(texture, collisionRegion.x, collisionRegion.y, collisionRegion.width, collisionRegion.height);
    }

    public void dispose()
    {
        texture.dispose();
    }

    public Rectangle getCollisionRegion()
    {
        return collisionRegion;
    }

    public Rectangle getInteractRegion()
    {
        return interactRegion;
    }
}
