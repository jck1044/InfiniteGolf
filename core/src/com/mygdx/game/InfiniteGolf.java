package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.gameobject.controller.BallController;
import com.mygdx.game.gameobject.model.BackgroundElement;
import com.mygdx.game.gameobject.model.BallModel;
import com.mygdx.game.gameobject.model.GrassModel;
import com.mygdx.game.gameobject.view.BackgroundElementView;
import com.mygdx.game.gameobject.view.BallView;
import com.mygdx.game.gameobject.view.GameObjectView;
import com.mygdx.game.gameobject.view.GrassView;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class InfiniteGolf extends ApplicationAdapter {
    private static final short FOREGROUND_LAYER = Short.MAX_VALUE;
    SpriteBatch batch;
    BallModel playerBall;
    BallView playerBallView;
    BallController playerController;
    Array<BackgroundElement> backgroundObjects;
    SortedMap<Short, Array<GameObjectView>> gameObjectViews;


    @Override
    public void create() {
        batch = new SpriteBatch();
        playerBall = new BallModel(new Vector2(100, 200), 62F);
        playerBallView = new BallView(playerBall, batch);
        playerController = new BallController(playerBall);
        this.backgroundObjects = new Array<>();
        this.gameObjectViews = new TreeMap<>();
        Array<GameObjectView> foregroundViews = new Array<>();
        this.gameObjectViews.put(FOREGROUND_LAYER, foregroundViews);
        Gdx.input.setInputProcessor(new InputHandler(playerController));
        this.createBackdrop();
        this.createGrass();
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        this.updateGame(dt);
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        for (Map.Entry<Short, Array<GameObjectView>> entry : this.gameObjectViews.entrySet()) {
            for (int i = 0; i < entry.getValue().size; i++) {
                entry.getValue().get(i).render(dt);
            }
        }
        playerBallView.render(dt);
        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        playerBallView.dispose();
    }

    private void updateGame(float dt) {
        // check for collisions
//        this.checkCollisions();
        playerBall.updatePosition(dt);
    }


    private void createBackdrop() {
        BackgroundElement backdrop = new BackgroundElement(new Vector2(), new Vector2(1024F, 683F), (short)(2000));
        BackgroundElementView backdropView = new BackgroundElementView(backdrop, batch);

        this.backgroundObjects.add(backdrop);
        this.addToGameObjectViews(backdropView);
    }

    private void createGrass() {
        GrassModel grassModel = new GrassModel(new Vector2(), new Vector2(1024F, 128F), (short)(3000));
        GrassView grassView = new GrassView(grassModel, batch);
        this.addToGameObjectViews(grassView);
    }

    private void addToGameObjectViews(GameObjectView view) {
        short layer = view.getLayer();
        if (!this.gameObjectViews.containsKey(layer)) {
            this.gameObjectViews.put(layer, new Array<>());
        }
        this.gameObjectViews.get(layer).add(view);
    }
}
