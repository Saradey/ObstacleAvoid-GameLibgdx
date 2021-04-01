package com.obstacle.avoid;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Logger;
import com.obstacle.avoid.screen.loading.LoadingScreen;

//аналог Application в android
public class ObstacleAvoidGame extends Game {
    //менеджер ресурсов который умеет асинхронно загружать ресурсы
    //показывать прогресс загрузки и стопать поток пока ресурсы не загрузятся
    private AssetManager assetManager;

    @Override
    public void create() {
        //для дебагинга
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        assetManager = new AssetManager();
        assetManager.getLogger().setLevel(Logger.DEBUG);

        //так стартует первый скрин
        setScreen(new LoadingScreen(this));
    }


    @Override
    public void dispose() {
        //по завершению приложения нужно освобождать ресурсы
        assetManager.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
