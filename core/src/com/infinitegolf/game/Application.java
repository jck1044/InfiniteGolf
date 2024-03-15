package com.infinitegolf.game;

import static Utils.Constants.PPM;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;

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
    private float horizontalForce = 0;
    private float verticalForce = 0;
    //    private float power = 0;
    private Boolean powerUp = true;
    private float powerBallSize = golfBallSize;
    private final float arrowSize = 32;
    private float ballAngle;

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
        ScreenUtils.clear(0, 0, 0, 1);

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
        arrowSprite.setX(golfBall.getPosition().x * PPM - ((ballTexture.getWidth() * (arrowSize / golfBallSize)) / 2));
        arrowSprite.setY(golfBall.getPosition().y * PPM - ((ballTexture.getHeight() * (arrowSize / golfBallSize)) / 2));
        arrowSprite.setSize(arrowSize, arrowSize);
//        arrowSprite.setOrigin(((golfBall.getPosition().x * PPM) - ((float) ballTexture.getWidth() / 2)), (golfBall.getPosition().y * PPM));
//        arrowSprite.setOrigin(golfBall.getPosition().x + ((float) ballTexture.getWidth() / 1), golfBall.getPosition().y + ((float) ballTexture.getHeight() / 1));
//        arrowSprite.rotate90(false);
//        arrowSprite.rotate90(false);
        arrowSprite.draw(batch);


        ballSprite.draw(batch);
        //		batch.draw(ballTexture,golfBall.getPosition().x * PPM - (ballTexture.getWidth()/2),golfBall.getPosition().y * PPM - (ballTexture.getHeight()/2));
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
    }

    public void updateGame(float delta) {
        world.step(1 / 60f, 6, 2);
        inputUpdate(delta);
        updateCamera(delta);
        tmr.setView(camera);
        batch.setProjectionMatrix(camera.combined);
    }

    public void inputUpdate(float delta) {
        if (Gdx.input.isKeyPressed((Input.Keys.LEFT))) {
            horizontalForce -= 1;

        }
        if (Gdx.input.isKeyPressed((Input.Keys.RIGHT))) {
            horizontalForce += 1;
        }
//        if (Gdx.input.isKeyJustPressed((Input.Keys.SPACE))) {
//            golfBall.applyForceToCenter(0, 500, false);
//        }


//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && horizontalForce == 0) {
//            if (redBallX - ballX > -25) {
//                redBallX -= 2;
//                if (redBallX - ballX - 5 > 0) {
//                    redBallY += 2;
//                } else if (redBallX - ballX < 0) {
//                    redBallY -= 2;
//                }
//                up = (redBallY - ballY - 5) / 25;
//                right = (redBallX - ballX - 5) / 30;
//            }
//        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && horizontalForce == 0) {
//            if (redBallX - ballX < 35) {
//                redBallX += 2;
//                if (redBallX - ballX - 10 > 0) {
//                    redBallY -= 2;
//                } else if (redBallX - ballX - 5 < 0) {
//                    redBallY += 2;
//                }
//                up = (redBallY - ballY - 5) / 25;
//                right = (redBallX - ballX - 5) / 30;
//            }
//        } else
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && horizontalForce <= .1) {
            horizontalForce = 0;
//            ballAngle = 0;
            if (powerUp) {
                if (powerBallSize >= (golfBallSize * 8)) {
                    powerUp = false;
                } else {
                    powerBallSize *= 1.15f;
                }
            } else {
                if (powerBallSize <= (golfBallSize + 1)) {
                    powerBallSize = golfBallSize;
                    powerUp = true;
                } else {
                    powerBallSize *= 0.85f;
                }
            }
        }

//        if (horizontalForce > 0) {
//            ballX += (movingPower * right);
//            ballY += (movingPower * up);
//            redBallX = -10;
//            redBallY = -10;
//            movingPower = (float) (movingPower * 0.96);
//            if (movingPower <= .5 && ballY <= 250.01) {
//                movingPower = 0;
//                velocityY = 0;
//                ballY = 250;
//                redBallX = ballX + 35;
//                redBallY = ballY + 5;
//                up = 0;
//                right = 1;
//            }
//        }
//        if (power > 0) {
//            movingPower = (float) (power * 1.6);
//            power = 0;
//        }


        golfBall.setLinearVelocity(horizontalForce * 5, golfBall.getLinearVelocity().y);
    }

    public void updateCamera(float delta) {
        Vector3 position = camera.position;
        position.x = golfBall.getPosition().x * PPM;
        position.y = (golfBall.getPosition().y + 2) * PPM;

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
