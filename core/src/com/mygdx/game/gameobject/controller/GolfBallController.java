package com.mygdx.game.gameobject.controller;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.gameobject.model.GolfBallModel;
import com.mygdx.game.gameobject.view.GolfBallView;

public class GolfBallController {
    private final GolfBallModel model;
    private final GolfBallView view;

    public GolfBallController(GolfBallModel model, GolfBallView view) {
        this.model = model;
        this.view = view;
    }

    public void updatePosition(Body golfBallBody) {
        this.view.updatePosition(golfBallBody);
    }

    public Sprite getBallSprite() {
        return this.view.getBallSprite();
    }

}