package com.obstacle.avoid.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.obstacle.avoid.config.DifficultyLevel;
import com.obstacle.avoid.config.GameConfig;
import com.obstacle.avoid.entity.Background;
import com.obstacle.avoid.entity.Obstacle;
import com.obstacle.avoid.entity.Player;

/**
 * Created by goran on 27/08/2016.
 */
public class GameController {

    // == constants ==
    private static final Logger log = new Logger(GameController.class.getName(), Logger.DEBUG);

    // == attributes ==
    private Player player;
    //специальный класс для использования вместо стандартных Java коллекций, таких как ArrayList.
    // Проблема с ними в том, что они производят мусор различными способами. Класс Array
    // пытается минимизировать мусор в максимально возможной степени.
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private float obstacleTimer;
    private float scoreTimer;
    private int lives = GameConfig.LIVES_START;
    private int score;
    private int displayScore;
    private DifficultyLevel difficultyLevel = DifficultyLevel.EASY;
    //Создает объект и если он не нужен, высвобождает но держит в памяти
    //таким образом мы не трахаем garbage collector
    private Pool<Obstacle> obstaclePool;
    private Background background;
    private final float startPlayerX = (GameConfig.WORLD_WIDTH - GameConfig.PLAYER_SIZE) / 2f;
    private final float startPlayerY = 1 - (GameConfig.PLAYER_SIZE / 2);

    // == constructors ==
    public GameController() {
        init();
    }

    // == init ==
    private void init() {
        // create player
        player = new Player();

        // position player
        player.setPosition(startPlayerX, startPlayerY);

        // create obstacle pool
        obstaclePool = Pools.get(Obstacle.class, 40);

        // create background
        background = new Background();
        background.setPosition(0, 0);
        background.setSize(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
    }

    // == public methods ==
    public void update(float delta) {
        if (isGameOver()) {
            return;
        }

        updatePlayer();
        updateObstacles(delta);
        updateScore(delta);
        updateDisplayScore(delta);

        if (isPlayerCollidingWithObstacle()) {
            log.debug("Collision detected.");
            lives--;

            if (isGameOver()) {
                log.debug("Game over");
            } else {
                restart();
            }
        }
    }

    private void restart() {
        obstaclePool.freeAll(obstacles);
        obstacles.clear();
        player.setPosition(startPlayerX, startPlayerY);
    }

    // == private methods ==
    public boolean isGameOver() {
        return lives <= 0;
    }

    private boolean isPlayerCollidingWithObstacle() {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.isNotHit() && obstacle.isPlayerColliding(player)) {
                return true;
            }
        }
        return false;
    }

    private void updatePlayer() {
        float xSpeed = 0;
        //для проверки нажатия
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xSpeed = GameConfig.MAX_PLAYER_X_SPEED;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xSpeed = -GameConfig.MAX_PLAYER_X_SPEED;
        }
        player.setX(player.getX() + xSpeed);
        player.updateBounds();
        blockPlayerFromLeavingTheWorld();
    }

    private void blockPlayerFromLeavingTheWorld() {
        //игрок не может выйти за пределы мира
        float playerX = MathUtils.clamp(
                player.getX(), // value
                0, // min
                GameConfig.WORLD_WIDTH - player.getWidth() // max
        );

        player.setPosition(playerX, player.getY());
    }

    private void updateObstacles(float delta) {
        for (Obstacle obstacle : obstacles) {
            obstacle.update();
        }

        createNewObstacle(delta);
        removePassedObstacles();
    }

    private void removePassedObstacles() {
        if (obstacles.size > 0) {
            Obstacle first = obstacles.first();
            if (first.getY() < -GameConfig.OBSTACLE_SIZE) {
                //удаляем Obstacle если он вышел за пределы игрового экрана
                obstacles.removeValue(first, true);
                obstaclePool.free(first);
            }
        }
    }

    private void createNewObstacle(float delta) {
        obstacleTimer += delta;

        if (obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME) {
            float min = 0;
            float max = GameConfig.WORLD_WIDTH - GameConfig.OBSTACLE_SIZE;
            float obstacleX = MathUtils.random(min, max);
            float obstacleY = GameConfig.WORLD_HEIGHT;

            //Возвращает объект из этого пула. Объект может быть новым или повторно использованным
            //что бы не дрочить GC
            Obstacle obstacle = obstaclePool.obtain();
            obstacle.setySpeed(difficultyLevel.getObstacleSpeed());
            obstacle.setPosition(obstacleX, obstacleY);

            //из массива удаляется но ссылка остается в пуле
            obstacles.add(obstacle);
            obstacleTimer = 0f;
        }
    }

    private void updateScore(float delta) {
        scoreTimer += delta;

        if (scoreTimer >= GameConfig.SCORE_MAX_TIME) {
            score += MathUtils.random(1, 5);
            scoreTimer = 0.0f;
        }
    }

    private void updateDisplayScore(float delta) {
        //для плавного начисление очков бонуса
        if (displayScore < score) {
            displayScore = Math.min(
                    score,
                    displayScore + (int) (60 * delta)
            );
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

    public int getLives() {
        return lives;
    }

    public int getDisplayScore() {
        return displayScore;
    }

    public Background getBackground() {
        return background;
    }

    public void restartGame() {
        restart();
        obstacleTimer = 0;
        scoreTimer = 0;
        lives = GameConfig.LIVES_START;
        score = 0;
        displayScore = 0;
        difficultyLevel = DifficultyLevel.EASY;
    }
}
