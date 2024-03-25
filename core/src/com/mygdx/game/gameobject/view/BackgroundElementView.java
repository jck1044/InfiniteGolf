package com.mygdx.game.gameobject.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.gameobject.model.BackgroundElement;
import com.mygdx.game.utils.Assets;


public class BackgroundElementView extends GameObjectView {
    private BackgroundElement model;
    private Sprite staticImg;

    public BackgroundElementView(BackgroundElement model, SpriteBatch batch) {
        super(batch);
        this.model = model;
        this.staticImg = new Sprite(Assets.getTexture("textures/sky.jpeg"));
    }

    public void render(float dt) {
        batch.draw(this.staticImg, this.model.getPosition().x, this.model.getPosition().y, this.model.getDimensions().x, this.model.getDimensions().y);
    }

    public short getLayer() {
        return this.model.getLayer();
    }
}
