package com.mygdx.infinitegolf.gameobject.controller;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.infinitegolf.gameobject.model.PowerBallModel;
import com.mygdx.infinitegolf.gameobject.view.PowerBallView;

public class PowerBallController {
    private final PowerBallModel model;
    private final PowerBallView view;

    public PowerBallController(PowerBallModel model, PowerBallView view) {
        this.model = model;
        this.view = view;
    }

    public boolean powerUp(boolean power) {
        return this.view.powerUp(power);
    }

    public float getSize() {
        return this.view.getSize();
    }

    public void setSize(float newSize) {
        this.view.setSize(newSize);
    }

    public void updatePosition(Body golfBallBody) {
        this.view.updatePosition(golfBallBody);
    }
}
