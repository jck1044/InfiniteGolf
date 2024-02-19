package com.mygdx.game.gameobject.view;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.gameobject.model.BallModel;

public class BallView {
    private final BallModel model;
    private final SpriteBatch batch;
    private final Array<Array<Sprite>> animationFrames;
    TextureAtlas textureAtlas;
    protected Animation currentAnimation;
    protected int currentAnimationSequence = -1;
    protected TextureRegion currentAnimationFrame;
    private float stateTime;
    final float animationFrameDuration = 0.5F;
    private int rotationAngle;
    final int rotationDegrees = 50;

    public BallView(BallModel model, SpriteBatch batch) {
        this.model = model;
        this.batch = batch;
        this.textureAtlas = new TextureAtlas("textures/ball.txt");
        this.animationFrames = new Array<>();
        animationFrames.add(textureAtlas.createSprites("ball"));
        currentAnimation = new Animation<>(this.animationFrameDuration, animationFrames.get(0));
        stateTime = 0f;
        this.rotationAngle = 0;
    }

    public void render(float dt) {
        if (this.model.getMovementVector().x > 0) { //moving right
            this.rotationAngle = (this.rotationAngle + rotationDegrees) % 360;
        } else if (this.model.getMovementVector().x < 0) { //moving left
            this.rotationAngle = (this.rotationAngle - rotationDegrees + 360) % 360;
        }
        stateTime += dt;
        currentAnimationFrame = (TextureRegion) currentAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentAnimationFrame, this.model.getPosition().x, this.model.getPosition().y, this.model.getRadius(), this.model.getRadius(), this.model.getRadius() * 2, this.model.getRadius() * 2, 1, 1, this.rotationAngle);
    }

    public void dispose() {
        this.textureAtlas.dispose();
    }
}
