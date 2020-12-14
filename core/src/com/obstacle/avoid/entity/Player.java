package com.obstacle.avoid.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.obstacle.avoid.config.GameConfig;

/**
 * Created by goran on 23/08/2016.
 */
public class Player extends GameObjectBase {

    private static final float BOUNDS_RADIUS = 0.4f; // world units
    private static final float SIZE = 2 * BOUNDS_RADIUS;

    public Player() {
        super(BOUNDS_RADIUS);
    }

    public void update() {
        float xSpeed = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xSpeed = GameConfig.MAX_PLAYER_X_SPEED;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xSpeed = -GameConfig.MAX_PLAYER_X_SPEED;
        }

        setX(getX() + xSpeed);
        updateBounds();
    }


    public float getWidth() {
        return SIZE;
    }

    public float getHeight() {
        return SIZE;
    }
}