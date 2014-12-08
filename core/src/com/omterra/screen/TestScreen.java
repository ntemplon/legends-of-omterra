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
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.FPSLogger;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.omterra.OmterraGame;

/**
 *
 * @author Nathan Templon
 */
public class TestScreen implements Screen {

    // Fields
    private final FPSLogger fpslog;

    private final Camera camera;
    private final SpriteBatch batch;
    private final Texture img;
    private final Sprite sprite;


    // Initialization
    public TestScreen(int width, int height) {
        this.fpslog = new FPSLogger();

        this.camera = new OrthographicCamera(width, height);
        this.batch = new SpriteBatch();
        this.img = new Texture("badlogic.jpg");
        this.sprite = new Sprite(this.img);
        this.sprite.setOrigin(0, 0);
        this.sprite.setPosition(-1 * sprite.getWidth() / 2.0f,
                -1 * sprite.getHeight() / 2.0f);
    }


    // Screen Implementation
    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        this.camera.update();
        this.batch.setProjectionMatrix(camera.combined);
        
        this.batch.begin();
        this.sprite.draw(this.batch);
        this.batch.end();
        
        if (OmterraGame.DEBUG) {
            fpslog.log();
        }
    }
    
    @Override
    public void resize(int width, int height) {
        this.camera.viewportWidth = width;
        this.camera.viewportHeight = height;
    }

    @Override
    public void show() {

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
        // Get rid of all the native resources
        if (this.batch != null) {
            this.batch.dispose();
        }
        if (this.img != null) {
            this.img.dispose();
        }
    }

}
