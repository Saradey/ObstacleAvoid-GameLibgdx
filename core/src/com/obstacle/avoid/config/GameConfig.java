package com.obstacle.avoid.config;

//тут будут находится только константы
public class GameConfig {

    //размер экрана в пикселях
    public static final float WIDTH = 480f;
    //размер экрана в пикселях
    public static final float HEIGHT = 800f;

    //размеры игрового мира
    public static final float WORLD_WIDTH = 6f;
    //размеры игрового мира
    public static final float WORLD_HEIGHT = 10f;


    public static final float HUD_WIDTH = 480f; // world units
    public static final float HUD_HEIGHT = 800f; // world units

    //центр экрана по размеру мира
    public static final float WORLD_CENTER_X = WORLD_WIDTH / 2f;
    //центр экрана по размеру мира
    public static final float WORLD_CENTER_Y = WORLD_HEIGHT / 2f;

    public static final float OBSTACLE_SPAWN_TIME = 0.25f;
    public static final float MAX_PLAYER_X_SPEED = 0.25f;

    public static final int LIVES_START = 3;

    public static final float SCORE_MAX_TIME = 1.25f; // add score every interval


    public static final float EASY_OBSTACLE_SPEED = 0.1f;
    public static final float MEDIUM_OBSTACLE_SPEED = 0.15f;
    public static final float HARD_OBSTACLE_SPEED = 0.18f;

    private GameConfig() {
    }
}
