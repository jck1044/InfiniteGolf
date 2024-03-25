package com.infinitegolf.game;

import static Utils.Constants.PPM;

import com.badlogic.gdx.ApplicationAdapter;
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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

import Utils.TileObjectUtil;

public class Application extends ApplicationAdapter {
    private final float golfBallSize = 16;
    private boolean DEBUG = false;
    private final float SCALE = 2.5f;
    SpriteBatch batch;
    Texture ballTexture;
    Texture arrowTexture;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tmr;
    private TiledMap map;
    private Box2DDebugRenderer b2dr;
    private World world;
    private Body golfBall, platform;
    private Boolean powerUp = true;
    private float powerBallSize = golfBallSize;
    private final float arrowSize = 32;
    private float ballAngle = 0;
    private float arrowAngle = 0;
    private float ballPower = 0;
    private int shotCounter = 0;
    private BitmapFont font;
    private int par = 3;


    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / SCALE, h / SCALE); //From YT
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        world = new World(new Vector2(0, -10), false);
        b2dr = new Box2DDebugRenderer();

        batch = new SpriteBatch();
        font = new BitmapFont();
        ballTexture = new Texture("Images/GolfBall.png");
        arrowTexture = new Texture("Images/Arrow.png");

        map = new TmxMapLoader().load("Maps/Hole1.tmx");
        tmr = new OrthogonalTiledMapRenderer(map); // Can pass in batch??? for shaders???

        TileObjectUtil.parseTileObjectLayer(world, map.getLayers().get("collision-layer").getObjects());

        golfBall = createBall();
//		platform = createBox(0,0,64, 32, true);
    }

    @Override
    public void render() {
        updateGame(Gdx.graphics.getDeltaTime());
        ScreenUtils.clear(0.325f, 0.576f, 0.867f, 1); // Set clear color to our sky's blue

        tmr.render();

        batch.begin();
        Sprite ballSprite = new Sprite(ballTexture);
        ballAngle = golfBall.getAngle() * (180 / (float) Math.PI);
        ballSprite.rotate(ballAngle);
        ballSprite.setX(golfBall.getPosition().x * PPM - ((float) ballTexture.getWidth() / 2));
        ballSprite.setY(golfBall.getPosition().y * PPM - ((float) ballTexture.getHeight() / 2));

        Sprite powerSprite = new Sprite(ballTexture);
//        powerSprite.setOrigin(ballSprite.getOriginX(), ballSprite.getOriginY());
        powerSprite.setX(golfBall.getPosition().x * PPM - ((ballTexture.getWidth() * (powerBallSize / golfBallSize)) / 2));
        powerSprite.setY(golfBall.getPosition().y * PPM - ((ballTexture.getHeight() * (powerBallSize / golfBallSize)) / 2));
        powerSprite.setSize(powerBallSize, powerBallSize);
        powerSprite.draw(batch);


        Sprite arrowSprite = new Sprite(arrowTexture);
        arrowSprite.setX(golfBall.getPosition().x * PPM);
        arrowSprite.setY(golfBall.getPosition().y * PPM - ((ballTexture.getHeight() * (arrowSize / golfBallSize)) / 2));
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

        b2dr.render(world, camera.combined.scl(PPM));
    }

    @Override
    public void dispose() {
        batch.dispose();
        ballTexture.dispose();
        world.dispose();
        b2dr.dispose();
        tmr.dispose();
        map.dispose();
        font.dispose();
    }

    public void updateGame(float delta) {
        world.step(1 / 60f, 6, 2);
        inputUpdate(delta);
        updateGolfBallPosition(delta);
        updateCamera(delta);
        tmr.setView(camera);
        batch.setProjectionMatrix(camera.combined);
    }

    public void updateGolfBallPosition(float delta) {
        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (powerBallSize > golfBallSize) {
                float horizontalForce = (90 - arrowAngle) * powerBallSize / 3;
                float verticalForce = getVerticalForce(arrowAngle) * powerBallSize / 5;
                golfBall.applyForceToCenter(horizontalForce, verticalForce, false);
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
        boolean isBallOnFloor = golfBall.getPosition().y < 6.32; //fixme: this value is hardcoded for map 1, may not work for others
        boolean doesBallHaveNoVelocity = golfBall.getLinearVelocity().x <= 0.2 && golfBall.getLinearVelocity().y <= 0.2;
        return isBallOnFloor && doesBallHaveNoVelocity;
    }

    public void updateCamera(float delta) {
        Vector3 position = camera.position;
        position.x = golfBall.getPosition().x * PPM;
        position.y = (golfBall.getPosition().y + 3.1f) * PPM;

//		Rectangle bounds = tmr.getViewBounds();
        float mapWidth = tmr.getViewBounds().width;
        float mapHeight = tmr.getViewBounds().height;
        if (tmr != null) {

        } else {
            camera.position.set(position);
        }

        camera.update();
    }


    public Body createBall() {
        MapProperties ballProperties = map.getLayers().get("ball")
                .getObjects().get(0).getProperties();
        float x = (float) ballProperties.get("x");
        float y = (float) ballProperties.get("y");

        Body golfBallBody;
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
        return golfBallBody;
    }

    public Body createBox(float x, float y, float width, float height, boolean isStatic) {
        Body body;
        BodyDef def = new BodyDef();
        if (isStatic) def.type = BodyDef.BodyType.StaticBody;
        else def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true; // TODO change to false for rotaion
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape(); // TODO change to circle
        shape.setAsBox((float) 32 / 2 / PPM, (float) 32 / 2 / PPM);

        body.createFixture(shape, 1.0f);
        shape.dispose();
        return body;
    }
}
