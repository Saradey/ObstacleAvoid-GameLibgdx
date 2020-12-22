package com.obstacle.avoid.screen.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.obstacle.avoid.ObstacleAvoidGame;
import com.obstacle.avoid.assets.AssetDescriptors;

/**
 * Created by goran on 27/08/2016.
 */
public class GameScreen implements Screen {

    private GameController controller;
    private GameRenderer renderer;

    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;

    public GameScreen(ObstacleAvoidGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        controller = new GameController();
        renderer = new GameRenderer(assetManager, controller);
    }

    @Override
    public void render(float delta) {
        if(!renderer.getGameIsPause()) {
            controller.update(delta);
            renderer.render(delta);
        }
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}