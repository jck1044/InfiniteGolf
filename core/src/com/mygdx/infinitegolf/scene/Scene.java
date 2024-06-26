package com.mygdx.infinitegolf.scene;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.infinitegolf.gameobject.model.BackgroundElementModel;
import com.mygdx.infinitegolf.gameobject.model.DynamicGameObjectModel;
import com.mygdx.infinitegolf.gameobject.view.GameObjectView;

import java.util.Map;
import java.util.SortedMap;

public abstract class Scene implements Screen {

    OrthographicCamera camera;
    SpriteBatch batch;

    Array<BackgroundElementModel> backgroundObjects;

    SortedMap<Short, Array<GameObjectView>> gameObjectViews;

    Array<DynamicGameObjectModel> dynamicGameObjects;


    public void render(float dt) {
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        ScreenUtils.clear(0.325f, 0.576f, 0.867f, 1); // Set clear color to our sky's blue
        batch.begin();

        for (
                Map.Entry<Short, Array<GameObjectView>> entry : this.gameObjectViews.entrySet()) {
            for (int i = 0; i < entry.getValue().size; i++) {
                entry.getValue().get(i).render(dt);
            }
        }

        batch.end();

    }

    public abstract void initScene();

    public abstract void updateScene(float dt);

    @Override
    public void show() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
