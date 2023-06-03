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
    private boolean edible = true; // whether it has been toasted, sliced, etc
    final int maxIndex;

    public Ingredient(Texture texture, int numCostumes) {
        this.texture = texture;
        costumes = new TextureRegion[numCostumes];
        costumeIndex = 0;
        maxIndex = numCostumes - 1;

        TextureRegion[][] tmpFrames = TextureRegion.split(texture, 32, 32);
        for (int i = 0; i < numCostumes; i++) {
            costumes[i] = tmpFrames[0][i];
        }
    }

    protected Ingredient(Texture texture, int numCostumes, boolean edible) {
        this(texture, numCostumes);
        this.edible = edible;
    }

    /**
     * This doesn't make a copy of the ingredient passed into this method, it just creates a new object of the same ingredient type.
     * This method is used ONLY for creating new ingredients to be dispensed from crates.
     */
    public Ingredient copy(Ingredient orig) {
        switch (orig.getType()) {
            case bread:
                return new Bread();
            case wheatBread:
                return new WheatBread();
            case sourBread:
                return new SourBread();
            case minionBread:
                return new MinionBread();

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

        if (costumeIndex > maxIndex)
            costumeIndex = maxIndex;

        // assuming that switch in texture means that it has been processed
        edible = true;

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

    public TextureRegion getIcon() {
        return costumes[0];
    }
    
    public int getCostumeIndex() {
        return costumeIndex;
    }

    public boolean edible() {
        return edible;
    }
    

    public Type getType() {
        return null;
    }

    public String toString() {
        return "ingredient";
    }

    public boolean equals(Object o) {
        if (o instanceof Ingredient) {
            Ingredient i = (Ingredient) o;
            return this.getType() == i.getType();
        }
        return false;
    }

    public int hashCode() {
        if (getType() == null)
            return 0;
        return getType().hashCode();
    }

}
