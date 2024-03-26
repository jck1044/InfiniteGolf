package com.mygdx.game.gameobject.model;

import com.badlogic.gdx.math.Vector2;

public abstract class GameObjectModel {
    protected Vector2 position; // vector for x, y location of the ship in the game world

    public GameObjectModel(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }
}