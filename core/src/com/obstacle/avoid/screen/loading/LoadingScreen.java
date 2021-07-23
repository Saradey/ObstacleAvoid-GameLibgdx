package com.obstacle.avoid.screen.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obstacle.avoid.ObstacleAvoidGame;
import com.obstacle.avoid.assets.AssetDescriptors;
import com.obstacle.avoid.config.GameConfig;
import com.obstacle.avoid.screen.game.GameScreen;
import com.obstacle.avoid.screen.menu.MenuScreen;

/**
 * Created by goran on 31/08/2016.
 * экран загрузки
 */
public class LoadingScreen extends ScreenAdapter {

    // == constants ==
    private static final float PROGRESS_BAR_WIDTH = GameConfig.HUD_WIDTH / 2f; // world units
    private static final float PROGRESS_BAR_HEIGHT = 60f; // world units

    // == attributes ==
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private float progress;
    private float waitTime = 0.75f;

    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;
    private boolean changeScreen = false;

    // == constructors ==
    public LoadingScreen(ObstacleAvoidGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    // == public methods ==
    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        assetManager.load(AssetDescriptors.GAME_PLAY);
        assetManager.load(AssetDescriptors.FONT);
        assetManager.load(AssetDescriptors.UI);
        assetManager.load(AssetDescriptors.UI_SKIN);
    }


    @Override
    public void render(float delta) {
        update(delta);

        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(renderer == null) {
            return;
        }

        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);
        //закрашивания внутри примитивов
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.WHITE);

        draw();

        renderer.end();

        if(changeScreen) {
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }


    @Override
    public void hide() {
        // NOTE: Screens are not auto disposed.
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    // == private methods ==
    private void update(float delta) {
        // progress is between 0 and 1
        progress = assetManager.getProgress();

        //update загрузка ресурсов
        if (assetManager.update()) {
            waitTime -= delta;

            if (waitTime <= 0) {
                changeScreen = true;
            }
        }
    }

    private void draw() {
        float progressBarX = (GameConfig.HUD_WIDTH - PROGRESS_BAR_WIDTH) / 2f;
        float progressBarY = (GameConfig.HUD_HEIGHT - PROGRESS_BAR_HEIGHT) / 2f;

        renderer.rect(progressBarX, progressBarY,
                progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT
        );
    }

    private static void waitMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
