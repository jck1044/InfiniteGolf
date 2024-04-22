package com.mygdx.infinitegolf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.infinitegolf.scene.Hole;
import com.mygdx.infinitegolf.scene.MainMenu;
import com.mygdx.infinitegolf.utils.Assets;


public class InfiniteGolf extends Game {
    SpriteBatch batch;
    private Hole hole;

    private MainMenu menu;

    @Override
    public void create() {
        Assets.init();
        menu = new MainMenu(this);
        menu.initScene();
        setScreen(menu);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        this.getScreen().render(dt);
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

}
