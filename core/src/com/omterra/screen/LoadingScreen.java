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
import com.omterra.OmterraGame;
import com.omterra.io.OmterraAssetManager;

/**
 *
 * @author Nathan Templon
 */
public class LoadingScreen implements Screen {
    
    // Constants
    public static final int MINIMUM_LOADING_TIME = 500; // Minimum time that the loading screen will be displayed, in ms.
    
    
    // Fields
    private final OrthographicCamera camera; // The camera for viewing the map
    private final OmterraGame game;
    private final OmterraAssetManager assetManager;
    
    private long startTime;
    
    
    // Initialization
    public LoadingScreen(OmterraGame game) {
        this.camera = new OrthographicCamera(640, 480);
        this.game = game;
        this.assetManager = game.getAssetManager();
    }
    

    // Screen implementation
    @Override
    public void render(float delta) {
        // OpenGL code to clear the screen
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1); // Clear white
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        
//        float progress = this.assetManager.getProgress();
        
        if (this.assetManager.update() && (System.nanoTime() - this.startTime) / 1000000 > MINIMUM_LOADING_TIME) {
            this.game.loadWorldData();
            this.game.setState(OmterraGame.GameStates.LEVEL);
        }
    }

    @Override
    public void resize(int width, int height) {
        this.camera.viewportWidth = width / OmterraGame.SCALE;
        this.camera.viewportHeight = height / OmterraGame.SCALE;
        camera.update();
    }

    @Override
    public void show() {
        assetManager.loadInternalResources();
        this.startTime = System.nanoTime();
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
    
}
