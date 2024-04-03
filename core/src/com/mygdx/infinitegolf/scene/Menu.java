package com.mygdx.infinitegolf.scene;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.infinitegolf.gameobject.controller.GolfBallController;
import com.mygdx.infinitegolf.gameobject.model.BackgroundElementModel;
import com.mygdx.infinitegolf.gameobject.model.GolfBallModel;
import com.mygdx.infinitegolf.gameobject.view.GolfBallView;
import com.mygdx.infinitegolf.gameobject.view.GameObjectView;
import com.mygdx.infinitegolf.utils.Assets;

import java.awt.Font;
import java.util.SortedMap;
import java.util.TreeMap;

public class Menu extends Scene {

    private Game game;

    private Stage stage;
    private Hole hole;
    private Skin skin;

    private Music backgroundMusic;

    private Texture titleTexture;

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

        batch = new SpriteBatch();
        gameObjectViews = new TreeMap<>();

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/Menu.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();


        stage = new Stage(new StretchViewport(w, h));
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("Skins/uiskin.json"));


        Texture backgroundTexture = new Texture(Gdx.files.internal("Images/Menu.jpg"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(w, h);

        titleTexture = new Texture(Gdx.files.internal("Images/Title.png"));
        Image titleImage = new Image (titleTexture);
        titleImage.setSize(750, 100);

        TextButton playButton = new TextButton("Play", skin, "default");
        TextButton optionsButton = new TextButton("Options", skin, "default");
        TextButton exitButton = new TextButton("Exit", skin, "default");

        float buttonWidth = 200;
        float buttonHeight = 50;
        playButton.setWidth(buttonWidth);
        playButton.setHeight(buttonHeight);
        optionsButton.setWidth(buttonWidth);
        optionsButton.setHeight(buttonHeight);
        exitButton.setWidth(buttonWidth);
        exitButton.setHeight(buttonHeight);


        Color cornflowerBlue = new Color(0x6495EDFF);

        playButton.setColor(cornflowerBlue);
        optionsButton.setColor(cornflowerBlue);
        exitButton.setColor(cornflowerBlue);

        float centerX = w / 2f;

        titleImage.setPosition(centerX - titleImage.getWidth() / 2f, 500);
        playButton.setPosition(centerX - playButton.getWidth() / 2f, 400);
        optionsButton.setPosition(centerX - optionsButton.getWidth() / 2f, 300);
        exitButton.setPosition(centerX - exitButton.getWidth() / 2f, 200);


        // Add listeners to buttons
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hole = new Hole(game, "Maps/Hole1.tmx");
                hole.initScene();
                game.setScreen(hole);
                backgroundMusic.dispose();
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle options button click
                // You can show options screen here
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle exit button click
                Gdx.app.exit();
            }
        });

        stage.addActor(backgroundImage);
        stage.addActor(titleImage);
        stage.addActor(playButton);
        stage.addActor(optionsButton);
        stage.addActor(exitButton);

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
