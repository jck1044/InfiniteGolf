package com.mygdx.game.gameobject.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.gameobject.model.GrassModel;


public class GrassView extends GameObjectView {
    private GrassModel grass;
    private Sprite staticImg;

    public GrassView(GrassModel grass, SpriteBatch batch) {
        super(batch);
        this.grass = grass;
        this.staticImg = new Sprite(new Texture("textures/grass.png"));
    }

    public void render(float dt) {
        batch.draw(this.staticImg, this.grass.getPosition().x, this.grass.getPosition().y, this.grass.getDimensions().x, this.grass.getDimensions().y);
    }

    public short getLayer() {
        return this.grass.getLayer();
    }
}
