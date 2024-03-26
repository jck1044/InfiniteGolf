package com.mygdx.game.gameobject.view;

import static Utils.Constants.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.gameobject.model.PowerBallModel;
import com.mygdx.game.utils.Assets;

public class PowerBallView extends GameObjectView {
    private final PowerBallModel model;
    private final SpriteBatch batch;
    private Texture ballTexture;
    private Sprite powerSprite;
    private final float golfBallSize = 16;
    private float powerBallSize = golfBallSize;


    public PowerBallView(PowerBallModel model, SpriteBatch batch) {
        super(batch);
        this.model = model;
        this.batch = batch;
        this.ballTexture = Assets.getTexture("Images/GolfBall.png");
        this.powerSprite =  new Sprite(this.ballTexture);
    }

    public void updatePosition(Body golfBallBody) {
        powerSprite.setX(golfBallBody.getPosition().x * PPM - ((ballTexture.getWidth() * (powerBallSize / golfBallSize)) / 2));
        powerSprite.setY(golfBallBody.getPosition().y * PPM - ((ballTexture.getHeight() * (powerBallSize / golfBallSize)) / 2));
        powerSprite.setSize(powerBallSize, powerBallSize);
        powerSprite.draw(batch);
    }

    public void render(float dt) {

    }

    public short getLayer() {
        return this.model.getLayer();
    }

    public Texture getTexture() {
        return ballTexture;
    }

    public Sprite getPowerSprite() {
        return powerSprite;
    }

    public float getSize() {
        return powerBallSize;
    }

    public void setSize(float newSize) {
        this.powerBallSize = newSize;
    }

    public boolean powerUp(boolean power) {
        if (power) {
            if (powerBallSize >= (golfBallSize * 8)) {
                power = false;
            } else {
                powerBallSize *= 1.1f;
            }
        } else {
            if (powerBallSize <= (golfBallSize + 1)) {
                powerBallSize = golfBallSize;
                power = true;
            } else {
                powerBallSize *= 0.9f;
            }
        }
        return power;
    }

}
