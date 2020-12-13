package com.obstacle.avoid.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.obstacle.avoid.config.DifficultyLevel;

/**
 * Created by goran on 23/08/2016.
 */
public class Obstacle extends GameObjectBase {

    private static final float BOUNDS_RADIUS = 0.3f; // world units
    public static final float SIZE = 2 * BOUNDS_RADIUS;

    private float ySpeed = DifficultyLevel.EASY.getObstacleSpeed();
    private boolean hit = false;

    public Obstacle() {
        super(BOUNDS_RADIUS);
    }

    public void update() {
        setY(getY() - ySpeed);
    }

    public float getWidth() {
        return SIZE;
    }

    public boolean isPlayerColliding(Player player) {
        Circle playerBounds = player.getBounds();
        // check if playerBounds overlap obstacle bounds
        boolean overlaps = Intersector.overlaps(playerBounds, getBounds());
        hit = overlaps;
        return overlaps;
    }

    public boolean isNotHit() {
        return !hit;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public float getSize() {
        return SIZE;
    }
}