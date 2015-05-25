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
package com.jupiter.europa.screen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Disposable;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.entity.EntityEventArgs;
import com.jupiter.europa.entity.Families;
import com.jupiter.europa.entity.Mappers;
import com.jupiter.europa.entity.MovementSystem.MovementDirections;
import com.jupiter.europa.entity.ability.BasicAbilityCategories;
import com.jupiter.europa.entity.messaging.WalkRequestMessage;
import com.jupiter.europa.screen.overlay.*;
import com.jupiter.europa.world.Level;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

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
    
    private int width;
    private int height;
    private PartyOverlay partyOverlay;


    // Properties
    public void setLevel(Level level) {
        this.level = level;

        Sprite focusedSprite = Mappers.render.get(this.level.getControlledEntity()).getSprite();
        this.camera.position.set(focusedSprite.getX() + focusedSprite.getWidth() / 2.0f, focusedSprite.getY() + focusedSprite.getHeight() / 2.0f, 0);

        this.mapRender = new LevelRenderer(this.level);

        if (this.partyOverlay != null) {
            this.removeOverlay(this.partyOverlay);
        }

        this.partyOverlay = new PartyOverlay(EuropaGame.game.getParty());
        this.partyOverlay.addEntityClickListener(this::onSidebarEntityClicked);
        this.addOverlay(this.partyOverlay);
    }

    public int getPauseKey() {
        return this.pauseKey;
    }

    public void setPauseKey(int key) {
        this.pauseKey = key;
    }
    
    public int getMouseLookSensitivity() {
        return 5;
    }


    // Initialization
    public LevelScreen() {
        super();

        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.getMultiplexer().addProcessor(0, new LevelInputProcessor());
    }


    // Screen Implementation
    @Override
    public void render(float f) {
        this.updateInput();

        // OpenGL code to clear the screen
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        this.updateCamera();

        if (this.mapRender != null) {
            this.mapRender.setView(this.camera);
            this.mapRender.getBatch().setColor((this.getTint()));
            this.mapRender.render();
        }

        this.renderOverlays();
        // Record metrics if in debug mode
//        if (EuropaGame.DEBUG) {
//            fpslog.log();
//        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        
        this.width = width;
        this.height = height;

        this.camera.viewportWidth = width / EuropaGame.SCALE;
        this.camera.viewportHeight = height / EuropaGame.SCALE;
        this.camera.update();
        
        this.getOverlays().stream().forEach((Overlay overlay) -> {
            overlay.resize(width, height);
        });
    }

    @Override
    public void show() {
        super.show();

        this.pauseMenu = new PauseMenu();
        this.pauseMenu.setExitKey(this.getPauseKey());
        
        EuropaGame.game.getAudioService().playMusic(this.level.getMusicType());
    }

    @Override
    public void hide() {
        EuropaGame.game.getAudioService().stop();
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
        this.getOverlays().stream()
                .filter(overlay -> overlay instanceof Disposable)
                .map(overlay -> (Disposable) overlay)
                .forEach(Disposable::dispose);
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
            EuropaGame.game.getMessageSystem().publish(new WalkRequestMessage(this.level.getControlledEntity(), totalDirection));
        }
    }
    
    private void updateCamera() {
        if (EuropaGame.game.isSuspended()) {
            return;
        }
        
        Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
        Point frameLoc = EuropaGame.game.getFrameLocation();
        Insets insets = EuropaGame.game.getContainingInsets();
        
        int mouseX = mouseLoc.x - frameLoc.x - insets.left;
        int mouseY = mouseLoc.y - frameLoc.y - insets.top;

        if (mouseX <= 0) {
            // MouseX = 0 -> We should move left
            int newX = Math.max(0, (int)this.camera.position.x - this.getMouseLookSensitivity());
            this.camera.position.set(newX, this.camera.position.y, 0);
        }
        else if (mouseX >= this.width - 1) {
            // MouseX = width -> We should move right
            int newX = Math.min(this.level.getPixelWidth(), (int)this.camera.position.x + this.getMouseLookSensitivity());
            this.camera.position.set(newX, this.camera.position.y, 0);
        }
        
        if (mouseY >= this.height - 1) {
            int newY = Math.max(0, (int)this.camera.position.y - this.getMouseLookSensitivity());
            this.camera.position.set(this.camera.position.x, newY, 0);
        }
        else if (mouseY <= 0) {
            int newY = Math.min(this.level.getPixelHeight(), (int)this.camera.position.y + this.getMouseLookSensitivity());
            this.camera.position.set(this.camera.position.x, newY, 0);
        }
        
        this.camera.update();
    }

    private void onSidebarEntityClicked(EntityEventArgs args) {
        this.centerOn(args.entity());
    }

    private void centerOn(Entity entity) {
        if (Families.renderables.matches(entity)) {
            Sprite focusedSprite = Mappers.render.get(entity).getSprite();
            this.camera.position.set(focusedSprite.getX() + focusedSprite.getWidth() / 2.0f, focusedSprite.getY() + focusedSprite.getHeight() / 2.0f, 0);
        }
    }

    private Point screenToTile(int screenX, int screenY) {
        // Camera position is center screen
        float xOff = (screenX / EuropaGame.SCALE) - (this.camera.viewportWidth / 2);
        float yOff = -1 * ((screenY / EuropaGame.SCALE) - (this.camera.viewportHeight / 2));

        float x = (this.camera.position.x + xOff) / this.level.getTileWidth();
        float y = (this.camera.position.y + yOff) / this.level.getTileWidth();
        return new Point((int) Math.floor(x), (int) Math.floor(y));
    }

    private void onEntityClick(Entity entity) {
        if (Families.renderables.matches(entity) && Families.abilitied.matches(entity)) {
            Sprite focusedSprite = Mappers.render.get(entity).getSprite();
            CircleMenu popup = new CircleMenu(new Point(
                    Math.round((focusedSprite.getX() + focusedSprite.getWidth() / 2.0f - this.camera.position.x) / EuropaGame.SCALE + this.camera.viewportWidth / 2.0f),
                    Math.round((focusedSprite.getY() + focusedSprite.getHeight() / 2.0f - this.camera.position.y) / EuropaGame.SCALE + this.camera.viewportHeight / 2.0f)),
                    Mappers.abilities.get(entity).getAbilities(BasicAbilityCategories.ALL_ABILITIES).stream()
                            .map(ability -> new CircleMenuItem(ability.getIcon(), ability.getName(), () -> System.out.println(ability.getName())))
                            .collect(Collectors.toList())
            );
            this.addOverlay(popup);
        }
    }


    // Inner Classes
    private class LevelInputProcessor implements InputProcessor {

        @Override
        public boolean keyDown(int i) {
            if (POLLED_INPUTS.contains(i)) {
                LevelScreen.this.keysDown.add(i);
                return true;
            }
            else if (i == pauseKey) {
                if (!EuropaGame.game.isSuspended()) {
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
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (button != Input.Buttons.LEFT) {
                return false;
            }

            Entity entity = LevelScreen.this.level.getEntityLayer().entityAt(LevelScreen.this.screenToTile(screenX, screenY));

            if (entity == null) {
                return false;
            }

            LevelScreen.this.onEntityClick(entity);
            return true;
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
        public boolean mouseMoved(int x, int y) {
            return false;
        }

        @Override
        public boolean scrolled(int i) {
            return false;
        }

    }

}
