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
    private Boolean isMute = false;

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

        Texture backgroundTexture = new Texture(Gdx.files.internal("Images/Menu.jpg"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(w, h);

        titleTexture = new Texture(Gdx.files.internal("Images/Title.png"));
        Image titleImage = new Image(titleTexture);
        titleImage.setSize(750, 100);

        Texture playButtonTexture = new Texture(Gdx.files.internal("Images/PlayButton.png"));
        TextureRegion playButtonRegion = new TextureRegion(playButtonTexture);
        ImageButton.ImageButtonStyle playButtonStyle = new ImageButton.ImageButtonStyle();
        playButtonStyle.imageUp = new TextureRegionDrawable(playButtonRegion);
        playButtonStyle.imageDown = new TextureRegionDrawable(playButtonRegion);
        final ImageButton playButton = new ImageButton(playButtonStyle);

        Texture quitButtonTexture = new Texture(Gdx.files.internal("Images/QuitButton.png"));
        TextureRegion quitButtonRegion = new TextureRegion(quitButtonTexture);
        ImageButton.ImageButtonStyle quitButtonStyle = new ImageButton.ImageButtonStyle();
        quitButtonStyle.imageUp = new TextureRegionDrawable(quitButtonRegion);
        quitButtonStyle.imageDown = new TextureRegionDrawable(quitButtonRegion);
        final ImageButton quitButton = new ImageButton(quitButtonStyle);

        Texture blueTexture = new Texture(Gdx.files.internal("Images/blue.png"));
        final Image blueImage = new Image(blueTexture);

        Texture volumeButtonTexture = new Texture(Gdx.files.internal("Images/volume.png"));
        TextureRegion volumeButtonRegion = new TextureRegion(volumeButtonTexture);
        ImageButton.ImageButtonStyle volumeButtonStyle = new ImageButton.ImageButtonStyle();
        volumeButtonStyle.imageUp = new TextureRegionDrawable(volumeButtonRegion);
        volumeButtonStyle.imageDown = new TextureRegionDrawable(volumeButtonRegion);
        final ImageButton volumeButton = new ImageButton(volumeButtonStyle);

        float buttonWidth = 250;
        float buttonHeight = 200;
        playButton.setWidth(buttonWidth);
        playButton.setHeight(buttonHeight);
        quitButton.setWidth(buttonWidth);
        quitButton.setHeight(buttonHeight);
        blueImage.setWidth(125);
        blueImage.setHeight(125);
        volumeButton.setWidth(125);
        volumeButton.setHeight(125);

        float centerX = w / 2f;

        titleImage.setPosition(centerX - titleImage.getWidth() / 2f, 500);
        playButton.setPosition(centerX - playButton.getWidth() / 2f, 275);
        quitButton.setPosition(centerX - quitButton.getWidth() / 2f, 100);
        volumeButton.setPosition(0, 0);
        blueImage.setPosition(0, 0);


        // Add listeners to buttons
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hole = new Hole(game, "Maps/Hole1.tmx");
                hole.initScene();
                hole.setIsMute(isMute);
                game.setScreen(hole);
                if (isMute) {
                    backgroundMusic.setVolume(0);
                } else {
                    backgroundMusic.setVolume(0.25f);
                }
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle exit button click
                Gdx.app.exit();
            }
        });

        volumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isMute) {
                    Texture muteButtonTexture = new Texture(Gdx.files.internal("Images/mute.png"));
                    volumeButtonRegion.setTexture(muteButtonTexture);
                    backgroundMusic.setVolume(0);
                } else {
                    Texture volumeButtonTexture = new Texture(Gdx.files.internal("Images/volume.png"));
                    volumeButtonRegion.setTexture(volumeButtonTexture);
                    backgroundMusic.setVolume(0.5f);
                }
                isMute = !isMute;
            }
        });

        stage.addActor(backgroundImage);
        stage.addActor(titleImage);
        stage.addActor(playButton);
        stage.addActor(quitButton);
        stage.addActor(blueImage);
        stage.addActor(volumeButton);

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
