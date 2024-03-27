package com.mygdx.infinitegolf.gameobject.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameObjectView {
    protected SpriteBatch batch;

    public GameObjectView(SpriteBatch batch) {
        this.batch = batch;
    }

    public abstract void render(float dt);

    public short getLayer() {
        return Short.MAX_VALUE;
    }
}