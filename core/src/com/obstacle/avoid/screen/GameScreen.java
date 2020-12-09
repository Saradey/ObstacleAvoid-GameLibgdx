package com.obstacle.avoid.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obstacle.avoid.config.GameConfig;
import com.obstacle.avoid.entity.Player;
import com.obstacle.avoid.utils.ViewportUtils;
import com.obstacle.avoid.utils.debug.DebugCameraController;

public class GameScreen implements Screen {

    private static final Logger log = new Logger(GameScreen.class.getName(), Logger.DEBUG);

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;
    private Player player;
    private DebugCameraController debugCameraController;

    //используем его для инициализации нашей игры и загружайте ресурсы
    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        // create player
        player = new Player();
        // calculate position
        float startPlayerX = GameConfig.WORLD_WIDTH / 2;
        float startPlayerY = 1;
        // position player
        player.setPosition(startPlayerX, startPlayerY);

        // create debug camera controller
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
    }

    @Override
    public void render(float delta) {
        //camera control
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        // update world
        update(delta);

        //clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render debug
        renderDebug();
    }

    private void update(float delta) {
        updatePlayer();
    }

    private void updatePlayer() {
        player.update();
    }

    private void renderDebug() {
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        drawDebug();
        renderer.end();

        ViewportUtils.drawGrid(viewport, renderer);
    }

    private void drawDebug() {
        player.drawDebug(renderer);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        ViewportUtils.debugPixelPerUnit(viewport);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    //вызывается когда мы прячим скрин к примеру когда мы переходим из одного экрана к другому
    @Override
    public void hide() {
        dispose();
    }

    //не вызывается автоматически
    @Override
    public void dispose() {
        renderer.dispose();
    }
}