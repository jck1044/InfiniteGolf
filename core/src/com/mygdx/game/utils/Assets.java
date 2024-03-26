package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {

    private static AssetManager assetManager;

    public static void init() {
        if (assetManager == null) {
            assetManager = new AssetManager();
            assetManager.setErrorListener(new AssetsErrorHandler());
        } else
            assetManager.clear();
    }

    public static void loadAll() {
        assetManager.finishLoading();
    }


    public static Texture getTexture(String name) {
        Texture tex = null;
        if (assetManager.isLoaded(name)) {
            // texture is available, let's fetch it and do do something interesting
            tex = assetManager.get(name, Texture.class);
        }
        return tex;
    }

    public static TextureAtlas getTextureAtlas(String name) {
        TextureAtlas tex = null;
        if (assetManager.isLoaded(name)) {
            // texture is available, let's fetch it and do do something interesting
            tex = assetManager.get(name, TextureAtlas.class);
        }
        return tex;
    }

    public static void addTexture(String name) {
        assetManager.load(name, Texture.class);
    }

    public static void addTextureAtlas(String name) {
        assetManager.load(name, TextureAtlas.class);
    }

    public static void dispose() {
        assetManager.dispose();
    }


}

class AssetsErrorHandler implements AssetErrorListener {
    public static final String TAG = AssetsErrorHandler.class.getName();

    @SuppressWarnings("rawtypes")
    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load Asset '" + asset.fileName + "'", (Exception) throwable);
    }
}
