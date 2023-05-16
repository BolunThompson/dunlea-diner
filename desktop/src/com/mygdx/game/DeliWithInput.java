package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class DeliWithInput extends ApplicationAdapter {

    boolean movingLeft, movingRight, movingUp, movingDown;

    public void create() {
        Gdx.input.setInputProcessor(new InputAdapter() {

            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Keys.LEFT:
                        movingLeft = true;
                        break;
                    case Keys.RIGHT:
                        movingRight = true;
                        break;
                    case Keys.UP:
                        movingUp = true;
                        break;
                    case Keys.DOWN:
                        movingDown = true;
                        break;
                    case Keys.E: // interact
                        break;
                }
                return true;
            }

            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Keys.LEFT:
                        movingLeft = false;
                        break;
                    case Keys.RIGHT:
                        movingRight = false;
                        break;
                    case Keys.UP:
                        movingUp = false;
                        break;
                    case Keys.DOWN:
                        movingDown = false;
                        break;
                }
                return true;
            }

        });
    }

}
