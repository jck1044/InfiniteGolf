package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Game;
import com.mygdx.game.gameobject.controller.BallController;
import com.mygdx.game.gameobject.model.BackgroundElement;
import com.mygdx.game.gameobject.model.BallModel;
import com.mygdx.game.gameobject.view.BallView;
import com.mygdx.game.gameobject.view.GameObjectView;
import com.mygdx.game.scene.Hole;
import com.mygdx.game.utils.Assets;

import java.util.SortedMap;

public class InfiniteGolf extends Game {
    SpriteBatch batch;
    BallModel playerBall;
    BallView playerBallView;
    BallController playerController;
    Array<BackgroundElement> backgroundObjects;
    SortedMap<Short, Array<GameObjectView>> gameObjectViews;


    @Override
    public void create() {

        Assets.init();
        Hole h = new Hole(this);
        h.initScene();
        setScreen(h);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0, 0, 0, 1);
        this.getScreen().render(dt);
//        batch.begin();
//        for (Map.Entry<Short, Array<GameObjectView>> entry : this.gameObjectViews.entrySet()) {
//            for (int i = 0; i < entry.getValue().size; i++) {
//                entry.getValue().get(i).render(dt);
//            }
//        }
//        playerBallView.render(dt);
//        batch.end();

    }

    @Override
    public void dispose() {
        this.getScreen().dispose();
        Assets.dispose();

    }

}
