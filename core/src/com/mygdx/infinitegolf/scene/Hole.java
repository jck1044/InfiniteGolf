package com.mygdx.infinitegolf.scene;

import static Utils.Constants.PPM;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.infinitegolf.InputHandler;
import com.mygdx.infinitegolf.gameobject.controller.ArrowController;
import com.mygdx.infinitegolf.gameobject.controller.GolfBallController;
import com.mygdx.infinitegolf.gameobject.controller.PowerBallController;
import com.mygdx.infinitegolf.gameobject.model.ArrowModel;
import com.mygdx.infinitegolf.gameobject.model.GolfBallModel;
import com.mygdx.infinitegolf.gameobject.model.PowerBallModel;
import com.mygdx.infinitegolf.gameobject.view.ArrowView;
import com.mygdx.infinitegolf.gameobject.view.GolfBallView;
import com.mygdx.infinitegolf.gameobject.view.GameObjectView;
import com.mygdx.infinitegolf.gameobject.view.PowerBallView;
import com.mygdx.infinitegolf.utils.Assets;

import java.util.TreeMap;

import Utils.TileObjectUtil;

public class Hole extends Scene {

    private static final short FOREGROUND_LAYER = Short.MAX_VALUE;
    private static final short PLAYER_LAYER = Short.MAX_VALUE;

    private Game game;
    private final float SCALE = 2.5f;
    private GolfBallView golfBallView;
    private GolfBallController golfBallController;
    private GolfBallModel golfBallModel;
    private PowerBallView powerBallView;
    private PowerBallController powerBallController;
    private PowerBallModel powerBallModel;
    private ArrowView arrowView;
    private ArrowController arrowController;
    private ArrowModel arrowModel;
    private World world;
    private TiledMap map;
    private OrthogonalTiledMapRenderer tmr;
    private String mapFile;
    private int holeShotCounter = 0;
    private int totalShotCounter = 0;
    private int par = 3;
    private Box2DDebugRenderer b2dr;
    private BitmapFont font;

    private Boolean powerUp = true;

    private final float golfBallSize = 16;
    private Body golfBallBody;
    private Boolean isBallInHole = false;
    private int holeNumber = 1;

    public Hole(Game game, String mapFile) { // Need to add a parameter for the map file
        this.game = game;
        this.mapFile = mapFile;
        this.createHole();
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

        batch = new SpriteBatch();

        golfBallModel = new GolfBallModel(new Vector2(5, 2));
        golfBallView = new GolfBallView(golfBallModel, batch);
        golfBallController = new GolfBallController(golfBallModel, golfBallView);

        powerBallModel = new PowerBallModel(new Vector2(5, 2));
        powerBallView = new PowerBallView(powerBallModel, batch);
        powerBallController = new PowerBallController(powerBallModel, powerBallView);

        arrowModel = new ArrowModel(new Vector2(5, 2));
        arrowView = new ArrowView(arrowModel, batch);
        arrowController = new ArrowController(arrowModel, arrowView);

        b2dr = new Box2DDebugRenderer();
        font = new BitmapFont();

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
        tmr = new OrthogonalTiledMapRenderer(map);

        TileObjectUtil.parseTileObjectLayer(world, map.getLayers().get("collision-layer").getObjects());

        MapProperties ballProperties = map.getLayers().get("ball")
                .getObjects().get(0).getProperties();
        float x = (float) ballProperties.get("x");
        float y = (float) ballProperties.get("y");

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = false;
        golfBallBody = world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(((float) ballProperties.get("height") / 2) / PPM);
        golfBallBody.createFixture(shape, 5.0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = .5f; // Make it bounce a little bit
        golfBallBody.createFixture(fixtureDef);

        shape.dispose();
        float linearDamping = 0.9f;
        golfBallBody.setLinearDamping(linearDamping);


//        BackgroundElement backdrop = new BackgroundElement(new Vector2(), new Vector2(60F, 65F), BackgroundElementType.BACKDROP, (short)(2000));
//        BackgroundElementView backdropView = new BackgroundElementView(backdrop, batch);
//
//        this.backgroundObjects.add(backdrop);
//        this.addToGameObjectViews(backdropView);
    }

    @Override
    public void updateScene(float dt) {
        updatePhysics(dt);

        tmr.render();
        batch.begin();


        if (!isBallInHole) {
            if (!isBallStopped()) {
                golfBallController.updatePosition(golfBallBody);
            } else {
                powerBallController.updatePosition(golfBallBody);
                arrowController.updatePosition(golfBallBody);
            }

            if (holeShotCounter < par) {
                font.setColor(Color.GREEN);
            } else if (holeShotCounter == par) {
                font.setColor(Color.YELLOW);
            } else {
                font.setColor(Color.RED);
            }
            font.draw(batch, String.valueOf(holeShotCounter), camera.viewportWidth / 2f + (camera.position.x - camera.viewportWidth / 2f), camera.viewportHeight / 2f + camera.position.y);
        } else {
            font.getData().setScale(2);
            font.draw(batch, "Hole # " + holeNumber + " finished in " + holeShotCounter + " shots",
                    camera.position.x - camera.viewportWidth / 2.5f, camera.viewportHeight / 1.5f);
            font.getData().setScale(1);
            font.draw(batch, "Press anything to continue",
                    camera.position.x - camera.viewportWidth / 2.5f + 80, camera.viewportHeight / 1.5f - 35);
        }
        golfBallController.getBallSprite().draw(batch);

        batch.end();

    }

    public void updatePhysics(float dt) {
        b2dr.render(world, camera.combined.scl(PPM));
        world.step(1 / 60f, 6, 2);
        inputUpdate(dt);
        updateGolfBallPosition(dt);
        updateCamera(dt);
        checkIfBallInHole(dt);
        tmr.setView(camera);
        batch.setProjectionMatrix(camera.combined);
    }

    private void checkIfBallInHole(float dt) {
        if (golfBallBody.getPosition().y < 5.4) {
            isBallInHole = true;
        }
    }

    public void inputUpdate(float delta) {
        if (!isBallInHole) {
            if (Gdx.input.isKeyPressed((Input.Keys.LEFT)) && arrowController.getAngle() < 180) {
                arrowController.increaseAngle();
            }
            if (Gdx.input.isKeyPressed((Input.Keys.RIGHT)) && arrowController.getAngle() > 0) {
                arrowController.decreaseAngle();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (isBallStopped()) {
                    powerUp = powerBallController.powerUp(powerUp);
                }
            }
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()) {
                goToNewHole();
            }
        }
    }

    private void goToNewHole() {
        holeNumber++;
        isBallInHole = false;
        totalShotCounter += holeShotCounter;
        holeShotCounter = 0;
        arrowView.resetAngle();
        this.mapFile = "Maps/Hole" + holeNumber + ".tmx";
        this.createHole();
    }

    private boolean isBallStopped() {
        float speedThreshold = .015f;
        float recentSpeed = 0;
        float speedNow = golfBallBody.getLinearVelocity().len();
        recentSpeed = 0.1f * speedNow + 0.9f * recentSpeed;
        if (recentSpeed < speedThreshold) {
            golfBallBody.setLinearVelocity(0, 0);
            return true;
        } else {
            return false;
        }
    }

    public void updateCamera(float delta) {
        Vector3 position = camera.position;
        position.x = golfBallBody.getPosition().x * PPM;
        position.y = (golfBallBody.getPosition().y + 3.1f) * PPM;
        if (position.x - camera.viewportWidth / 2f < 0) {
            position.x = camera.viewportWidth / 2f;
        }
        if (position.x + camera.viewportWidth / 2f > 480) {
            position.x = 480 - camera.viewportWidth / 2f;
        }
        if (position.y + camera.viewportHeight / 2f > 321) {
            position.y = 321 - camera.viewportHeight / 2f;
        }
        camera.position.set(position);
        camera.update();
    }

    public void updateGolfBallPosition(float delta) {
        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (powerBallController.getSize() > golfBallSize) {
                float horizontalForce = (90 - arrowController.getAngle()) * powerBallController.getSize() / 2.75f;
                float verticalForce = getVerticalForce(arrowController.getAngle()) * powerBallController.getSize() / 2.75f;
                golfBallBody.applyForceToCenter(horizontalForce, verticalForce, false);
                powerBallController.setSize(golfBallSize);
                holeShotCounter++;
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
