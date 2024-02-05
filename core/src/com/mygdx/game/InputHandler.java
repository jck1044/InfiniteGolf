package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.gameobject.controller.BallController;

public class InputHandler implements InputProcessor {

    private final BallController controller;

    public InputHandler(BallController controller) {
        this.controller = controller;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            this.controller.moveLeft();
            return true;
        }

        if (keycode == Input.Keys.RIGHT) {
            this.controller.moveRight();
            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT) {
            this.controller.stop();
            return true;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
