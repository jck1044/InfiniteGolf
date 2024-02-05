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

    public BallView(BallModel model, SpriteBatch batch) {
        this.model = model;
        this.batch = batch;
        this.textureAtlas = new TextureAtlas("textures/ball.txt");
        this.animationFrames = new Array<>();
        animationFrames.add(textureAtlas.createSprites("ball_0"));
        animationFrames.add(textureAtlas.createSprites("ball_1"));
        animationFrames.add(textureAtlas.createSprites("ball_2"));
        animationFrames.add(textureAtlas.createSprites("ball_3"));
        currentAnimation = new Animation<>(this.animationFrameDuration, animationFrames.get(0));
        stateTime = 0f;
    }

    public void render(float dt) {
        if (this.model.getMovementVector().x != 0 || this.model.getMovementVector().y != 0) { //ball is moving --> should be rotating
            int nextAnimationSequence = getNextBallRotation(currentAnimationSequence);
            Array<Sprite> spriteList = animationFrames.get(nextAnimationSequence);
            currentAnimation = new Animation<>(this.animationFrameDuration, spriteList);
            currentAnimationSequence = nextAnimationSequence;
        }
        stateTime += dt;
        currentAnimationFrame = (TextureRegion) currentAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentAnimationFrame, this.model.getPosition().x, this.model.getPosition().y, 128, 128);
    }

    public int getNextBallRotation(int currentAnimationSequence) {
        if (currentAnimationSequence == 0) {
            return 1;
        } else if (currentAnimationSequence == 1) {
            return 2;
        } else if (currentAnimationSequence == 2) {
            return 3;
        } else {
            return 0;
        }
    }

    public void dispose() {
        this.textureAtlas.dispose();
    }
}
