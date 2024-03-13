package com.mygdx.game.gameobject.model;

import com.badlogic.gdx.math.Vector2;

public class BallModel extends GameObjectModel{
    private Vector2 position; // vector for x, y location of the ship in the game world
    private float speed = 200F;
    private final float radius;

    private short layer = 0;
    private Vector2 movementVector = new Vector2(0, 0);

    public BallModel(Vector2 position, float radius, short layer) {
        super(position);
        this.position = position;
        this.radius = radius;
        this.layer = layer;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void updatePosition(float dt) {
        Vector2 posChange = this.movementVector.cpy().scl(speed * dt);
        this.position.add(posChange);
    }

    public short getLayer() {
        return layer;
    }


    public float getRadius() {
        return radius;
    }

    public Vector2 getMovementVector() {
        return movementVector;
    }

    public void setMovementVector(Vector2 movementVector) {
        this.movementVector = movementVector;
    }

}
