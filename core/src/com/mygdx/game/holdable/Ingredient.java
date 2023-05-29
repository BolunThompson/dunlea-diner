package com.mygdx.game.holdable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Ingredient {

    protected Texture texture;
    protected TextureRegion[] costumes;
    protected int costumeIndex;
    final int maxIndex;

    public enum Type {
        bread, ham, cheese, lettuce, tomato
    }

    public Ingredient(Texture texture, int numCostumes) {
        this.texture = texture;
        costumes = new TextureRegion[numCostumes];
        costumeIndex = 0;
        maxIndex = numCostumes;

        TextureRegion[][] tmpFrames = TextureRegion.split(texture, 32, 32);
        for(int i = 0; i < numCostumes; i++)
        {
            costumes[i] = tmpFrames[0][i];
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
