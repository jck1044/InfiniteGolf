package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.gameobject.controller.BallController;
import com.mygdx.game.gameobject.model.BallModel;
import com.mygdx.game.gameobject.view.BallView;

public class InfiniteGolf extends ApplicationAdapter {
    SpriteBatch batch;
    BallModel playerBall;
    BallView playerBallView;
    BallController playerController;


    @Override
    public void create() {
        batch = new SpriteBatch();
        playerBall = new BallModel(new Vector2(100, 100));
        playerBallView = new BallView(playerBall, batch);
        playerController = new BallController(playerBall);
        Gdx.input.setInputProcessor(new InputHandler(playerController));
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        playerBall.updatePosition(dt);
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        playerBallView.render(dt);
        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        playerBallView.dispose();
    }
}
