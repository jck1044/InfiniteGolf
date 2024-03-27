package com.mygdx.infinitegolf.gameobject.model;

import com.badlogic.gdx.math.Vector2;

public class GolfBallModel extends DynamicGameObjectModel {
    private Vector2 position; // vector for x, y location of the ship in the game world
    private float speed = 200F;
    private short layer = 0;

    private Vector2 movementVector = new Vector2(0, 0);


    public GolfBallModel(Vector2 position) {
        super(position);
    }

    public Vector2 getPosition() {
        return position;
    }

    public short getLayer() {
        return layer;
    }

    public Vector2 getMovementVector() {
        return movementVector;
    }

    public void setMovementVector(Vector2 movementVector) {
        this.movementVector = movementVector;
    }

}
