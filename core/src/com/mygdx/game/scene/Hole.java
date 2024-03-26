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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.InputHandler;
import com.mygdx.game.gameobject.controller.ArrowController;
import com.mygdx.game.gameobject.controller.GolfBallController;
import com.mygdx.game.gameobject.controller.PowerBallController;
import com.mygdx.game.gameobject.model.ArrowModel;
import com.mygdx.game.gameobject.model.GolfBallModel;
import com.mygdx.game.gameobject.model.PowerBallModel;
import com.mygdx.game.gameobject.view.ArrowView;
import com.mygdx.game.gameobject.view.GolfBallView;
import com.mygdx.game.gameobject.view.GameObjectView;
import com.mygdx.game.gameobject.view.PowerBallView;
import com.mygdx.game.utils.Assets;

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
    private int shotCounter = 0;
    private int par = 3;
    private Box2DDebugRenderer b2dr;
    private BitmapFont font;

    private Boolean powerUp = true;

    private final float golfBallSize = 16;
    private Body golfBallBody;


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
        shape.dispose();
        float angularDamping = 10f;
        golfBallBody.setAngularDamping(angularDamping);


//        BackgroundElement backdrop = new BackgroundElement(new Vector2(), new Vector2(60F, 65F), BackgroundElementType.BACKDROP, (short)(2000));
//        BackgroundElementView backdropView = new BackgroundElementView(backdrop, batch);
//
//        this.backgroundObjects.add(backdrop);
//        this.addToGameObjectViews(backdropView);
    }

    @Override
    public void updateScene(float dt) {
        updateGame(dt);
        tmr.render();
        batch.begin();

//        if (!isBallStopped()) {
        golfBallController.updatePosition(golfBallBody);
        powerBallController.updatePosition(golfBallBody);
        arrowController.updatePosition(golfBallBody);
//        }


        if (shotCounter < par) {
            font.setColor(Color.GREEN);
        } else if (shotCounter == par) {
            font.setColor(Color.YELLOW);
        } else {
            font.setColor(Color.RED);
        }
        font.draw(batch, String.valueOf(shotCounter), camera.viewportWidth / 2f + (camera.position.x - camera.viewportWidth / 2f), camera.viewportHeight / 2f + camera.position.y);

        golfBallController.getBallSprite().draw(batch);

        batch.end();

    }

    public void updateGame(float dt) {
        b2dr.render(world, camera.combined.scl(PPM));
        world.step(1 / 60f, 6, 2);
        inputUpdate(dt);
        updateGolfBallPosition(dt);
        updateCamera(dt);
        tmr.setView(camera);
        batch.setProjectionMatrix(camera.combined);
    }

    public void inputUpdate(float delta) {
        if (Gdx.input.isKeyPressed((Input.Keys.LEFT)) && arrowController.getAngle() < 180) {
            arrowController.increaseAngle();
//            System.out.println(arrowAngle);
        }
        if (Gdx.input.isKeyPressed((Input.Keys.RIGHT)) && arrowController.getAngle() > 0) {
            arrowController.decreaseAngle();
//            System.out.println(arrowAngle);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (isBallStopped()) {
                powerUp = powerBallController.powerUp(powerUp);
            }
        }
    }

    private boolean isBallStopped() {
        boolean isBallOnFloor = golfBallBody.getPosition().y < 6.33; //fixme: this value is hardcoded for map 1, may not work for others
        boolean doesBallHaveNoVelocity = golfBallBody.getLinearVelocity().x <= 0.2 && golfBallBody.getLinearVelocity().y <= 0.2 &&
                golfBallBody.getLinearVelocity().x >= -0.2 && golfBallBody.getLinearVelocity().y >= -0.2;
        return isBallOnFloor && doesBallHaveNoVelocity;
    }

    public void updateCamera(float delta) {
        Vector3 position = camera.position;
        position.x = golfBallBody.getPosition().x * PPM;
        position.y = (golfBallBody.getPosition().y + 3.1f) * PPM;
        if (tmr != null) {

        } else {
            camera.position.set(position);
        }

        camera.update();
    }

    public void updateGolfBallPosition(float delta) {
        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (powerBallController.getSize() > golfBallSize) {
                float horizontalForce = (90 - arrowController.getAngle()) * powerBallController.getSize() / 3;
                float verticalForce = getVerticalForce(arrowController.getAngle()) * powerBallController.getSize() / 5;
                golfBallBody.applyForceToCenter(horizontalForce, verticalForce, false);
                powerBallController.setSize(golfBallSize);
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
