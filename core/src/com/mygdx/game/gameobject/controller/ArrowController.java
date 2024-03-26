package com.mygdx.game.gameobject.controller;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.gameobject.model.ArrowModel;
import com.mygdx.game.gameobject.view.ArrowView;

public class ArrowController {
    private ArrowModel model;
    private ArrowView view;
    public ArrowController(ArrowModel model, ArrowView view) {
        this.model = model;
        this.view = view;
    }

    public void updatePosition(Body golfBallBody) {
        this.view.updatePosition(golfBallBody);
    }

    public void increaseAngle() {
        this.view.increaseAngle();
    }

    public void decreaseAngle() {
        this.view.decreaseAngle();
    }

    public float getAngle() {
        return this.view.getAngle();
    }

    public Sprite getArrowSprite() {
        return this.view.getArrowSprite();
    }
}
