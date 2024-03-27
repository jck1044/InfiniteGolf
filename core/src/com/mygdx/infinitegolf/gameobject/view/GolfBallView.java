package com.mygdx.infinitegolf.gameobject.view;

import static Utils.Constants.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.infinitegolf.gameobject.model.GolfBallModel;
import com.mygdx.infinitegolf.utils.Assets;

public class GolfBallView extends GameObjectView {
    private final GolfBallModel model;
    private final SpriteBatch batch;
    protected Animation currentAnimation;

    private Texture texture;
    private float stateTime;
    private int rotationAngle;
    final int rotationDegrees = 50;
    private Sprite ballSprite;
    private float ballAngle;


    public GolfBallView(GolfBallModel model, SpriteBatch batch) {
        super(batch);
        this.ballAngle = 0;
        this.model = model;
        this.batch = batch;
        this.texture = Assets.getTexture("Images/GolfBall.png");
        this.ballSprite = new Sprite(this.texture);
    }

    public void updatePosition(Body golfBallBody) {
        ballAngle = golfBallBody.getAngle() * (180 / (float) Math.PI);
        ballSprite.rotate(ballAngle);
        ballSprite.setX(golfBallBody.getPosition().x * PPM - ((float) texture.getWidth() / 2));
        ballSprite.setY(golfBallBody.getPosition().y * PPM - ((float) texture.getHeight() / 2));
    }

    public void render(float dt) {

    }

    public short getLayer() {
        return this.model.getLayer();
    }

    public Texture getTexture() {
        return texture;
    }

    public Sprite getBallSprite() {
        return ballSprite;
    }


}
