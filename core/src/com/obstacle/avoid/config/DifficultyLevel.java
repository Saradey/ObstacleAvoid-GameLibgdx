package com.obstacle.avoid.config;

/**
 * Created by goran on 27/08/2016.
 */
public enum DifficultyLevel {
    EASY(GameConfig.EASY_OBSTACLE_SPEED),
    MEDIUM(GameConfig.MEDIUM_OBSTACLE_SPEED),
    HARD(GameConfig.HARD_OBSTACLE_SPEED);

    private final float obstacleSpeed;

    DifficultyLevel(float obstacleSpeed) {
        this.obstacleSpeed = obstacleSpeed;
    }

    public float getObstacleSpeed() {
        return obstacleSpeed;
    }

}
