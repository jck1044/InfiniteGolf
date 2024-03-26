package com.mygdx.game.gameobject.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.gameobject.model.GolfBallModel;
import com.mygdx.game.utils.Assets;

public class GolfBallView extends GameObjectView {
    private final GolfBallModel model;
    private final SpriteBatch batch;
    protected Animation currentAnimation;
    protected TextureRegion currentAnimationFrame;
    private float stateTime;
    private int rotationAngle;
    final int rotationDegrees = 50;
    private Sprite staticImg;



    public GolfBallView(GolfBallModel model, SpriteBatch batch) {
        super(batch);
        this.model = model;
        this.batch = batch;
        this.staticImg = new Sprite(Assets.getTexture("Images/GolfBall.png"));

    }

    public void render(float dt) {

    }

    public short getLayer() {
        return this.model.getLayer();
    }


}
