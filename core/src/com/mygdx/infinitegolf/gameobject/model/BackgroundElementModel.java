package com.mygdx.infinitegolf.gameobject.model;

import com.badlogic.gdx.math.Vector2;

public class BackgroundElementModel extends GameObjectModel {
    private short layer = 0;
    private final Vector2 dimensions;

    public BackgroundElementModel(Vector2 position, Vector2 dimensions, short layer) {
        super(position);
        this.dimensions = dimensions;
        this.layer = layer;
    }

    public short getLayer() {
        return layer;
    }

    public Vector2 getDimensions() {
        return dimensions;
    }
}