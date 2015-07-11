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

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.Disposable
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.EntityEventArgs
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.MovementSystem.MovementDirections
import com.jupiter.europa.entity.ability.BasicAbilityCategories
import com.jupiter.europa.entity.messaging.WalkRequestMessage
import com.jupiter.europa.screen.overlay.CircleMenu
import com.jupiter.europa.screen.overlay.CircleMenuItem
import com.jupiter.europa.screen.overlay.PartyOverlay
import com.jupiter.europa.screen.overlay.PauseMenu
import com.jupiter.europa.world.Level
import com.jupiter.ganymede.event.Listener
import java.awt.MouseInfo
import java.awt.Point
import java.util.HashMap
import java.util.HashSet

/**

 * @author Nathan Templon
 */
public class LevelScreen : OverlayableScreen() {


    // Fields
    private val camera: OrthographicCamera // The camera for viewing the map
    private var level: Level? = null // The current level being rendered
    private var mapRender: LevelRenderer? = null  // The map renderer for the current map
    private val keysDown = HashSet<Int>()

    public var pauseKey: Int = Keys.ESCAPE
    private var pauseMenu: PauseMenu? = null

    private val fpslog = FPSLogger()

    private var width: Int = 0
    private var height: Int = 0
    private var partyOverlay: PartyOverlay? = null


    // Properties
    public fun setLevel(level: Level) {
        this.level = level

        val focusedSprite = Mappers.render.get(this.level!!.controlledEntity).sprite
        this.camera.position.set(focusedSprite.getX() + focusedSprite.getWidth() / 2.0f, focusedSprite.getY() + focusedSprite.getHeight() / 2.0f, 0f)

        this.mapRender = LevelRenderer(level)

        if (this.partyOverlay != null) {
            this.removeOverlay(this.partyOverlay!!)
        }

        this.partyOverlay = PartyOverlay(EuropaGame.game.party!!)
        this.partyOverlay?.addEntityClickListener(Listener { args -> this.onSidebarEntityClicked(args) })
        this.addOverlay(this.partyOverlay!!)
    }

    public fun getMouseLookSensitivity(): Int {
        return 5
    }


    init {

        this.camera = OrthographicCamera(Gdx.graphics.getWidth().toFloat(), Gdx.graphics.getHeight().toFloat())
        this.multiplexer.addProcessor(0, LevelInputProcessor(this))
    }


    // Screen Implementation
    override fun render(f: Float) {
        this.updateInput()

        // OpenGL code to clear the screen
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f)
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT)

        this.updateCamera()

        if (this.mapRender != null) {
            this.mapRender!!.setView(this.camera)
            this.mapRender!!.getBatch().setColor((this.getTint()))
            this.mapRender!!.render()
        }

        this.renderOverlays()
        // Record metrics if in debug mode
        //        if (EuropaGame.DEBUG) {
        //            fpslog.log();
        //        }
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)

        this.width = width
        this.height = height

        this.camera.viewportWidth = width / EuropaGame.SCALE
        this.camera.viewportHeight = height / EuropaGame.SCALE
        this.camera.update()

        for (overlay in this.getOverlays()) {
            overlay.resize(width, height)
        }
    }

    override fun show() {
        super.show()

        this.pauseMenu = PauseMenu()
        this.pauseMenu!!.exitKey = this.pauseKey

        EuropaGame.game.audioService!!.playMusic(this.level!!.musicType)
    }

    override fun hide() {
        EuropaGame.game.audioService!!.stop()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
        // Get rid of all the native resources
        this.mapRender?.dispose()
        for (overlay in this.getOverlays()) {
            if (overlay is Disposable) {
                overlay.dispose()
            }
        }
    }


    // Private Methods
    private fun updateInput() {
        var deltaX = 0
        var deltaY = 0
        for (key in this.keysDown) {
            if (MOVEMENT_MAP.containsKey(key)) {
                val direction = MOVEMENT_MAP.get(key) ?: MovementDirections.UP
                deltaX += direction.deltaX
                deltaY += direction.deltaY
            }
        }

        val totalDirection = MovementDirections.getSingleStepDirectionFor(deltaX, deltaY)
        if (totalDirection != null) {
            EuropaGame.game.messageSystem.publish(WalkRequestMessage(this.level!!.controlledEntity!!, totalDirection))
        }
    }

    private fun updateCamera() {
        if (EuropaGame.game.isSuspended()) {
            return
        }

        val mouseLoc = MouseInfo.getPointerInfo().getLocation()
        val frameLoc = EuropaGame.game.getFrameLocation()
        val insets = EuropaGame.game.getContainingInsets()

        val mouseX = mouseLoc.x - frameLoc.x - insets.left
        val mouseY = mouseLoc.y - frameLoc.y - insets.top

        if (mouseX <= 0) {
            // MouseX = 0 -> We should move left
            val newX = Math.max(0, this.camera.position.x.toInt() - this.getMouseLookSensitivity())
            this.camera.position.set(newX.toFloat(), this.camera.position.y, 0f)
        } else if (mouseX >= this.width - 1) {
            // MouseX = width -> We should move right
            val newX = Math.min(this.level!!.pixelWidth, this.camera.position.x.toInt() + this.getMouseLookSensitivity())
            this.camera.position.set(newX.toFloat(), this.camera.position.y, 0f)
        }

        if (mouseY >= this.height - 1) {
            val newY = Math.max(0, this.camera.position.y.toInt() - this.getMouseLookSensitivity())
            this.camera.position.set(this.camera.position.x, newY.toFloat(), 0f)
        } else if (mouseY <= 0) {
            val newY = Math.min(this.level!!.pixelHeight, this.camera.position.y.toInt() + this.getMouseLookSensitivity())
            this.camera.position.set(this.camera.position.x, newY.toFloat(), 0f)
        }

        this.camera.update()
    }

    private fun onSidebarEntityClicked(args: EntityEventArgs) {
        this.centerOn(args.entity)
    }

    private fun centerOn(entity: Entity) {
        if (Families.renderables.matches(entity)) {
            val focusedSprite = Mappers.render.get(entity).sprite
            this.camera.position.set(focusedSprite.getX() + focusedSprite.getWidth() / 2.0f, focusedSprite.getY() + focusedSprite.getHeight() / 2.0f, 0f)
        }
    }

    private fun screenToTile(screenX: Int, screenY: Int): Point {
        // Camera position is center screen
        val xOff = (screenX / EuropaGame.SCALE) - (this.camera.viewportWidth / 2)
        val yOff = -1 * ((screenY / EuropaGame.SCALE) - (this.camera.viewportHeight / 2))

        val x = (this.camera.position.x + xOff) / this.level!!.tileWidth
        val y = (this.camera.position.y + yOff) / this.level!!.tileWidth
        return Point(Math.floor(x.toDouble()).toInt(), Math.floor(y.toDouble()).toInt())
    }

    private fun onEntityClick(entity: Entity) {
        if (Families.renderables.matches(entity) && Families.abilitied.matches(entity)) {
            val focusedSprite = Mappers.render.get(entity).sprite
            val popup = CircleMenu(
                    Point(Math.round((focusedSprite.getX() + focusedSprite.getWidth() / 2.0f - this.camera.position.x) / EuropaGame.SCALE + this.camera.viewportWidth / 2.0f),
                            Math.round((focusedSprite.getY() + focusedSprite.getHeight() / 2.0f - this.camera.position.y) / EuropaGame.SCALE + this.camera.viewportHeight / 2.0f)),
                    Mappers.abilities[entity].getAbilities(BasicAbilityCategories.ALL_ABILITIES)
                            .map { ability ->
                                CircleMenuItem(ability.icon, ability.name, Runnable { println(ability.name) })
                            }.toList())
            this.addOverlay(popup)
        }
    }


    // Inner Classes
    public class LevelInputProcessor(private val screen: LevelScreen) : InputProcessor {

        override fun keyDown(i: Int): Boolean {
            if (POLLED_INPUTS.contains(i)) {
                this.screen.keysDown.add(i)
                return true
            } else if (i == screen.pauseKey) {
                if (!EuropaGame.game.isSuspended()) {
                    this.screen.addOverlay(this.screen.pauseMenu!!)
                }
            }
            return false
        }

        override fun keyUp(i: Int): Boolean {
            return this.screen.keysDown.remove(i)
        }

        override fun keyTyped(c: Char): Boolean {
            return false
        }

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            if (button != Input.Buttons.LEFT) {
                return false
            }

            val entity = this.screen.level!!.entityLayer!!.entityAt(this.screen.screenToTile(screenX, screenY)) ?: return false

            this.screen.onEntityClick(entity)
            return true
        }

        override fun touchUp(i: Int, i1: Int, i2: Int, i3: Int): Boolean {
            return false
        }

        override fun touchDragged(i: Int, i1: Int, i2: Int): Boolean {
            return false
        }

        override fun mouseMoved(x: Int, y: Int): Boolean {
            return false
        }

        override fun scrolled(i: Int): Boolean {
            return false
        }

    }

    companion object {

        // Constants
        public val POLLED_INPUTS: Set<Int> = getUsedInputs()
        public val MOVEMENT_MAP: Map<Int, MovementDirections> = getMovementKeys()


        // Static Methods
        private fun getUsedInputs(): Set<Int> {
            val keys = HashSet<Int>()

            keys.add(Keys.W)
            keys.add(Keys.A)
            keys.add(Keys.S)
            keys.add(Keys.D)

            return keys
        }

        private fun getMovementKeys(): Map<Int, MovementDirections> {
            val keys = HashMap<Int, MovementDirections>()

            keys.put(Keys.W, MovementDirections.UP)
            keys.put(Keys.A, MovementDirections.LEFT)
            keys.put(Keys.S, MovementDirections.DOWN)
            keys.put(Keys.D, MovementDirections.RIGHT)

            return keys
        }
    }

}
