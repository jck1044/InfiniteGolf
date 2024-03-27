package com.mygdx.infinitegolf.gameobject.model;

import com.badlogic.gdx.math.Vector2;

public abstract class DynamicGameObjectModel extends GameObjectModel {

    protected Vector2 movementDirection; // this will be replaced by physics forces later

    protected float speed;

    public DynamicGameObjectModel(Vector2 position, Vector2 movementDirection, float speed) {
        super(position);
        this.movementDirection = movementDirection;
        this.speed = speed;
    }

    public DynamicGameObjectModel(Vector2 position) {
        super(position);
        this.movementDirection = new Vector2();
        this.speed = 0F;
    }

    public Vector2 getMovementDirection() {
        return movementDirection;
    }

    public void setMovementDirection(Vector2 movementVector) {
        this.movementDirection = movementVector;
    }

    public void updatePosition(float dt) {
        Vector2 posChange = this.movementDirection.cpy().scl(speed * dt);
        this.position.add(posChange);
    }

}
