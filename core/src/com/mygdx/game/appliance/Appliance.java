package com.mygdx.game.appliance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.holdable.Ingredient;

/**
 * Appliance class (abstract class)
 *
 * Created: May 24, 2023
 *
 * Basic application functions: draw, interact, and dispose
 */
public abstract class Appliance {

    protected Texture texture;
    protected Rectangle collisionRegion;
    protected Rectangle interactRegion;

    private TextureRegion[] frames;
    protected Animation animation;
    protected float elapsedTime;
    protected boolean doAnimation;

    protected Ingredient ingr;

    public enum direction {
        LEFT, RIGHT, UP, DOWN
    }

    /**
     * Creates an appliance with no interaction
     *
     * @param texture - SHOULD PROBABLY DELETE THIS PARAMETER
     * @param x - x-coordinate of collision box's bottom left corner (in pixels)
     * @param y - y-coordinate of collision box's bottom left corner (in pixels)
     * @param width - width of collision box (in pixels)
     * @param height - height of collision box (in pixels)
     */
    public Appliance(Texture texture, int x, int y, int width, int height)
    {
        this.texture = texture;
        collisionRegion = new Rectangle(x, y, width, height);
        interactRegion = new Rectangle(x , y, 0, 0);
        ingr = null;
        doAnimation = false;
    }

    /**
     * Creates an appliance with interaction box in 1 of 4 directions
     *
     * @param dir - sets interaction box to left, right, above, or below appliance
     */
    public Appliance(Texture texture, int x, int y, int width, int height, direction dir) // interaction
    {
        this(texture, x, y, width, height);

        switch(dir) { // from what direction the player interacts from
            case LEFT:
                interactRegion = new Rectangle(x - width/2f, y + height/4f, width/2f, height/2f);
                break;
            case RIGHT:
                interactRegion = new Rectangle(x + width, y + height/4f, width/2f, height/2f);
                break;
            case UP:
                interactRegion = new Rectangle(x + width/4f, y + height, width/2f, height/2f);
                break;
            case DOWN:
                interactRegion = new Rectangle(x + width/4f, y - height/2f, width/2f, height/2f);
                break;
            default: // invalid direction
                interactRegion = new Rectangle(0, 0, 0, 0);
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

        animation = new Animation(1f, (Object[])frames);
    }

    /**
     * Called when (1) Player is within appliance's interact region
     *             (2) canInteract is true
     *             (3) E key pressed
     *
     * @param ingr - Ingredient held by the player (null if nothing held)
     * @return Ingredient held by the appliance (null if nothing held)
     */
    public Ingredient interact(Ingredient ingr)
    {
        Ingredient temp = this.ingr;
        this.ingr = ingr;
        return temp;
    }

    /**
     * Returns whether the appliance can currently be interacted with by the player
     *
     * @param ingr - Ingredient held by the player (null if nothing held)
     */
    public boolean canInteract(Ingredient ingr)
    {
        return true;
    }

    /**
     * Allows animation to start/progress/stop playing
     */
    public void update(float delta)
    {
        if(doAnimation)
            elapsedTime += Gdx.graphics.getDeltaTime();
        if(animation != null && animation.isAnimationFinished(elapsedTime) && ingr == null)
        {
            doAnimation = false;
            elapsedTime = 0;
        }
    }

    public void draw(Batch batch)
    {
        if(doAnimation) {
            batch.draw((TextureRegion) animation.getKeyFrame(elapsedTime, false), collisionRegion.x, collisionRegion.y, collisionRegion.width, collisionRegion.height);
        } //else
            //batch.draw(texture, collisionRegion.x, collisionRegion.y, collisionRegion.width, collisionRegion.height);

        //if(ingr != null) // visual of ingredient on appliance for testing purposes
            //batch.draw(ingr.getTexture(), collisionRegion.x, collisionRegion.y, collisionRegion.width, collisionRegion.height);
    }

    public void dispose() {
        texture.dispose();
        if(ingr != null)
            ingr.dispose();
    }

    /**
     * Get methods
     */
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
