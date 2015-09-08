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

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.io.FileLocations
import com.jupiter.europa.screen.OverlayableScreen
import com.jupiter.ganymede.event.Event
import com.jupiter.ganymede.event.EventWrapper
import java.awt.Point
import kotlin.properties.Delegates

/**
 * Created by nathan on 5/23/15.
 */
public class CircleMenu(private val center: Point, private val items: List<CircleMenu.CircleMenuItem>) : Scene2DOverlay(true) {

    // Properties
    private val onCloseNoSelection: Event<CircleMenu> = Event()
    private var firstUp = true
    private var selectionMade = false


    // Initialization
    init {
        items.forEach { item ->
            item.selectionMade.addListener { this.selectionMade = true }
        }
    }


    // Public Methods
    override fun added(screen: OverlayableScreen) {
        super<Scene2DOverlay>.added(screen)

        val cam = this.stage.getCamera()

        if (cam != null) {
            cam.viewportWidth = cam.viewportWidth / EuropaGame.SCALE
            cam.viewportHeight = cam.viewportHeight / EuropaGame.SCALE
            cam.update()
        }

        // The functional approach causes an issue, this one doesn't.  Huh.
        for (entry in this.items) {
            this.stage.addActor(entry)
        }
        //        this.entries.forEach { entry ->
        //            this.stage?.addActor(entry)
        //        }

        val spacing = PIF / (this.items.size() - 1)
        val radius = BACK.getRegionWidth().toFloat() * 1f * EuropaGame.SCALE

        var i = 0
        for (entry in this.items) {
            val angle = i * spacing

            val x = center.x * EuropaGame.SCALE - radius * Math.cos(angle.toDouble()).toFloat() - BACK.getRegionWidth() / 2.0f
            val y = center.y * EuropaGame.SCALE + radius * Math.sin(angle.toDouble()).toFloat() - BACK.getRegionHeight() / 2.0f
            entry.setPosition(x, y)
            entry.setScale(EuropaGame.SCALE)

            i++
        }
    }

    override fun resize(width: Int, height: Int) {
        val cam = this.stage.getCamera()
        if (cam != null) {
            cam.viewportWidth = width / EuropaGame.SCALE
            cam.viewportHeight = height / EuropaGame.SCALE
            cam.update()
        }
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (!this.firstUp) {
            if (!this.selectionMade) {
                this.onCloseNoSelection.dispatch(this)
            }

            this.screen?.removeOverlay(this)
        }
        this.firstUp = false
        return true
    }

    public fun addCloseNoSelectionListener(listener: (CircleMenu) -> Unit) {
        this.onCloseNoSelection.addListener(listener)
    }


    // Statics
    companion object {

        // Constants
        public val BACK_TEXTURE_NAME: String = "popup-item"
        public val HOVERED_TEXTURE_NAME: String = "popup-item-hover"

        public val PIF: Float = Math.PI.toFloat()
        public val MAX_ANGULAR_SPACING: Float = Math.toRadians(30.0).toFloat() // Radians


        // Static Variables
        public val BACK: TextureRegion by Delegates.lazy { EuropaGame.game.assetManager!!.get(FileLocations.HUD_ATLAS, javaClass<TextureAtlas>()).findRegion(BACK_TEXTURE_NAME) }
        public val BACK_HOVERED: TextureRegion by Delegates.lazy { EuropaGame.game.assetManager!!.get(FileLocations.HUD_ATLAS, javaClass<TextureAtlas>()).findRegion(HOVERED_TEXTURE_NAME) }
    }


    // Nested Classes
    abstract class CircleMenuItem : Actor() {
        public abstract val selectionMade: EventWrapper<CircleMenuEventArgs>
    }

    class BasicCircleMenuItem(private val icon: TextureRegion, private val text: String, private val action: () -> Unit) : CircleMenuItem() {

        // Properties
        private var back: TextureRegion = BACK

        private val selectionMadeEvent = Event<CircleMenuEventArgs>()
        public override val selectionMade: EventWrapper<CircleMenuEventArgs> = EventWrapper(selectionMadeEvent)


        // Initialization
        init {
            this.setBounds(0f, 0f, this.back.getRegionWidth().toFloat(), this.back.getRegionHeight().toFloat())
            this.addListener(object : InputListener() {
                override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                    if (button == Input.Buttons.LEFT) {
                        this@BasicCircleMenuItem.selectionMadeEvent.dispatch(CircleMenuEventArgs(this@BasicCircleMenuItem))
                        this@BasicCircleMenuItem.action.invoke()
                        return true
                    } else {
                        return false
                    }
                }

                override fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                    this@BasicCircleMenuItem.back = BACK_HOVERED
                }

                override fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                    this@BasicCircleMenuItem.back = BACK
                }
            })
        }


        // Public Methods
        override final fun draw(batch: Batch, parentAlpha: Float) {
            batch.draw(this.back, this.getX(), this.getY())
            batch.draw(this.icon, this.getX(), this.getY())
        }
    }

    class SubmenuItem(private val icon: TextureRegion, private val text: String, private val action: () -> Unit) : CircleMenuItem() {

        // Properties
        private var back: TextureRegion = BACK

        private val selectionMadeEvent = Event<CircleMenuEventArgs>()
        public override val selectionMade: EventWrapper<CircleMenuEventArgs> = EventWrapper(selectionMadeEvent)


        // Initialization
        init {
            this.setBounds(0f, 0f, this.back.getRegionWidth().toFloat(), this.back.getRegionHeight().toFloat())
            this.addListener(object : InputListener() {
                override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                    if (button == Input.Buttons.LEFT) {
                        this@SubmenuItem.selectionMadeEvent.dispatch(CircleMenuEventArgs(this@SubmenuItem))
                        this@SubmenuItem.action.invoke()
                        return true
                    } else {
                        return false
                    }
                }

                override fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                    this@SubmenuItem.back = BACK_HOVERED
                }

                override fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                    this@SubmenuItem.back = BACK
                }
            })
        }


        // Public Methods
        override final fun draw(batch: Batch, parentAlpha: Float) {
            batch.draw(this.back, this.getX(), this.getY())
            batch.draw(this.icon, this.getX(), this.getY())
        }

    }

    data class CircleMenuEventArgs(public val item: CircleMenuItem)
}
