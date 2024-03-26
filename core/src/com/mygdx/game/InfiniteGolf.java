package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.scene.Hole;
import com.mygdx.game.utils.Assets;


public class InfiniteGolf extends Game {
    SpriteBatch batch;
    private Hole hole;

    @Override
    public void create() {
        Assets.init();
        hole = new Hole(this, "Maps/Hole1.tmx");
        hole.initScene();
        setScreen(hole);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0.325f, 0.576f, 0.867f, 1); // Set clear color to our sky's blue
        this.getScreen().render(dt);
        hole.updateScene(dt);
    }

    @Override
    public void dispose() {
        this.getScreen().dispose();
        Assets.dispose();
//        ballTexture.dispose();
//        world.dispose();
//        b2dr.dispose();
//        tmr.dispose();
//        map.dispose();
//        font.dispose();
    }

    /*
    public Body createBox(float x, float y, float width, float height, boolean isStatic) {
        Body body;
        BodyDef def = new BodyDef();
        if (isStatic) def.type = BodyDef.BodyType.StaticBody;
        else def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true; // TODO change to false for rotaion
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape(); // TODO change to circle
        shape.setAsBox((float) 32 / 2 / PPM, (float) 32 / 2 / PPM);

        body.createFixture(shape, 1.0f);
        shape.dispose();
        return body;
    }
    */
}
