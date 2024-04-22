package com.mygdx.infinitegolf.scene;

import static Utils.Constants.PPM;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
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
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mygdx.infinitegolf.InputHandler;
import com.mygdx.infinitegolf.gameobject.controller.ArrowController;
import com.mygdx.infinitegolf.gameobject.controller.GolfBallController;
import com.mygdx.infinitegolf.gameobject.controller.PowerBallController;
import com.mygdx.infinitegolf.gameobject.model.ArrowModel;
import com.mygdx.infinitegolf.gameobject.model.GolfBallModel;
import com.mygdx.infinitegolf.gameobject.model.PlayerModel;
import com.mygdx.infinitegolf.gameobject.model.PowerBallModel;
import com.mygdx.infinitegolf.gameobject.view.ArrowView;
import com.mygdx.infinitegolf.gameobject.view.GolfBallView;
import com.mygdx.infinitegolf.gameobject.view.GameObjectView;
import com.mygdx.infinitegolf.gameobject.view.PowerBallView;
import com.mygdx.infinitegolf.utils.Assets;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
    private final int[] parValues = {2, 2, 3, 3, 4, 3, 2, 6, 5};
    private Box2DDebugRenderer b2dr;
    private BitmapFont font;

    private Boolean powerUp = true;

    private EndScreen endScreen;

    private final float golfBallSize = 16;
    private Body golfBallBody;
    private Boolean isBallInHole = false;
    private int holeNumber = 1;
    private boolean isPaused = false;
    private boolean playInHoleSoundOnce = false;
    private Sound hitSound;
    private Sound inHoleSound;
    private Boolean isMute;
    private HashMap<Integer, Integer> perHoleScore;


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

        hitSound = Gdx.audio.newSound(Gdx.files.internal("Music/hit.mp3"));
        inHoleSound = Gdx.audio.newSound(Gdx.files.internal("Music/inHole.mp3"));

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

        perHoleScore = new HashMap<>();

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
    public void render(float dt) {
        this.updateScene(dt);
    }

    public void pauseGame() {
        isPaused = true;
        //fixme add pause screen
    }

    public void resumeGame() {
        isPaused = false;
    }

    @Override
    public void updateScene(float dt) {
        updatePhysics(dt);
        if (!isPaused) {
            tmr.render();
            batch.begin();
            if (!isBallInHole) {
                golfBallController.updatePosition(golfBallBody);
                if (isBallStopped()) {
                    powerBallController.updatePosition(golfBallBody);
                    arrowController.updatePosition(golfBallBody);
                }

                if (holeShotCounter < parValues[holeNumber - 1]) {
                    font.setColor(Color.GREEN);
                } else if (holeShotCounter == parValues[holeNumber - 1]) {
                    font.setColor(Color.YELLOW);
                } else {
                    font.setColor(Color.RED);
                }
                font.draw(batch, String.valueOf(holeShotCounter), camera.viewportWidth / 2f + (camera.position.x - camera.viewportWidth / 2f), camera.viewportHeight / 2f + camera.position.y);
            } else {
                if (!playInHoleSoundOnce && !isMute) {
                    inHoleSound.play();
                    playInHoleSoundOnce = true;
                }
                golfBallBody.setLinearVelocity(0f, 0f);
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
    }

    public void updatePhysics(float dt) {
        inputUpdate(dt);
        if (!isPaused) {
            b2dr.render(world, camera.combined.scl(PPM));
            world.step(1 / 60f, 6, 2);
            updateGolfBallPosition(dt);
            updateCamera(dt);
            checkIfBallInHole(dt);
            tmr.setView(camera);
            batch.setProjectionMatrix(camera.combined);
        }
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
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                this.createHole();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.P)) {
                if (isPaused) {
                    resumeGame();
                } else {
                    pauseGame();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()) {
                goToNewHole();
            }
        }
    }

    private void goToNewHole() {
        playInHoleSoundOnce = false;
        isBallInHole = false;
        totalShotCounter += holeShotCounter;
        perHoleScore.put(holeNumber - 1, holeShotCounter);
        holeShotCounter = 0;
        arrowView.resetAngle();
        holeNumber++;
        if (holeNumber == 10) {
            endScreen = new EndScreen(this.game, perHoleScore);
            endScreen.initScene();
            this.game.setScreen(endScreen);
        } else {
            this.mapFile = "Maps/Hole" + holeNumber + ".tmx";
            this.createHole();
        }
    }


    private boolean isBallStopped() {
        float speedThreshold = .015f;
        float angularThreshold = .015f;
        float recentLinear = 0;
        float linearNow = golfBallBody.getLinearVelocity().len();
        recentLinear = 0.1f * linearNow + 0.9f * recentLinear;

        float recentAngular = 0;
        float angularNow = golfBallBody.getAngularVelocity();
        recentAngular = 0.1f * angularNow + 0.9f * recentAngular;

        if (recentLinear < speedThreshold && recentAngular < angularThreshold) {
            golfBallBody.setLinearVelocity(0, 0);
            golfBallBody.setAngularVelocity(0);
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
                if (!isMute) {
                    float volume = powerBallController.getSize() / 100;
                    hitSound.play(volume);
                }
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

    public void setIsMute(Boolean isMute){
        this.isMute = isMute;
    }

    private void addScoreToDB(int score, String name) {
        @SuppressWarnings("AuthLeak") String uri = "mongodb+srv://InfiniteGolfDev:InfiniteGolfDev@cluster0.ilveng7.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("golfScores");
            MongoCollection<Document> collection = database.getCollection("golfScores");
            // Create a document with the number field
            Document doc = new Document("name", name).append("score", score);
            // Insert the document into the collection
            collection.insertOne(doc);
            System.out.println("Number inserted successfully.");
        }
    }

    @SuppressWarnings("NewApi")
    private List<PlayerModel> getScoresFromDB() {
        @SuppressWarnings("AuthLeak") String uri = "mongodb+srv://InfiniteGolfDev:InfiniteGolfDev@cluster0.ilveng7.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        List<PlayerModel> leaderboard = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("golfScores");
            MongoCollection<Document> collection = database.getCollection("golfScores");
            try (MongoCursor<Document> cursor = collection.find().iterator()) {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    // Extract name and score from each document
                    String name = doc.getString("name");
                    int score = doc.getInteger("score");
                    // Create a Player object and add it to the list
                    leaderboard.add(new PlayerModel(name, score));
                }
            }
        }
        leaderboard.sort(Comparator.comparingInt(PlayerModel::getScore));
        return leaderboard;
    }
}
