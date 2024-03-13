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
import com.mygdx.game.gameobject.view.BallView;
import com.mygdx.game.gameobject.view.GameObjectView;
import com.mygdx.game.utils.Assets;

import java.util.SortedMap;
import java.util.TreeMap;

public class Menu extends Scene {

    private static final short FOREGROUND_LAYER = Short.MAX_VALUE;
    private Game game;
    BallModel playerBall;
    BallView playerBallView;
    BallController playerController;
    Array<BackgroundElement> backgroundObjects;
    SortedMap<Short, Array<GameObjectView>> gameObjectViews;
    public Menu(Game game) {
        this.game = game;
    }

    @Override
    public void initScene() {
        Assets.init();
        this.loadAssets();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30 * (h / w));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

    }

    @Override
    public void updateScene(float dt) {

    }

    private void loadAssets() {
        Assets.loadAll();
    }
}
