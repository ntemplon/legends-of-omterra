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
import com.badlogic.gdx.graphics.FPSLogger;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.omterra.OmterraGame;
import com.omterra.world.Level;

/**
 *
 * @author Nathan Templon
 */
public class LevelScreen implements Screen {

    // Fields
    private final OrthographicCamera camera; // The camera for viewing the map
    private Level level; // The current level being rendered
    private TiledMap map;  // The map of the current level
    private LevelRenderer mapRender;  // The map renderer for the current map

    private boolean paused = false;  // Whether or not the game is currently paused

    private final FPSLogger fpslog = new FPSLogger();


    // Properties
    public void setLevel(Level level) {
        this.level = level;
        this.map = level.getMap();

        // Debug code: set view to center of map
        this.camera.position.set(this.level.getPixelWidth() / 2.0f, this.level.getPixelHeight() / 2.0f, 0.0f);

        this.mapRender = new LevelRenderer(this.level);
    }


    // Initialization
    public LevelScreen() {
        this.camera = new OrthographicCamera(640, 480);
    }


    // Screen Implementation
    @Override
    public void render(float f) {
        // OpenGL code to clear the screen
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        camera.update();

        if (this.mapRender != null && !this.paused) {
            mapRender.setView(camera);
            mapRender.render();
        }

        // Record metrics if in debug mode
//        if (OmterraGame.DEBUG) {
//            fpslog.log();
//        }
    }

    @Override
    public void resize(int width, int height) {
        this.camera.viewportWidth = width / OmterraGame.SCALE;
        this.camera.viewportHeight = height / OmterraGame.SCALE;
        camera.update();
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {
        this.paused = true;
    }

    @Override
    public void resume() {
        this.paused = false;
    }

    @Override
    public void dispose() {
        // Get rid of all the native resources
        if (this.mapRender != null) {
            this.mapRender.dispose();
        }
    }


    // Inner Classes
    private class LevelRenderer extends OrthogonalTiledMapRenderer {

        // Initialization
        public LevelRenderer(Level level) {
            super(level.getMap());
        }


        // Custom OrthogonalTiledMapRenderer Implementation
        @Override
        public void render() {
            beginRender(); // Built - in method

            // Render each layer, in order
            for (MapLayer layer : map.getLayers()) {
                if (layer.isVisible()) {
                    if (layer instanceof TiledMapTileLayer) {
                        this.renderTileLayer((TiledMapTileLayer) layer);
                    } else {
                        for (MapObject object : layer.getObjects()) {
                            this.renderObject(object);
                        }
                    }
                }
            }

            endRender(); // Built-in method
        }

    }

}
