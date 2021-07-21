package com.obstacle.avoid.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by goran on 30/08/2016.
 */
public class AssetDescriptors {

    //дескрипторы для описания ресурсов
    public static final AssetDescriptor<BitmapFont> FONT =
            new AssetDescriptor<BitmapFont>(AssetPaths.UI_FONT, BitmapFont.class);

    //дескрипторы для описания ресурсов
    public static final AssetDescriptor<TextureAtlas> GAME_PLAY =
            new AssetDescriptor<TextureAtlas>(AssetPaths.GAME_PLAY, TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> UI =
            new AssetDescriptor<TextureAtlas>(AssetPaths.UI, TextureAtlas.class);


    private AssetDescriptors() {
    }
}