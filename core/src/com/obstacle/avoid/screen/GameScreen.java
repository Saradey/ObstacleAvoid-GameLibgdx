package com.obstacle.avoid.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {

    SpriteBatch batch;
    Texture img;

    //используем его для инициализации нашей игры и загружайте ресурсы
    @Override
    public void show() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    //вызывается когда мы прячим скрин к примеру когда мы переходим из одного экрана к другому
    @Override
    public void hide() {
        dispose();
    }

    //не вызывается автоматически
    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
