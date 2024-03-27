package com.mygdx.infinitegolf.gameobject.model;

import com.badlogic.gdx.math.Vector2;

public class ArrowModel extends DynamicGameObjectModel {

    private short layer = 0;

    public ArrowModel(Vector2 position) {
        super(position);
    }

    public Vector2 getPosition() {
        return position;
    }

    public short getLayer() {
        return layer;
    }
}
