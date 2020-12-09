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

    //центр экрана по размеру мира
    public static final float WORLD_CENTER_X = WORLD_WIDTH / 2f;
    //центр экрана по размеру мира
    public static final float WORLD_CENTER_Y = WORLD_HEIGHT / 2f;

    public static final float OBSTACLE_SPAWN_TIME = 0.25f;

    private GameConfig() {
    }
}
