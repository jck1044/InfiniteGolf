package com.mygdx.infinitegolf.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.infinitegolf.utils.Assets;

import java.util.TreeMap;

public class EndScreen extends Scene {

    private Stage stage;
    public void initScene() {
        Assets.init();
        this.loadAssets();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30 * (h / w));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        gameObjectViews = new TreeMap<>();

        stage = new Stage(new StretchViewport(w, h));
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void updateScene(float dt) {

    }

    @Override
    public void render(float dt) {
        super.render(dt);
        // Draw the stage
        stage.draw();
    }

    @Override
    public void dispose() {

    }

    private void loadAssets() {
        Assets.loadAll();
    }
}
