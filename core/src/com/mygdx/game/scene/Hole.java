package com.mygdx.game.scene;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.InputHandler;
import com.mygdx.game.gameobject.controller.BallController;
import com.mygdx.game.gameobject.model.BackgroundElement;
import com.mygdx.game.gameobject.model.BallModel;
import com.mygdx.game.gameobject.model.GrassModel;
import com.mygdx.game.gameobject.view.BackgroundElementView;
import com.mygdx.game.gameobject.view.BallView;
import com.mygdx.game.gameobject.view.GameObjectView;
import com.mygdx.game.gameobject.view.GrassView;

import java.util.SortedMap;
import java.util.TreeMap;

public class Hole extends Scene {


    private static final short FOREGROUND_LAYER = Short.MAX_VALUE;
    private Game game;
    BallModel playerBall;
    BallView playerBallView;
    BallController playerController;
    Array<BackgroundElement> backgroundObjects;
    SortedMap<Short, Array<GameObjectView>> gameObjectViews;
    public Hole(Game game) { // Need to add a parameter for the map file
        this.game = game;
    }

    @Override
    public void initScene() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30 * (h / w));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

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
    public void updateScene(float dt) {
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
