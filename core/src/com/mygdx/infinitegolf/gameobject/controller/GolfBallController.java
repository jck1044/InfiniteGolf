package com.mygdx.infinitegolf.gameobject.controller;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.infinitegolf.gameobject.model.GolfBallModel;
import com.mygdx.infinitegolf.gameobject.view.GolfBallView;

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