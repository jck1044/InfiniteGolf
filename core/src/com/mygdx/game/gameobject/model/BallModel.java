package com.mygdx.game.gameobject.model;

import com.badlogic.gdx.math.Vector2;

public class BallModel {
    private Vector2 position; // vector for x, y location of the ship in the game world
    private float speed = 0F;
    private Vector2 movementVector = new Vector2(0, 0);

    public BallModel(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void updatePosition(float dt) {
        Vector2 posChange = this.movementVector.cpy().scl(speed * dt);
        this.position.add(posChange);
    }

    public Vector2 getMovementVector() {
        return movementVector;
    }

    public void setMovementVector(Vector2 movementVector) {
        this.movementVector = movementVector;
    }

}
