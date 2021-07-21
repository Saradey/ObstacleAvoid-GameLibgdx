package com.obstacle.avoid.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obstacle.avoid.assets.AssetDescriptors;
import com.obstacle.avoid.assets.RegionNames;
import com.obstacle.avoid.config.GameConfig;
import com.obstacle.avoid.entity.Background;
import com.obstacle.avoid.entity.Obstacle;
import com.obstacle.avoid.entity.Player;
import com.obstacle.avoid.utils.ViewportUtils;
import com.obstacle.avoid.utils.debug.DebugCameraController;

/**
 * Created by goran on 27/08/2016.
 */
public class GameRenderer implements Disposable, InputProcessor {

    // == attributes ==
    //камера которая предоставляет матрицу проекции саму игру
    private OrthographicCamera camera;
    //для расстягивания картинок и экрана самой игры
    private Viewport viewport;
    //для дебаг отрисовки примитивов
    private ShapeRenderer renderer;

    //для отрисовки ui
    private OrthographicCamera hudCamera;

    //для расстягивания экрана ui
    private Viewport hudViewport;

    //для отрисвоки текста
    private BitmapFont font;
    //для замеров размера текста
    private final GlyphLayout layout = new GlyphLayout();
    private DebugCameraController debugCameraController;
    private final GameController controller;

    //его предоставляет TextureAtlas, то есть TextureRegion это вырезка из общей текстуры
    private TextureRegion playerRegion;
    private TextureRegion obstacleRegion;
    private TextureRegion backgroundRegion;

    private boolean renderDebug = true;
    private boolean renderAssets = true;
    private boolean gameIsPause = false;

    private final AssetManager assetManager;

    private final SpriteBatch batch;

    // == constructors ==
    public GameRenderer(SpriteBatch batch, AssetManager assetManager, GameController controller) {
        this.assetManager = assetManager;
        this.controller = controller;
        this.batch = batch;
        init();
    }

    // == init ==
    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        hudCamera = new OrthographicCamera();
        //расстягивает так что если соотношения разные будут черные полосы внизу или верху
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, hudCamera);
        font = assetManager.get(AssetDescriptors.FONT);

        // create debug camera controller
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);

        //содержит в себе большую текстуру и может предоставлять регионы и спрайты
        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);

        playerRegion = gamePlayAtlas.findRegion(RegionNames.PLAYER);
        obstacleRegion = gamePlayAtlas.findRegion(RegionNames.OBSTACLE);
        backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);

        Gdx.input.setInputProcessor(this);
    }

    // == public methods ==
    public void render(float delta) {
        //возможно посмотреть сколько раз вызывалась выгрузка текстур в gpu
        batch.totalRenderCalls = 0;

        // not wrapping inside alive cuz we want to be able to control camera even when there is game over
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        if (Gdx.input.isTouched() && !controller.isGameOver()) {
            //берем нажатие координат на экран
            Vector2 screenTouch = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            //берем нажатие координат в игровом мире
            Vector2 worldTouch = viewport.unproject(screenTouch);

            Player player = controller.getPlayer();
            //перемещаем игрока при этом ограничиваем его размерами игрового мира
            worldTouch.x = MathUtils.clamp(worldTouch.x, player.getWidth() / 2, GameConfig.WORLD_WIDTH - player.getWidth() / 2) - player.getWidth() / 2;
            player.setX(worldTouch.x);
        }

        //отчищает скрин
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (renderAssets) {
            renderGamePlay();
        }

        // render ui/hud
        renderUi();

        // render debug graphics
        if (renderDebug) {
            renderDebug();
        }
//        System.out.println("batch.totalRenderCalls:" + batch.totalRenderCalls);
    }

    // == private methods ==
    private void renderGamePlay() {
        //Применяет видовой экран к камере и устанавливает glViewport.
        viewport.apply();
        //подготавливаем матрицу проекции
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // draw background
        Background background = controller.getBackground();
        batch.draw(backgroundRegion,
                background.getX(), background.getY(),
                background.getWidth(), background.getHeight()
        );

        // draw player
        Player player = controller.getPlayer();
        batch.draw(playerRegion,
                player.getX(), player.getY(),
                player.getWidth(), player.getHeight()
        );

        // draw obstacles
        for (Obstacle obstacle : controller.getObstacles()) {
            batch.draw(obstacleRegion,
                    obstacle.getX(), obstacle.getY(),
                    obstacle.getWidth(), obstacle.getHeight()
            );
        }
        batch.end();
    }

    public void resize(int width, int height) {
        //нужно обязщательно оповещать viewport об изменениях размеров экрана
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
        ViewportUtils.debugPixelPerUnit(viewport);
    }

    // == private methods ==
    private void renderUi() {
        //ЭТО ВАЖНО
        hudViewport.apply();
        //подготавливаем матрицу проекции
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        String livesText = "LIVES: " + controller.getLives();
        layout.setText(font, livesText);

        //отрисовка текста
        font.draw(batch, livesText,
                20,
                GameConfig.HUD_HEIGHT - layout.height
        );

        String scoreText = "SCORE: " + controller.getDisplayScore();
        layout.setText(font, scoreText);

        //отрисовка текста
        font.draw(batch, scoreText,
                GameConfig.HUD_WIDTH - layout.width - 20,
                GameConfig.HUD_HEIGHT - layout.height
        );

        batch.end();
    }

    private void renderDebug() {
        //ЭТО ВАЖНО
        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        drawDebug();

        renderer.end();

        ViewportUtils.drawGrid(viewport, renderer);
    }

    private void drawDebug() {
        controller.getPlayer().drawDebug(renderer);

        for (Obstacle obstacle : controller.getObstacles()) {
            obstacle.drawDebug(renderer);
        }
    }

    @Override
    public void dispose() {
        //обзательно освобождать ресурсы
        renderer.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        //обработка нажатий на клавиши не так часто срабатывает как если бы мы отлавливали
        //нажатия в методе render
        switch (keycode) {
            case Input.Keys.R:
                controller.restartGame();
                break;
            case Input.Keys.C:
                renderDebug = !renderDebug;
                break;

            case Input.Keys.V:
                renderAssets = !renderAssets;
                break;

            case Input.Keys.SPACE:
                gameIsPause = !gameIsPause;
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public boolean getGameIsPause() {
        return gameIsPause;
    }
}