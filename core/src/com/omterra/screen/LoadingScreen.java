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
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.omterra.EmergenceGame;
import com.omterra.io.EmergenceAssetManager;
import com.omterra.threading.NotifyingThread;
import java.text.DecimalFormat;

/**
 *
 * @author Nathan Templon
 */
public class LoadingScreen implements Screen {
    
    // Constants
    public static final int MINIMUM_LOADING_TIME = 500; // Minimum time that the loading screen will be displayed, in ms.
    
    
    // Fields
    private final OrthographicCamera camera; // The camera for viewing the map
    private final EmergenceGame game; // The game that we will wait to load data
    private final EmergenceAssetManager assetManager; // The asset manager who we will wait to load all assets
    
    private long loadTime; // The time at which we started loading assets
    private boolean startedDataLoadingThread = false; // Whether or not the data loading thread has started yet
    private boolean loadingComplete = false; // True if all loading is completed
    private NotifyingThread dataLoadingThread; // The thread that handles the game loading data, separate from the assets.
    
    private final DecimalFormat format = new DecimalFormat("##");
    private final Batch batch = new SpriteBatch();
    private final BitmapFont font = new BitmapFont();
    
    
    // Initialization
    public LoadingScreen(EmergenceGame game) {
        this.camera = new OrthographicCamera(640, 480);
        this.game = game;
        this.assetManager = game.getAssetManager();
    }
    

    // Screen implementation
    @Override
    public void render(float delta) {
        // OpenGL code to clear the screen
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1); // Clear black
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        
        float progress = this.assetManager.getProgress();
        
        if (!this.startedDataLoadingThread && this.assetManager.update()) {
            this.dataLoadingThread = new NotifyingThread(() -> this.game.loadWorldData());
            this.dataLoadingThread.addListener((Thread thread) -> this.loadingComplete = true);
            this.dataLoadingThread.start();
            this.startedDataLoadingThread = true;
        }
        
        if (loadingComplete && this.minimumTimeElapsed()) {
            this.game.setState(EmergenceGame.GameStates.MAIN_MENU);
            this.dispose();
        }
        
        // Rendering code
        batch.setProjectionMatrix(camera.combined);
        
        batch.begin();
        
        this.font.setScale(1f);
        String message = "Loading: " + this.format.format(progress * 100) + "%";
        TextBounds bounds = this.font.getBounds(message);
        this.font.draw(batch, message, -1 * (bounds.width) / (2.0f * this.font.getScaleX()), (bounds.height / (2.0f * this.font.getScaleY())));
        
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        this.camera.viewportWidth = width;
        this.camera.viewportHeight = height;
        this.camera.update();
    }

    @Override
    public void show() {
        this.assetManager.loadInternalResources();
        this.loadTime = System.nanoTime();
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
        
    }
    
    
    // Private Methods
    private boolean minimumTimeElapsed() {
        return (System.nanoTime() - this.loadTime) / 1000000 > MINIMUM_LOADING_TIME;
    }
    
}
