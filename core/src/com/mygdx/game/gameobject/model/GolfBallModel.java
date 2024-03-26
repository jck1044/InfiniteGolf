package com.mygdx.game.gameobject.model;

import static Utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

    private Vector2 movementVector = new Vector2(0, 0);


    public GolfBallModel(Vector2 position) {
        super(position);
    }

    public Vector2 getPosition() {
        return position;
    }

    public short getLayer() {
        return layer;
    }

    public Vector2 getMovementVector() {
        return movementVector;
    }

    public void setMovementVector(Vector2 movementVector) {
        this.movementVector = movementVector;
    }

}
