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

package com.jupiter.europa.screen.overlay

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.screen.OverlayableScreen

/**

 * @author Nathan Templon
 */
public open class Scene2DOverlay @jvmOverloads constructor(private val blocking: Boolean = true) : Overlay {

    public var tint: Color? = null
    public var screen: OverlayableScreen? = null
        private set
    public var stage: Stage? = null
        private set


    // Properties
    override fun isBlocking(): Boolean {
        return this.blocking
    }

    public open fun getBackgroundTint(): Color? {
        return Color.WHITE
    }


    // Public Methods
    override fun added(screen: OverlayableScreen) {
        this.screen = screen
        if (this.getBackgroundTint() != null) {
            this.screen!!.setTint(this.getBackgroundTint() ?: Color.WHITE)
        }

        this.stage = Stage(ScreenViewport())

        if (this.isBlocking()) {
            EuropaGame.game.suspend()
        }
    }

    override fun removed() {
        if (this.isBlocking() && EuropaGame.game.isSuspended()) {
            EuropaGame.game.wake()
        }

        if (this.getBackgroundTint() != null) {
            this.screen!!.setTint(Color.WHITE)
        }
    }

    override fun render() {
        if (this.tint != null) {
            this.stage?.getBatch()?.setColor(this.tint)
            this.stage?.getActors()?.forEach { actor ->
                actor.setColor(this.tint)
                if (actor is Group) {
                    this.shadeChildren(actor, this.tint ?: Color.WHITE)
                }
            }
        }
        this.stage?.act()
        this.stage?.draw()
    }

    override fun resize(width: Int, height: Int) {
        // True buts 0, 0 at the bottom left corner, false or omission puts 0, 0 at the center
        this.stage!!.getViewport().update(width, height, true)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (this.stage != null) {
            return this.stage!!.keyDown(keycode) || this.blocking
        }
        return this.blocking
    }

    override fun keyUp(keycode: Int): Boolean {
        if (this.stage != null) {
            return this.stage!!.keyUp(keycode) || this.blocking
        }
        return this.blocking
    }

    override fun keyTyped(character: Char): Boolean {
        if (this.stage != null) {
            return this.stage!!.keyTyped(character) || this.blocking
        }
        return this.blocking
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (this.stage != null) {
            return this.stage!!.touchDown(screenX, screenY, pointer, button) || this.blocking
        }
        return this.blocking
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (this.stage != null) {
            return this.stage!!.touchUp(screenX, screenY, pointer, button) || this.blocking
        }
        return this.blocking
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        if (this.stage != null) {
            return this.stage!!.touchDragged(screenX, screenY, pointer) || this.blocking
        }
        return this.blocking
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        if (this.stage != null) {
            return this.stage!!.mouseMoved(screenX, screenY) || this.blocking
        }
        return this.blocking
    }

    override fun scrolled(amount: Int): Boolean {
        if (this.stage != null) {
            return this.stage!!.scrolled(amount) || this.blocking
        }
        return this.blocking
    }


    // Private Methods
    private fun shadeChildren(group: Group, color: Color) {
        group.getChildren().forEach { actor ->
            actor.setColor(color)
            if (actor is Group) {
                this.shadeChildren(actor, color)
            }
        }
    }

}
