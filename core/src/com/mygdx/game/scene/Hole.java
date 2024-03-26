package com.mygdx.game.scene;

import static Utils.Constants.PPM;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.InputHandler;
import com.mygdx.game.gameobject.controller.GolfBallController;
import com.mygdx.game.gameobject.model.BackgroundElementModel;
import com.mygdx.game.gameobject.model.GolfBallModel;
import com.mygdx.game.gameobject.view.BackgroundElementView;
import com.mygdx.game.gameobject.view.GolfBallView;
import com.mygdx.game.gameobject.view.GameObjectView;
import com.mygdx.game.utils.Assets;

import java.util.TreeMap;

import Utils.TileObjectUtil;

public class Hole extends Scene {

    private static final short FOREGROUND_LAYER = Short.MAX_VALUE;
    private static final short PLAYER_LAYER = Short.MAX_VALUE;

    private Game game;
    private final float SCALE = 2.5f;
    GolfBallView golfBallView;
    GolfBallController golfBallController;
    private GolfBallModel golfBall;
    private World world;
    private TiledMap map;
    private OrthogonalTiledMapRenderer tmr;
    private String mapFile;
    private final float arrowSize = 32;
    private float arrowAngle = 0;
    private float ballPower = 0;
    private int shotCounter = 0;
    private int par = 3;
    Texture ballTexture;
    Texture arrowTexture;
    private Box2DDebugRenderer b2dr;
    private BitmapFont font;
    private float ballAngle = 0;

    private Boolean powerUp = true;

    private final float golfBallSize = 16;
    private float powerBallSize = golfBallSize;


    public Hole(Game game, String mapFile) { // Need to add a parameter for the map file
        this.game = game;
        this.mapFile = mapFile;

    }

    @Override
    public void initScene() {
        Assets.init();
        this.loadAssets();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / SCALE, h / SCALE); //From YT
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        this.createHole();


        b2dr = new Box2DDebugRenderer();
        font = new BitmapFont();
        ballTexture = Assets.getTexture("Images/GolfBall.png");
        arrowTexture = Assets.getTexture("Images/Arrow.png");

        batch = new SpriteBatch();

        golfBall = new GolfBallModel(new Vector2(5, 2), map, world);
        golfBallView = new GolfBallView(golfBall, batch);
        golfBallController = new GolfBallController(golfBall);

        backgroundObjects = new Array<>();
        gameObjectViews = new TreeMap<>();
        dynamicGameObjects = new Array<>();

        Array<GameObjectView> playerViews = new Array<>();
        Array<GameObjectView> foregroundViews = new Array<>();

        this.gameObjectViews.put(FOREGROUND_LAYER, foregroundViews);
        playerViews.add(golfBallView);
        this.gameObjectViews.put(PLAYER_LAYER, playerViews);

        Gdx.input.setInputProcessor(new InputHandler(golfBallController));

    }

    private void createHole() {
        world = new World(new Vector2(0, -10), false);
        map = new TmxMapLoader().load(mapFile);
        tmr = new OrthogonalTiledMapRenderer(map); // Can pass in batch??? for shaders???

        TileObjectUtil.parseTileObjectLayer(world, map.getLayers().get("collision-layer").getObjects());

//        BackgroundElement backdrop = new BackgroundElement(new Vector2(), new Vector2(60F, 65F), BackgroundElementType.BACKDROP, (short)(2000));
//        BackgroundElementView backdropView = new BackgroundElementView(backdrop, batch);
//
//        this.backgroundObjects.add(backdrop);
//        this.addToGameObjectViews(backdropView);
    }

    @Override
    public void updateScene(float dt) {
        batch.begin();
        tmr.render();
        Sprite ballSprite = new Sprite(ballTexture);
        Body golfBallBody = golfBall.getBody();
        ballAngle = golfBallBody.getAngle() * (180 / (float) Math.PI);
        ballSprite.rotate(ballAngle);
        ballSprite.setX(golfBallBody.getPosition().x * PPM - ((float) ballTexture.getWidth() / 2));
        ballSprite.setY(golfBallBody.getPosition().y * PPM - ((float) ballTexture.getHeight() / 2));

        Sprite powerSprite = new Sprite(ballTexture);
        powerSprite.setX(golfBallBody.getPosition().x * PPM - ((ballTexture.getWidth() * (powerBallSize / golfBallSize)) / 2));
        powerSprite.setY(golfBallBody.getPosition().y * PPM - ((ballTexture.getHeight() * (powerBallSize / golfBallSize)) / 2));
        powerSprite.setSize(powerBallSize, powerBallSize);
        powerSprite.draw(batch);


        Sprite arrowSprite = new Sprite(arrowTexture);
        arrowSprite.setX(golfBallBody.getPosition().x * PPM);
        arrowSprite.setY(golfBallBody.getPosition().y * PPM - ((ballTexture.getHeight() * (arrowSize / golfBallSize)) / 2));
        arrowSprite.setSize(arrowSize, arrowSize);
        arrowSprite.setOrigin(0, golfBallSize);
        arrowSprite.setRotation(arrowAngle);
        arrowSprite.draw(batch);

        if (shotCounter < par) {
            font.setColor(Color.GREEN);
        } else if (shotCounter == par) {
            font.setColor(Color.YELLOW);
        } else {
            font.setColor(Color.RED);
        }
        font.draw(batch, String.valueOf(shotCounter), camera.viewportWidth / 2f + (camera.position.x - camera.viewportWidth / 2f), camera.viewportHeight / 2f + camera.position.y);


        ballSprite.draw(batch);
        batch.end();

        // updateGame code
        b2dr.render(world, camera.combined.scl(PPM));
        world.step(1 / 60f, 6, 2);
        inputUpdate(dt);
        updateGolfBallPosition(dt);
        updateCamera(dt);
        tmr.setView(camera);
        batch.setProjectionMatrix(camera.combined);
    }

    public void inputUpdate(float delta) {
        if (Gdx.input.isKeyPressed((Input.Keys.LEFT)) && arrowAngle < 180) {
            arrowAngle++;
//            System.out.println(arrowAngle);
        }
        if (Gdx.input.isKeyPressed((Input.Keys.RIGHT)) && arrowAngle > 0) {
            arrowAngle--;
//            System.out.println(arrowAngle);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (isBallStopped()) {
                if (powerUp) {
                    if (powerBallSize >= (golfBallSize * 8)) {
                        powerUp = false;
                    } else {
                        powerBallSize *= 1.1f;
                    }
                } else {
                    if (powerBallSize <= (golfBallSize + 1)) {
                        powerBallSize = golfBallSize;
                        powerUp = true;
                    } else {
                        powerBallSize *= 0.9f;
                    }
                }
            }
        }
    }

    private boolean isBallStopped() {
        Body golfBallBody = golfBall.getBody();
        boolean isBallOnFloor = golfBallBody.getPosition().y < 6.33; //fixme: this value is hardcoded for map 1, may not work for others
        boolean doesBallHaveNoVelocity = golfBallBody.getLinearVelocity().x <= 0.2 && golfBallBody.getLinearVelocity().y <= 0.2 &&
                golfBallBody.getLinearVelocity().x >= -0.2 && golfBallBody.getLinearVelocity().y >= -0.2;
        return isBallOnFloor && doesBallHaveNoVelocity;
    }

    public void updateCamera(float delta) {
        Body golfBallBody = golfBall.getBody();
        Vector3 position = camera.position;
        position.x = golfBallBody.getPosition().x * PPM;
        position.y = (golfBallBody.getPosition().y + 3.1f) * PPM;

//		Rectangle bounds = tmr.getViewBounds();
        float mapWidth = tmr.getViewBounds().width;
        float mapHeight = tmr.getViewBounds().height;
        if (tmr != null) {

        } else {
            camera.position.set(position);
        }

        camera.update();
    }

    public void updateGolfBallPosition(float delta) {
        Body golfBallBody = golfBall.getBody();
        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (powerBallSize > golfBallSize) {
                float horizontalForce = (90 - arrowAngle) * powerBallSize / 3;
                float verticalForce = getVerticalForce(arrowAngle) * powerBallSize / 5;
                golfBallBody.applyForceToCenter(horizontalForce, verticalForce, false);
                powerBallSize = golfBallSize;
                shotCounter++;
                System.out.println(shotCounter);
            }
        }
    }

    public static float getVerticalForce(float arrowAngle) {
        if (arrowAngle == 90) {
            return 90;
        } else if (arrowAngle < 90) {
            return 90 * (arrowAngle / 90);
        } else {
            return 90 - (arrowAngle - 90);
        }
    }
    private void addToGameObjectViews(GameObjectView view) {
        short layer = view.getLayer();
        if (!this.gameObjectViews.containsKey(layer)) {
            this.gameObjectViews.put(layer, new Array<>());
        }
        this.gameObjectViews.get(layer).add(view);
    }

    private void loadAssets() {
        Assets.addTexture("Maps/sky.jpeg");
        Assets.addTexture("Images/GolfBall.png");
        Assets.addTexture("Images/Arrow.png");
        Assets.loadAll();
    }

}
