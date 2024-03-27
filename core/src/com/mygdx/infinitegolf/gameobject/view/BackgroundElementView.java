package com.mygdx.infinitegolf.gameobject.view;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.infinitegolf.gameobject.model.BackgroundElementModel;
import com.mygdx.infinitegolf.utils.Assets;


public class BackgroundElementView extends GameObjectView {
    private BackgroundElementModel model;
    private Sprite staticImg;

    public BackgroundElementView(BackgroundElementModel model, SpriteBatch batch) {
        super(batch);
        this.model = model;
        this.staticImg = new Sprite(Assets.getTexture("Maps/sky.jpeg"));
    }

    public void render(float dt) {
        batch.draw(this.staticImg, this.model.getPosition().x, this.model.getPosition().y, this.model.getDimensions().x, this.model.getDimensions().y);
    }

    public short getLayer() {
        return this.model.getLayer();
    }
}