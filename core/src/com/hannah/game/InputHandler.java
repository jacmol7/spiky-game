package com.hannah.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class InputHandler implements InputProcessor {
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;

    private boolean doAction;

    public Vector2 getMoveDirection() {
        int x = 0;
        int y = 0;

        if (moveUp && !moveDown) {
            y = 1;
        } else if (moveDown && !moveUp) {
            y = -1;
        }

        if (moveLeft && !moveRight) {
            x = -1;
        } else if (moveRight && !moveLeft) {
            x = 1;
        }

        return new Vector2(x, y);
    }


    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                moveUp = true;
                break;
            case Input.Keys.A:
                moveLeft = true;
                break;
            case Input.Keys.S:
                moveDown = true;
                break;
            case Input.Keys.D:
                moveRight = true;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                moveUp = false;
                break;
            case Input.Keys.A:
                moveLeft = false;
                break;
            case Input.Keys.S:
                moveDown = false;
                break;
            case Input.Keys.D:
                moveRight = false;
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return true;
    }
}
