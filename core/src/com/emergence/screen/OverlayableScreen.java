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
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.emergence.EmergenceGame;
import com.emergence.io.FileLocations;
import com.emergence.screen.overlay.Overlay;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author Nathan Templon
 */
public abstract class OverlayableScreen implements Screen, InputProcessor {
    
    // Fields
    private final Camera overlayCamera;
    private final Set<Overlay> overlays = new LinkedHashSet<>();
    private final InputMultiplexer multiplexer = new InputMultiplexer();
    private final Batch overlayBatch = new SpriteBatch();
    
    private Color tint = Color.WHITE;
    
    
    // Properties
    public final Camera getOverlayCamera() {
        return this.overlayCamera;
    }
    
    public final Batch getOverlayBatch() {
        return this.overlayBatch;
    }
    
    public final Color getTint() {
        return this.tint;
    }
    
    public final void setTint(Color tint) {
        this.tint = tint;
    }
    
    protected final InputMultiplexer getMultiplexer() {
        return this.multiplexer;
    }
    
    
    // Initialization
    public OverlayableScreen() {
        this.overlayCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    
    
    // Public Methods
    @Override
    public void show() {
        
    }
    
    @Override
    public void resize(int width, int height) {
        this.overlayCamera.viewportWidth = width;
        this.overlayCamera.viewportHeight = height;
        this.overlayCamera.update();
    }
    
    public void renderOverlays() {
        this.overlayCamera.update();
        this.overlayBatch.setProjectionMatrix(this.overlayCamera.combined);
        
        this.overlayBatch.begin();
        this.overlays.forEach((Overlay overlay) -> {
            overlay.render();
        });
        this.overlayBatch.end();
    }
    
    public void addOverlay(Overlay overlay) {
        this.overlays.add(overlay);
        this.multiplexer.addProcessor(overlay);
        overlay.added(this);
    }
    
    public boolean removeOverlay(Overlay overlay) {
        boolean wasPresent = this.overlays.remove(overlay);
        if (wasPresent) {
            this.multiplexer.removeProcessor(overlay);
            overlay.removed();
        }
        return wasPresent;
    }
    
    
    // InputProcessor Implementation
    @Override
    public boolean keyDown(int i) {
        return this.getMultiplexer().keyDown(i);
    }

    @Override
    public boolean keyUp(int i) {
        return this.getMultiplexer().keyUp(i);
    }

    @Override
    public boolean keyTyped(char c) {
        return this.getMultiplexer().keyTyped(c);
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return this.getMultiplexer().touchDown(i, i1, i2, i3);
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return this.getMultiplexer().touchUp(i, i1, i2, i3);
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return this.getMultiplexer().touchDragged(i, i1, i2);
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return this.getMultiplexer().mouseMoved(i, i1);
    }

    @Override
    public boolean scrolled(int i) {
        return this.getMultiplexer().scrolled(i);
    }
    
}
