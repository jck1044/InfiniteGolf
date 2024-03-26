package com.mygdx.game.gameobject.model;

import static Utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

public class GolfBallModel extends DynamicGameObjectModel {
    private Vector2 position; // vector for x, y location of the ship in the game world
    private float speed = 200F;
    private short layer = 0;

    private Body golfBallBody;
    private TiledMap map;
    private Vector2 movementVector = new Vector2(0, 0);
    private World world;


    public GolfBallModel(Vector2 position, TiledMap map, World world) {
        super(position);
        this.map = map;
        this.world = world;
        this.createBall();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void updatePosition(float dt) {
        Vector2 posChange = this.movementVector.cpy().scl(speed * dt);
        this.position.add(posChange);
    }

    public void createBall() {
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
    }

    public Body getBody() {
        return golfBallBody;
    }

    public short getLayer() {
        return layer;
    }


//    public float getRadius() {
//        return radius;
//    }

    public Vector2 getMovementVector() {
        return movementVector;
    }

    public void setMovementVector(Vector2 movementVector) {
        this.movementVector = movementVector;
    }

}
