package com.mygdx.game.appliance;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.holdable.Holdable;

public class Barrier extends Appliance {

    public Barrier(int x, int y) {
        super(null, x, y);
    }

    public Barrier(int x, int y, int width, int height) {
        super(null, x, y);

        this.collisionRegion = new Rectangle(x * this.width, y * this.height, width * this.width, height * this.height);
    }

    @Override
    public Holdable interact(Holdable item) {
        return item;
    }

    @Override
    public boolean canInteract(Holdable item) {
        return false;
    }

    @Override
    public void update(float delta) {}

    @Override
    public void draw(Batch batch) {}

    @Override
    public void dispose() {}


}
