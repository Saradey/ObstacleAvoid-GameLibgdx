package com.obstacle.avoid.screen.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.obstacle.avoid.ObstacleAvoidGame;
import com.obstacle.avoid.assets.AssetDescriptors;
import com.obstacle.avoid.screen.menu.MenuScreen;


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
        controller = new GameController(game);
        renderer = new GameRenderer(game.getBatch(), assetManager, controller);
    }

    @Override
    public void render(float delta) {
        if(!renderer.getGameIsPause()) {
            controller.update(delta);
            renderer.render(delta);
        }
        if(controller.isGameOver()) {
            game.setScreen(new MenuScreen(game));
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
        //всегда надо высвобождать ресурсы
        renderer.dispose();
    }
}