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
package com.emergence.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.emergence.EmergenceGame;
import com.emergence.entity.MovementSystem.MovementDirections;
import com.emergence.entity.messaging.MovementRequestMessage;
import com.emergence.world.Level;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Nathan Templon
 */
public class LevelScreen implements Screen, InputProcessor {

    // Constants
    private static final Set<Integer> USED_INPUTS = getUsedInputs();
    private static final Set<Integer> MOVEMENT_KEYS = getMovementKeys();


    // Static Methods
    private static Set<Integer> getUsedInputs() {
        Set<Integer> keys = new HashSet<>();

        keys.add(Keys.W);
        keys.add(Keys.A);
        keys.add(Keys.S);
        keys.add(Keys.D);

        return keys;
    }

    private static Set<Integer> getMovementKeys() {
        Set<Integer> keys = new HashSet<>();

        keys.add(Keys.W);
        keys.add(Keys.A);
        keys.add(Keys.S);
        keys.add(Keys.D);

        return keys;
    }


    // Fields
    private final OrthographicCamera camera; // The camera for viewing the map
    private Level level; // The current level being rendered
    private LevelRenderer mapRender;  // The map renderer for the current map
    private final Set<Integer> keysDown = new HashSet<>();

    private boolean paused = false;  // Whether or not the game is currently paused

    private final FPSLogger fpslog = new FPSLogger();


    // Properties
    public void setLevel(Level level) {
        this.level = level;

        // Debug code: set view to center of map
        this.camera.position.set(this.level.getPixelWidth() / 2.0f, this.level.getPixelHeight() / 2.0f, 0.0f);

        this.mapRender = new LevelRenderer(this.level);
    }


    // Initialization
    public LevelScreen() {
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }


    // Screen Implementation
    @Override
    public void render(float f) {
        this.updateInput();
        
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
        this.camera.viewportWidth = width / EmergenceGame.SCALE;
        this.camera.viewportHeight = height / EmergenceGame.SCALE;
        this.camera.update();
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


    // InputProcessor Implementation
    @Override
    public boolean keyDown(int i) {
        if (USED_INPUTS.contains(i)) {
            this.keysDown.add(i);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return this.keysDown.remove(i);
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }


    // Private Methods
    private void updateInput() {
        this.keysDown.stream().forEach((Integer key) -> {
            switch (key) {
                case Keys.W:
                    EmergenceGame.game.getMessageSystem().publish(
                            new MovementRequestMessage(this.level.getControlledEntity(), MovementDirections.UP));
                    break;
                case Keys.A:
                    EmergenceGame.game.getMessageSystem().publish(
                            new MovementRequestMessage(this.level.getControlledEntity(), MovementDirections.LEFT));
                    break;
                case Keys.S:
                    EmergenceGame.game.getMessageSystem().publish(
                            new MovementRequestMessage(this.level.getControlledEntity(), MovementDirections.DOWN));
                    break;
                case Keys.D:
                    EmergenceGame.game.getMessageSystem().publish(
                            new MovementRequestMessage(this.level.getControlledEntity(), MovementDirections.RIGHT));
                    break;
            }
        });
    }

}
