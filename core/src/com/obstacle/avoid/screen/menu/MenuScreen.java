package com.obstacle.avoid.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.obstacle.avoid.ObstacleAvoidGame;
import com.obstacle.avoid.assets.AssetDescriptors;
import com.obstacle.avoid.assets.RegionNames;
import com.obstacle.avoid.screen.game.GameScreen;

public class MenuScreen extends MenuScreenBase {

    private static final Logger log = new Logger(MenuScreen.class.getName(), Logger.DEBUG);

    public MenuScreen(ObstacleAvoidGame game) {
        super(game);
    }

    @Override
    protected Actor createUi() {
        Skin uiSkin = assetManager.get(AssetDescriptors.UI_SKIN);
        Table table = new Table();
        TextureRegion textureRegion = assetManager.get(AssetDescriptors.GAME_PLAY)
                .findRegion("background");
        table.setBackground(new TextureRegionDrawable(textureRegion));

        // play button
        TextButton playButton = new TextButton("PLAY", uiSkin);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                play();
            }
        });

        // high score button
        TextButton highScoreButton = new TextButton("HIGHSCORE", uiSkin);
        highScoreButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showHighScore();
            }
        });

        // options button
        TextButton optionsButton = new TextButton("OPTIONS", uiSkin);
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showOptions();
            }
        });

        // quit button
        TextButton quitButton = new TextButton("QUIT", uiSkin);
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                quit();
            }
        });

        // setup table
        Table buttonTable = new Table(uiSkin);
        buttonTable.defaults().pad(20);
        buttonTable.setBackground(RegionNames.PANEL);

        buttonTable.add(playButton).row();
        buttonTable.add(highScoreButton).row();
        buttonTable.add(optionsButton).row();
        buttonTable.add(quitButton);

        buttonTable.center();

        table.add(buttonTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }

    private void play() {
        log.debug("play()");
        game.setScreen(new GameScreen(game));
    }

    private void showHighScore() {
        log.debug("showHighScore()");
        game.setScreen(new HighScoreScreen(game));
    }

    private void showOptions() {
        log.debug("showOptions()");
        game.setScreen(new OptionsScreen(game));
    }

    private void quit() {
        log.debug("quit()");
        Gdx.app.exit();
    }
}