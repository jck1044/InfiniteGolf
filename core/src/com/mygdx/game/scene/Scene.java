package com.mygdx.game.scene;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.gameobject.model.BackgroundElement;
import com.mygdx.game.gameobject.view.GameObjectView;

import java.util.Map;
import java.util.SortedMap;

public abstract class Scene implements Screen {

    Camera camera;
    SpriteBatch batch;

    Array<BackgroundElement> backgroundObjects;

    SortedMap<Short, Array<GameObjectView>> gameObjectViews;

    public void render (float dt) {
        this.updateScene(dt);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        ScreenUtils.clear(0, 0, 0, 1);
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
    public void dispose () {
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
