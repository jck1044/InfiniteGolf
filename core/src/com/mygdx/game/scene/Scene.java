package com.mygdx.game.scene;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public abstract class Scene implements Screen {

    Camera camera;
    SpriteBatch batch;
    public void render (float dt) {
        this.updateScene(dt);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();

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
