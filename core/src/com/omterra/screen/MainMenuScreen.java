/*
 * The MIT License
 *
 * Copyright 2014 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.omterra.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.omterra.OmterraGame;
import com.omterra.io.FileLocations;
import java.io.File;

/**
 *
 * @author Nathan Templon
 */
public class MainMenuScreen implements Screen {
    
    // Constants
    public static final String MENU_FONT = "arial12.fnt";
    

    // Fields
    private final OmterraGame game;
    
    private BitmapFont font;
    private Skin skin; // The skin for the main menu controls
    private Stage stage;
    private Table table;
    private TextButton newGameButton;


    // Initialization
    public MainMenuScreen(OmterraGame game) {
        this.game = game;
    }


    // Screen Implementation
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.stage.act();
        this.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // True buts 0, 0 at the bottom left corner, false or omission buts 0, 0 at the center
        this.stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        this.init();
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }


    // Private Methods
    private void init() {
        this.font = this.game.getAssetManager().get(new File(FileLocations.FONTS_DIRECTORY, MENU_FONT).getPath());
//        this.font = new BitmapFont();
        this.buildDefaultSkin();
        
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);// Make the stage consume events
        
        // Add and arrange the buttons
        this.table = new Table();
        this.table.setFillParent(true);

        this.newGameButton = new TextButton("New Game", this.skin); // Use the initialized skin
        this.newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainMenuScreen.this.onNewGameClick();
            }
        });
        
        this.table.add(this.newGameButton).width(250).height(75).row();
        
        this.stage.addActor(this.table);
    }
    
    private void buildDefaultSkin() {
        this.skin = new Skin();

        this.skin.add("default", this.font);

        // Set the background texture
        Pixmap pixmap = new Pixmap((int) Gdx.graphics.getWidth() / 4, (int) Gdx.graphics.getHeight() / 10,
                Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        this.skin.add("background", new Texture(pixmap));

        //Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = this.skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.down = this.skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = this.skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.over = this.skin.newDrawable("background", Color.GRAY);
        textButtonStyle.font = this.skin.getFont("default");
        this.skin.add("default", textButtonStyle);
    }
    
    private void onNewGameClick() {
        this.game.setState(OmterraGame.GameStates.LEVEL);
    }

}
