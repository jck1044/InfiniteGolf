package com.mygdx.infinitegolf.scene;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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

public class MainMenu extends Scene {

    private Game game;

    private Stage stage;
    private Hole hole;
    private Skin skin;

    private Music backgroundMusic;

    private Texture titleTexture;

    public MainMenu(Game game) {
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
        backgroundMusic.setVolume(0.5f);
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

        Texture playButtonTexture = new Texture(Gdx.files.internal("Images/PlayButton.png"));
        TextureRegion playButtonRegion = new TextureRegion(playButtonTexture);
        ImageButton.ImageButtonStyle playButtonStyle = new ImageButton.ImageButtonStyle();
        playButtonStyle.imageUp = new TextureRegionDrawable(playButtonRegion);
        playButtonStyle.imageDown = new TextureRegionDrawable(playButtonRegion);
        final ImageButton playButton = new ImageButton(playButtonStyle);

        Texture optionsButtonTexture = new Texture(Gdx.files.internal("Images/OptionsButton.png"));
        TextureRegion optionsButtonRegion = new TextureRegion(optionsButtonTexture);
        ImageButton.ImageButtonStyle optionsButtonStyle = new ImageButton.ImageButtonStyle();
        optionsButtonStyle.imageUp = new TextureRegionDrawable(optionsButtonRegion);
        optionsButtonStyle.imageDown = new TextureRegionDrawable(optionsButtonRegion);
        final ImageButton optionsButton = new ImageButton(optionsButtonStyle);

        Texture quitButtonTexture = new Texture(Gdx.files.internal("Images/QuitButton.png"));
        TextureRegion quitButtonRegion = new TextureRegion(quitButtonTexture);
        ImageButton.ImageButtonStyle quitButtonStyle = new ImageButton.ImageButtonStyle();
        quitButtonStyle.imageUp = new TextureRegionDrawable(quitButtonRegion);
        optionsButtonStyle.imageDown = new TextureRegionDrawable(quitButtonRegion);
        final ImageButton quitButton = new ImageButton(quitButtonStyle);

        float buttonWidth = 250;
        float buttonHeight = 200;
        playButton.setWidth(buttonWidth);
        playButton.setHeight(buttonHeight);
        optionsButton.setWidth(buttonWidth);
        optionsButton.setHeight(buttonHeight);
        quitButton.setWidth(buttonWidth);
        quitButton.setHeight(buttonHeight);

        float centerX = w / 2f;

        titleImage.setPosition(centerX - titleImage.getWidth() / 2f, 500);
        playButton.setPosition(centerX - playButton.getWidth() / 2f, 300);
        optionsButton.setPosition(centerX - optionsButton.getWidth() / 2f, 150);
        quitButton.setPosition(centerX - quitButton.getWidth() / 2f, 0);


        // Add listeners to buttons
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hole = new Hole(game, "Maps/Hole1.tmx");
                hole.initScene();
                game.setScreen(hole);
                backgroundMusic.setVolume(0.25f);
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle options button click
                // You can show options screen here
            }
        });

        quitButton.addListener(new ClickListener() {
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
        stage.addActor(quitButton);

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
