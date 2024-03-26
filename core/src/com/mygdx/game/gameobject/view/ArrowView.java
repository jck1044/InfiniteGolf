package com.mygdx.game.gameobject.view;

import static Utils.Constants.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.gameobject.model.ArrowModel;
import com.mygdx.game.utils.Assets;

public class ArrowView extends GameObjectView {

    private ArrowModel model;
    private Texture texture;
    private Sprite arrowSprite;
    private final float arrowSize = 32;
    private final float golfBallSize = 16;
    private float arrowAngle;

    public ArrowView(ArrowModel model, SpriteBatch batch) {
        super(batch);
        this.arrowAngle = 0;
        this.model = model;
        this.batch = batch;
        this.texture = Assets.getTexture("Images/Arrow.png");
        this.arrowSprite =  new Sprite(this.texture);
    }

    public void updatePosition(Body golfBallBody) {
        arrowSprite.setX(golfBallBody.getPosition().x * PPM);
        arrowSprite.setY(golfBallBody.getPosition().y * PPM - ((16 * (arrowSize / golfBallSize)) / 2)); //16 hardcoded as height of normal golf ball
        arrowSprite.setSize(arrowSize, arrowSize);
        arrowSprite.setOrigin(0, golfBallSize);
        arrowSprite.setRotation(arrowAngle);
        arrowSprite.draw(batch);
    }

    public void increaseAngle() {
        arrowAngle+=2;
    }

    public void decreaseAngle() {
        arrowAngle-=2;
    }

    public float getAngle() {
        return arrowAngle;
    }

    public Sprite getArrowSprite() {
        return arrowSprite;
    }

    @Override
    public void render(float dt) {

    }
}
