package com.obstacle.avoid.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.obstacle.avoid.config.GameConfig;

import static com.obstacle.avoid.config.GameConfig.PLAYER_BOUNDS_RADIUS;
import static com.obstacle.avoid.config.GameConfig.PLAYER_SIZE;

/**
 * Created by goran on 23/08/2016.
 */
public class Player extends GameObjectBase {

    public Player() {
        super(PLAYER_BOUNDS_RADIUS);
        setSize(PLAYER_SIZE, PLAYER_SIZE);
    }

}