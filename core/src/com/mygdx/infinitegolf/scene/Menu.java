package com.mygdx.infinitegolf.scene;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.mygdx.infinitegolf.gameobject.controller.GolfBallController;
import com.mygdx.infinitegolf.gameobject.model.BackgroundElementModel;
import com.mygdx.infinitegolf.gameobject.model.GolfBallModel;
import com.mygdx.infinitegolf.gameobject.view.GolfBallView;
import com.mygdx.infinitegolf.gameobject.view.GameObjectView;
import com.mygdx.infinitegolf.utils.Assets;

import java.util.SortedMap;

public class Menu extends Scene {

    private static final short FOREGROUND_LAYER = Short.MAX_VALUE;
    private Game game;
    GolfBallModel playerBall;
    GolfBallView playerBallView;
    GolfBallController playerController;
    Array<BackgroundElementModel> backgroundObjects;
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
