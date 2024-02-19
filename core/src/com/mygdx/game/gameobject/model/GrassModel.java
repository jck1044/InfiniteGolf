package com.mygdx.game.gameobject.model;

import com.badlogic.gdx.math.Vector2;
public class GrassModel extends GameObjectModel {
    private short layer = 1;
    private final Vector2 dimensions;

    public GrassModel(Vector2 position, Vector2 dimensions, short layer) {
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
