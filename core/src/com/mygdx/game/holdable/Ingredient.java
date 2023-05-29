package com.mygdx.game.holdable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Ingredient {

    protected Texture texture;
    protected TextureRegion[] costumes;
    protected int costumeIndex;
    final int maxIndex;

    Ingredient shallowCopy;

    public enum Type {
        bread, ham, cheese, lettuce, tomato
    }

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
     * This doesn't actually copy the ingredient passed into this method, it just creates a new one of the same type.
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

    public void nextCostume() {
        costumeIndex++;

        if(costumeIndex > maxIndex)
            costumeIndex = maxIndex;
    }

    public TextureRegion getTexture() {
        return costumes[costumeIndex];
    }

    public Type getType() {
        return null;
    }

    public String toString() {
        return "ingredient";
    }

    public void dispose() {
        texture.dispose();
    }
}
