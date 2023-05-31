package com.mygdx.game.holdable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Ingredient class (abstract class)
 *
 * Created: May 28, 2023
 */
public abstract class Ingredient extends Holdable {

    protected Texture texture;
    protected TextureRegion[] costumes;
    protected int costumeIndex;
    final int maxIndex;

    public Ingredient(Texture texture, int numCostumes) {
        this.texture = texture;
        costumes = new TextureRegion[numCostumes];
        costumeIndex = 0;
        maxIndex = numCostumes - 1;

        TextureRegion[][] tmpFrames = TextureRegion.split(texture, 32, 32);
        for(int i = 0; i < numCostumes; i++)
        {
            costumes[i] = tmpFrames[0][i];
        }
    }

    /**
     * This doesn't actually copy the ingredient passed into this method, it just creates a new one of the same ingredient type.
     * This method is used only for creating new ingredients to be dispensed from crates.
     */
    public Ingredient copy(Ingredient orig) {
        switch(orig.getType())
        {
            case bread:
                return new Bread();
            case ham:
                return new Ham();
            case cheese:
                return new Cheese();
            case lettuce:
                return new Lettuce();
            case tomato:
                return new Tomato();
            default:
                return null;
        }
    }

    /**
     * Change texture to next in sprite sheet
     */
    public void nextCostume() {
        costumeIndex++;

        if(costumeIndex > maxIndex)
            costumeIndex = maxIndex;
    }

    public void draw(Batch batch, float x, float y, float width, float height) {
        batch.draw(costumes[costumeIndex], x, y, width, height);
    }

    public void dispose() {
        texture.dispose();
    }

    /**
     * Get methods
     */
    public TextureRegion getTexture() {
        return costumes[costumeIndex];
    }
    public Type getType() {
        return null;
    }
    public String toString() {
        return "ingredient";
    }

}
