package com.mygdx.game.gameobject.controller;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gameobject.model.GolfBallModel;

public class GolfBallController {
    private final GolfBallModel model;

    public GolfBallController(GolfBallModel model) {
        this.model = model;
    }

    public void moveLeft() {
        this.model.setMovementVector(new Vector2(-1, 0));
    }

    public void moveRight() {
        this.model.setMovementVector(new Vector2(1, 0));
    }

    public void moveUp() {
        this.model.setMovementVector(new Vector2(0, 1));
    }

    public void moveDown() {
        this.model.setMovementVector(new Vector2(0, -1));
    }

    public void stop() {
        this.model.setMovementVector(new Vector2(0, 0));
    }

}