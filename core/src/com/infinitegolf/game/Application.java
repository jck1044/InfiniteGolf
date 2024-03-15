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
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
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
    private boolean DEBUG = false;
    private final float SCALE = 2.5f;
    SpriteBatch batch;
    Texture ballTexture;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tmr;
    private TiledMap map;
    private Box2DDebugRenderer b2dr;
    private World world;
    private Body golfBall, platform;

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
        float angle = golfBall.getAngle() * (180 / (float) Math.PI);
        ballSprite.rotate(angle);
        ballSprite.setX(golfBall.getPosition().x * PPM - (ballTexture.getWidth() / 2));
        ballSprite.setY(golfBall.getPosition().y * PPM - (ballTexture.getHeight() / 2));
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
        int horizontalForce = 0;

        if (Gdx.input.isKeyPressed((Input.Keys.LEFT))) {
            horizontalForce -= 1;
        }
        if (Gdx.input.isKeyPressed((Input.Keys.RIGHT))) {
            horizontalForce += 1;
        }
        if (Gdx.input.isKeyJustPressed((Input.Keys.SPACE))) {
            golfBall.applyForceToCenter(0, 500, false);
        }

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

    public Body createBox(int x, int y, int width, int height, boolean isStatic) {
        Body body;
        BodyDef def = new BodyDef();
        if (isStatic) def.type = BodyDef.BodyType.StaticBody;
        else def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true; // TODO change to false for rotaion
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape(); // TODO change to circle
        shape.setAsBox(32 / 2 / PPM, 32 / 2 / PPM);

        body.createFixture(shape, 1.0f);
        shape.dispose();
        return body;
    }
}
