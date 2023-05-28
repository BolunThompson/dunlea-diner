package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Ingredient;

/**
 * Appliance class (abstract class)
 *
 * Created: May 24, 2023
 *
 * Basic application functions: draw, interact, and dispose
 */
public abstract class Appliance {

    Texture texture;
    Rectangle collisionRegion;
    Rectangle interactRegion;

    private TextureRegion[] frames;
    private Animation animation;
    private float elapsedTime;

    protected boolean doAnimation;

    Ingredient ingr;

    public enum direction {
        LEFT, RIGHT, UP, DOWN
    }

    /**
     * Creates an appliance with no interaction
     */
    public Appliance(Texture texture, int x, int y, int width, int height)
    {
        this.texture = texture;
        collisionRegion = new Rectangle(x, y, width, height);
        interactRegion = new Rectangle(x , y, 0, 0);
        ingr = null;
    }

    /**
     * Creates an appliance with interaction in 1 of 4 directions
     *
     * @param dir - sets interaction box to left, right, above, or below appliance
     */
    public Appliance(Texture texture, int x, int y, int width, int height, direction dir) // interaction
    {
        this(texture, x, y, width, height);

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

    /**
     * Creates an appliance with animation and interaction
     *
     * @param defaultTexture - default texture when not animated
     * @param sprite_sheet - frames for animation
     */
    public Appliance(Texture defaultTexture, Texture sprite_sheet, int x, int y, int width, int height, direction dir) // has animation
    {
        this(defaultTexture, x, y, width, height, dir);

        TextureRegion[][] tmpFrames = TextureRegion.split(sprite_sheet, 32, 32);
        frames = new TextureRegion[5];
        for(int i = 0; i < 5; i ++)
        {
            frames[i] = tmpFrames[0][i];
        }

        animation = new Animation(1/3f, (Object[])frames);
    }

    public void interact(Ingredient ingr)
    {
        this.ingr = ingr;
    }

    public void update(float delta) // allows animation to progress
    {
        if(doAnimation)
            elapsedTime += Gdx.graphics.getDeltaTime();
        else
            elapsedTime = 0;
    }

    public void draw(Batch batch)
    {
        if(doAnimation) {
            batch.draw((TextureRegion) animation.getKeyFrame(elapsedTime, false), collisionRegion.x, collisionRegion.y, collisionRegion.width, collisionRegion.height);
        } else
            batch.draw(texture, collisionRegion.x, collisionRegion.y, collisionRegion.width, collisionRegion.height);

        if(ingr != null)
            batch.draw(ingr.getTexture(), collisionRegion.x, collisionRegion.y, collisionRegion.width, collisionRegion.height);
    }

    public void dispose() {
        texture.dispose();
        if(ingr != null)
            ingr.dispose();
    }

    public Rectangle getCollisionRegion() {
        return collisionRegion;
    }

    public Rectangle getInteractRegion() {
        return interactRegion;
    }

    public Ingredient getIngredient() {
        return ingr;
    }
}
