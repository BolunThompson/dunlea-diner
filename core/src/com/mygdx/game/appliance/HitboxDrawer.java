package com.mygdx.game.appliance;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Player;

public class HitboxDrawer {

    private ShapeRenderer sr;

    private Player player, playerTwo;
    private Array<Appliance> appliances;

    public HitboxDrawer(Camera camera) {
        sr = new ShapeRenderer();
        //sr.setProjectionMatrix(camera.combined);
    }

    public HitboxDrawer(Camera camera, Player player, Player playerTwo, Array<Appliance> appliances) {
        this(camera);
        this.player = player;
        this.playerTwo = playerTwo;
        this.appliances = appliances;
    }

    public void drawCollision() {
        sr.setColor(0.1f, 1f, 0.3f, 1f);
        sr.begin(ShapeRenderer.ShapeType.Line);

        sr.rect(player.getBoundingRectangle().x, player.getBoundingRectangle().y, player.getBoundingRectangle().width, player.getBoundingRectangle().height);
        sr.rect(playerTwo.getBoundingRectangle().x, playerTwo.getBoundingRectangle().y, playerTwo.getBoundingRectangle().width, playerTwo.getBoundingRectangle().height);

        for(Appliance app : appliances) {
            sr.rect(app.getCollisionRegion().x, app.getCollisionRegion().y, app.getCollisionRegion().width, app.getCollisionRegion().height);
        }

        sr.end();
    }

    public void drawInteraction() {
        sr.setColor(1.0f, 0, 1.0f, 1.0f);
        sr.begin(ShapeType.Line);

        sr.rect(player.getInteractRectangle().x, player.getInteractRectangle().y, player.getInteractRectangle().width, player.getInteractRectangle().height);
        sr.rect(playerTwo.getInteractRectangle().x, playerTwo.getInteractRectangle().y, playerTwo.getInteractRectangle().width, playerTwo.getInteractRectangle().height);

        sr.setColor(1.0f, 0, 0, 1.0f);
        for(Appliance app : appliances) {
            sr.rect(app.getInteractRegion().x, app.getInteractRegion().y, app.getInteractRegion().width, app.getInteractRegion().height);
            sr.rect(app.getInteractRegion2().x, app.getInteractRegion2().y, app.getInteractRegion2().width, app.getInteractRegion2().height);
        }

        sr.end();
    }
}
