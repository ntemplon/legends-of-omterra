/*
 * The MIT License
 *
 * Copyright 2015 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
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
 *
 */

package com.jupiter.europa.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import com.jupiter.europa.screen.overlay.Overlay
import com.jupiter.ganymede.event.Listener
import com.jupiter.ganymede.property.Property

import java.util.LinkedHashSet

/**

 * @author Nathan Templon
 */
public abstract class OverlayableScreen : Screen, InputProcessor {

    // Fields
    public val overlayCamera: Camera
    private val overlays = LinkedHashSet<Overlay>()
    public val multiplexer: InputMultiplexer = InputMultiplexer()
    public val overlayBatch: Batch = SpriteBatch()

    private val tint = Property(Color.WHITE)

    public fun getOverlays(): Set<Overlay> {
        return this.overlays
    }

    public fun getTint(): Color {
        return this.tint.get() ?: Color.WHITE
    }

    public fun setTint(tint: Color) {
        this.tint.set(tint)
    }


    init {
        this.overlayCamera = OrthographicCamera(Gdx.graphics.getWidth().toFloat(), Gdx.graphics.getHeight().toFloat())
    }


    // Public Methods
    override fun show() {
    }

    override fun hide() {
    }

    override fun dispose() {
        for (overlay in this.overlays) {
            if (overlay is Disposable) {
                overlay.dispose()
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        this.overlayCamera.viewportWidth = width.toFloat()
        this.overlayCamera.viewportHeight = height.toFloat()
        this.overlayCamera.update()
    }

    public fun renderOverlays() {
        this.overlayCamera.update()
        this.overlayBatch.setProjectionMatrix(this.overlayCamera.combined)

        this.overlayBatch.begin()
        for (overlay in this.overlays) {
            overlay.render()
        }
        this.overlayBatch.end()
    }

    public fun addOverlay(overlay: Overlay) {
        this.overlays.add(overlay)
        this.multiplexer.addProcessor(0, overlay)
        overlay.added(this)
    }

    public fun removeOverlay(overlay: Overlay): Boolean {
        val wasPresent = this.overlays.remove(overlay)
        if (wasPresent) {
            this.multiplexer.removeProcessor(overlay)
            overlay.removed()
        }
        return wasPresent
    }

    public fun addTintChangedListener(listener: Listener<Property.PropertyChangedArgs<Color>>): Boolean {
        return this.tint.addPropertyChangedListener(listener)
    }

    public fun addTintChangedListener(listener: (Property.PropertyChangedArgs<Color>) -> Unit): Boolean = this.tint.addPropertyChangedListener(listener)

    public fun removeTintChangedListener(listener: Listener<Property.PropertyChangedArgs<Color>>): Boolean {
        return this.tint.removePropertyChangedListener(listener)
    }


    // InputProcessor Implementation
    override fun keyDown(i: Int): Boolean {
        return this.multiplexer.keyDown(i)
    }

    override fun keyUp(i: Int): Boolean {
        return this.multiplexer.keyUp(i)
    }

    override fun keyTyped(c: Char): Boolean {
        return this.multiplexer.keyTyped(c)
    }

    override fun touchDown(i: Int, i1: Int, i2: Int, i3: Int): Boolean {
        return this.multiplexer.touchDown(i, i1, i2, i3)
    }

    override fun touchUp(i: Int, i1: Int, i2: Int, i3: Int): Boolean {
        return this.multiplexer.touchUp(i, i1, i2, i3)
    }

    override fun touchDragged(i: Int, i1: Int, i2: Int): Boolean {
        return this.multiplexer.touchDragged(i, i1, i2)
    }

    override fun mouseMoved(i: Int, i1: Int): Boolean {
        return this.multiplexer.mouseMoved(i, i1)
    }

    override fun scrolled(i: Int): Boolean {
        return this.multiplexer.scrolled(i)
    }

}
