package com.obstacle.avoid.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Pool;
import com.obstacle.avoid.config.DifficultyLevel;

import static com.obstacle.avoid.config.GameConfig.OBSTACLE_BOUNDS_RADIUS;
import static com.obstacle.avoid.config.GameConfig.OBSTACLE_SIZE;

/**
 * Created by goran on 23/08/2016.
 */
public class Obstacle extends GameObjectBase implements Pool.Poolable {

    private float ySpeed = DifficultyLevel.MEDIUM.getObstacleSpeed();
    private boolean hit;

    public Obstacle() {
        super(OBSTACLE_BOUNDS_RADIUS);
        setSize(OBSTACLE_SIZE, OBSTACLE_SIZE);
    }

    public void update() {
        setY(getY() - ySpeed);
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

    @Override
    public void reset() {
        hit = false;
    }

}