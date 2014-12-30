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
import com.badlogic.gdx.graphics.FPSLogger;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.emergence.EmergenceGame;
import com.emergence.entity.Mappers;
import com.emergence.entity.MovementSystem.MovementDirections;
import com.emergence.entity.messaging.WalkRequestMessage;
import com.emergence.screen.overlay.PauseMenu;
import com.emergence.world.Level;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Nathan Templon
 */
public class LevelScreen extends OverlayableScreen {

    // Constants
    private static final Set<Integer> POLLED_INPUTS = getUsedInputs();
    private static final Map<Integer, MovementDirections> MOVEMENT_MAP = getMovementKeys();


    // Static Methods
    private static Set<Integer> getUsedInputs() {
        Set<Integer> keys = new HashSet<>();

        keys.add(Keys.W);
        keys.add(Keys.A);
        keys.add(Keys.S);
        keys.add(Keys.D);

        return keys;
    }

    private static Map<Integer, MovementDirections> getMovementKeys() {
        Map<Integer, MovementDirections> keys = new HashMap<>();

        keys.put(Keys.W, MovementDirections.UP);
        keys.put(Keys.A, MovementDirections.LEFT);
        keys.put(Keys.S, MovementDirections.DOWN);
        keys.put(Keys.D, MovementDirections.RIGHT);

        return keys;
    }


    // Fields
    private final OrthographicCamera camera; // The camera for viewing the map
    private Level level; // The current level being rendered
    private LevelRenderer mapRender;  // The map renderer for the current map
    private final Set<Integer> keysDown = new HashSet<>();

    public int pauseKey = Keys.ESCAPE;
    private PauseMenu pauseMenu;

    private final FPSLogger fpslog = new FPSLogger();


    // Properties
    public void setLevel(Level level) {
        this.level = level;

        // Debug code: set view to center of map
        this.camera.position.set(this.level.getPixelWidth() / 2.0f, this.level.getPixelHeight() / 2.0f, 0.0f);

        this.mapRender = new LevelRenderer(this.level);
    }

    public int getPauseKey() {
        return this.pauseKey;
    }

    public void setPauseKey(int key) {
        this.pauseKey = key;
    }


    // Initialization
    public LevelScreen() {
        super();

        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.getMultiplexer().addProcessor(new LevelInputProcesser());
    }


    // Screen Implementation
    @Override
    public void render(float f) {
        this.updateInput();

        // OpenGL code to clear the screen
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        Sprite focusedSprite = Mappers.render.get(this.level.getControlledEntity()).getSprite();
        this.camera.position.set(focusedSprite.getX() + focusedSprite.getWidth() / 2.0f,
                focusedSprite.getY() + focusedSprite.getHeight() / 2.0f, 0);
        this.camera.update();

        if (this.mapRender != null) {
            this.mapRender.setView(camera);
            this.mapRender.getBatch().setColor((this.getTint()));
            this.mapRender.render();
        }

        this.renderOverlays();
        // Record metrics if in debug mode
//        if (OmterraGame.DEBUG) {
//            fpslog.log();
//        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        this.camera.viewportWidth = width / EmergenceGame.SCALE;
        this.camera.viewportHeight = height / EmergenceGame.SCALE;
        this.camera.update();
    }

    @Override
    public void show() {
        super.show();

        this.pauseMenu = new PauseMenu();
        this.pauseMenu.setExitKey(this.getPauseKey());
        
        EmergenceGame.game.getAudioService().playMusic(this.level.getMusicType());
    }

    @Override
    public void hide() {
        EmergenceGame.game.getAudioService().stop();
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
        if (this.mapRender != null) {
            this.mapRender.dispose();
        }
    }


    // Private Methods
    private void updateInput() {
        int deltaX = 0;
        int deltaY = 0;
        for(int key : this.keysDown) {
            if (MOVEMENT_MAP.containsKey(key)) {
                MovementDirections direction = MOVEMENT_MAP.get(key);
                deltaX += direction.deltaX;
                deltaY += direction.deltaY;
            }
        }
        
        MovementDirections totalDirection = MovementDirections.getSingleStepDirectionFor(deltaX, deltaY);
        if (totalDirection != null) {
            EmergenceGame.game.getMessageSystem().publish(new WalkRequestMessage(this.level.getControlledEntity(), totalDirection));
        }
    }


    // Inner Classes
    private class LevelInputProcesser implements InputProcessor {

        @Override
        public boolean keyDown(int i) {
            if (POLLED_INPUTS.contains(i)) {
                LevelScreen.this.keysDown.add(i);
                return true;
            }
            else if (i == pauseKey) {
                if (!EmergenceGame.game.isSuspended()) {
                    LevelScreen.this.addOverlay(LevelScreen.this.pauseMenu);
                }
            }
            return false;
        }

        @Override
        public boolean keyUp(int i) {
            return LevelScreen.this.keysDown.remove(i);
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

    }

}
