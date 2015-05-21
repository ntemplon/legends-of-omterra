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
package com.jupiter.europa.screen.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.screen.OverlayableScreen;

/**
 *
 * @author Nathan Templon
 */
public class Scene2DOverlay implements Overlay {

    // Fields
    private final boolean blocking;
    
    private Color tint;
    
    private OverlayableScreen screen;
    private Stage stage;
    
    
    // Properties
    @Override
    public final boolean isBlocking() {
        return this.blocking;
    }
    
    public final Color getTint() {
        return this.tint;
    }
    
    public final void setTint(Color tint) {
        this.tint = tint;
    }
    
    public final OverlayableScreen getScreen() {
        return this.screen;
    }
    
    public final Stage getStage() {
        return this.stage;
    }

    public Color getBackgroundTint() {
        return Color.WHITE;
    }
    
    
    // Initialization
    public Scene2DOverlay() {
        this(true);
    }
    
    public Scene2DOverlay(boolean blocking) {
        this.blocking = blocking;
    }


    // Public Methods
    @Override
    public void added(OverlayableScreen screen) {
        this.screen = screen;
        if (this.getBackgroundTint() != null) {
            this.screen.setTint(this.getBackgroundTint());
        }
        
        this.stage = new Stage(new ScreenViewport());
        
        if (this.isBlocking()) {
            EuropaGame.game.suspend();
        }
    }

    @Override
    public void removed() {
        if (this.isBlocking() && EuropaGame.game.isSuspended()) {
            EuropaGame.game.wake();
        }

        if (this.getBackgroundTint() != null) {
            this.screen.setTint(Color.WHITE);
        }
    }

    @Override
    public void render() {
        if (this.getTint() != null) {
            this.getStage().getBatch().setColor(this.getTint());
            this.getStage().getActors().iterator().forEachRemaining(actor -> {
                actor.setColor(this.getTint());
                if (actor instanceof Group) {
                    this.shadeChildren((Group) actor, this.getTint());
                }
            });
        }
        this.stage.act();
        this.stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        // True buts 0, 0 at the bottom left corner, false or omission puts 0, 0 at the center
        this.stage.getViewport().update(width, height, true);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (this.stage != null) {
            return this.stage.keyDown(keycode) || this.blocking;
        }
        return this.blocking;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (this.stage != null) {
            return this.stage.keyUp(keycode) || this.blocking;
        }
        return this.blocking;
    }

    @Override
    public boolean keyTyped(char character) {
        if (this.stage != null) {
            return this.stage.keyTyped(character) || this.blocking;
        }
        return this.blocking;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (this.stage != null) {
            return this.stage.touchDown(screenX, screenY, pointer, button) || this.blocking;
        }
        return this.blocking;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (this.stage != null) {
            return this.stage.touchUp(screenX, screenY, pointer, button) || this.blocking;
        }
        return this.blocking;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (this.stage != null) {
            return this.stage.touchDragged(screenX, screenY, pointer) || this.blocking;
        }
        return this.blocking;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (this.stage != null) {
            return this.stage.mouseMoved(screenX, screenY) || this.blocking;
        }
        return this.blocking;
    }

    @Override
    public boolean scrolled(int amount) {
        if (this.stage != null) {
            return this.stage.scrolled(amount) || this.blocking;
        }
        return this.blocking;
    }


    // Private Methods
    private void shadeChildren(Group group, Color color) {
        group.getChildren().iterator().forEachRemaining(actor -> {
            actor.setColor(color);
            if (actor instanceof Group) {
                this.shadeChildren((Group) actor, color);
            }
        });
    }
    
}
